# 电影取景地 × 旅行规划 —— 项目计划（修订版）

> 这是一份**活文档（living document）**。项目推进过程中，Claude Code 应持续参照本文件，并在每完成一步后更新对应状态与备注。

---

## 0. 项目一句话

用户在网页上输入一部**电影** → 系统从真实数据源 + LLM 抽取管线得到经过验证的**取景地** → 在地图上标注 → 在城市/时间约束下排出最优游览路线。

**作者背景**：金融工程本科 + 澳洲 ANU computing master 转码（第二年），有 Java 基础。本项目用于简历，需能撑起技术面试的深入追问。

**范围收敛**：MVP 只做电影。书/歌每加一种媒介，数据问题翻倍且简历不加分，永久砍掉或留到产品化阶段。

**两大差异化亮点**（面试故事线，其他都是脚手架）：
1. **LLM 抽取 + 真实数据源交叉验证管线**——不是 ChatGPT wrapper，有可量化的准确率
2. **带约束的路线优化（TSP 变体）**——衔接金融工程的量化/优化背景

---

## 1. 整体架构

三部分配合，一次只动一块，跑通一块再接下一块：

- **前端**：展示地图和取景地标记，触发抽取任务并轮询结果。
- **后端（项目主体）**：REST API + 异步抽取管线 + 验证层 + 路线优化。Java/Spring 的价值所在。
- **数据库**：取景地、抽取任务状态、验证结果。

**核心闭环**：前端发请求 → 后端返回 JSON → Leaflet 画点。
**深度闭环**：输入新电影 → 后端异步跑「LLM 抽取 → Wikidata/地理编码交叉验证 → 打置信度分 → 入库」→ 前端轮询拿到结果。

---

## 2. 技术栈

| 层 | 选型 | 说明 |
|---|---|---|
| 后端 | **Spring Boot (Java 17+)** | 顺应已有 Java 基础，澳洲就业市场吃香 |
| 前端 | **HTML + JavaScript + Leaflet** | MVP 阶段保持极简，不上 React |
| 地图 | **Leaflet + OpenStreetMap** | 免费、无需 API key |
| 数据库 | **PostgreSQL** | 第 6 步引入，之前用假数据 |
| 数据源 | **Wikidata (P915 取景地属性) / TMDB API / Wikipedia** | 真实 ground truth，验证层的锚点 |
| AI | **大模型 API**（后端异步调用） | 从非结构化文本抽取取景地，不凭空生成 |
| 地理编码 | **Nominatim (OSM)** | 校验坐标真实性 |
| 测试 | **JUnit 5 + Testcontainers** | 澳洲 junior 面试重点考察 |
| CI/部署 | **GitHub Actions + Docker + Render/Railway** | 必须有能点开的 live demo 链接 |

**关键概念速查**：
- **API**：后端的"菜单"，规定别人能向后端要什么、怎么要、返回什么。
- **JSON**：前后端之间传数据的通用格式，例：`[{ "name": "东京塔", "lat": 35.6586, "lng": 139.7454 }]`
- **异步任务**：LLM 调用要几秒，不能让 HTTP 请求干等。前端提交任务拿到 taskId，轮询查状态。
- **置信度分（confidence score）**：每个抽取出的取景地经多源验证后的可信程度。

---

## 3. 落地步骤

> 状态标记：`[ ]` 未开始 · `[~]` 进行中 · `[x]` 已完成
> **MVP 目标 = 完成到第 5 步，时间盒 2–3 周。** 步骤 1–6 是脚手架，快速通过；真正花时间的是第 7、8 步。
> **步骤 0 之后所有工作都在 Git 上进行，commit 历史本身就是面试材料。**

### 步骤 0 — Git 仓库 + README 骨架  `[x]`（第 1 天）
- **要实现的东西**：GitHub 仓库，README 含一句话简介和架构草图（手画拍照也行）。
- **完成 Milestone**：仓库建好，第一个 commit 推上去。
- **备注**：已完成。仓库 `yue03084-ce/movie-locations`（Public），push 用 Personal Access Token 认证（GitHub 不支持账户密码）。README 已写简介 + 目标架构；架构草图待补。`.gitignore` 补了 `.DS_Store`。

### 步骤 1 — 装好开发环境  `[x]`（第 1 天）
- **技术细节**：安装 JDK（Java 17+）、IntelliJ IDEA。确认 `java -version` 正常。
- **完成 Milestone**：IDE 里新建项目并成功运行 "Hello World"。
- **备注**：已完成。本地 JDK 23（≥17 满足）、IntelliJ IDEA 就绪。

### 步骤 2 — 建 Spring Boot 空项目  `[x]`（第 1–2 天）
- **技术细节**：[Spring Initializr](https://start.spring.io) 生成项目，勾选 `Spring Web`。
- **完成 Milestone**：启动后浏览器访问 `http://localhost:8080` 有响应。
- **备注**：已完成。Maven + Java 17 + Jar + Spring Boot 3.5.16，依赖仅 Spring Web，包名 `com.example.movielocations`。启动成功（Tomcat on 8080，`Started ... in 0.6s`）。

### 步骤 3 — 写第一个 API，返回假数据  `[ ]`（第 1 周内）
- **技术细节**：`@RestController`，暴露 `GET /api/locations?movie=xxx`，返回手写 JSON（2~3 个取景地，含 name / lat / lng）。**同时写第一个单元测试**（MockMvc 测该接口），养成习惯。
- **完成 Milestone**：Postman 能看到 JSON；`mvn test` 通过。
- **备注**：______

### 步骤 4 — 最简前端地图页  `[ ]`（第 1–2 周）
- **技术细节**：单 HTML 文件，引入 Leaflet，手写假坐标打 2 个 marker。此步不连后端。
- **完成 Milestone**：地图上有 2 个图钉，点击弹出名字。
- **备注**：______

### 步骤 5 — 前后端连通（★ MVP 里程碑）  `[ ]`（第 2–3 周）
- **技术细节**：前端 `fetch` 步骤 3 的 API，循环生成 marker。CORS 用配置类按环境精确放行前端 origin，**不用 `@CrossOrigin("*")` 糊过去**（面试可能问为什么）。
- **完成 Milestone**：输入/选择电影 → 地图自动出现取景地标记。第一个可演示版本完成。
- **备注**：______

### 步骤 6 — 接数据库 + 部署上线  `[ ]`（第 3–4 周）
- **要实现的东西**：数据入库 + **live demo 上线**。一个能点开的链接比多做两个功能重要。
- **技术细节**：
  - PostgreSQL + Spring Data JPA（Location 实体、Repository），手动录入一批真实取景地。
  - 集成测试用 **Testcontainers** 起真实 PostgreSQL。
  - **Dockerfile + GitHub Actions**（push 自动跑测试）+ 部署到 Render/Railway 免费档。
  - API key、数据库密码走环境变量，**绝不进代码库**。
- **完成 Milestone**：删掉写死数据后 API 仍能查库返回；任何人打开公网 URL 能用；CI 全绿。
- **备注**：______

### 步骤 7 — LLM 抽取 + 交叉验证管线（★ 深度亮点一）  `[ ]`（第 4–7 周，重点投入）
- **要实现的东西**：输入库里没有的新电影，系统自动抽取取景地、验证、打分、入库。
- **技术细节**：
  - **数据源优先于生成**：先查 Wikidata（P915 filming location）/ TMDB / Wikipedia 拿原始素材；LLM 的角色是**从非结构化文本抽取结构化取景地**，而非凭空生成——这是和 ChatGPT wrapper 的本质区别。
  - **验证层（有实质内容）**：① Nominatim 地理编码校验地点真实存在、坐标一致；② 与 Wikidata 交叉核对该地点确与该电影相关；③ 综合打**置信度分**，低分标记待人工复核而非直接丢弃。
  - **异步设计**：LLM 调用耗时数秒，同步接口会超时。`POST /api/extractions` 返回 taskId → 后端异步执行（`@Async` 或简单任务表）→ 前端轮询 `GET /api/extractions/{id}`。这是面试里比 CRUD 值钱得多的谈资。
  - **缓存**：同一部电影不重复调用 LLM（查库即缓存 + 失败重试策略）。
  - **量化成果**：选 20–30 部电影人工标注 ground truth，记录 precision / 幻觉率 / 验证层拦截率——直接写进简历。
- **完成 Milestone**：新电影输入 → 自动抽取、验证、入库、上图；有一份准确率评测数据。
- **备注**：______

### 步骤 8 — 路线优化（★ 深度亮点二，衔接量化背景）  `[ ]`（第 7–10 周）
- **要实现的东西**：给定一个城市的多个取景地 + 时间约束，排出合理游览顺序。
- **技术细节**：带约束的 TSP 变体。点少（≤10）可精确解（Held-Karp / 枚举），点多用启发式（最近邻 + 2-opt）。距离用 haversine 起步，后续可换真实路网。**这是金融工程 → 优化算法的故事线，面试时主动往这引。**
- **完成 Milestone**：勾选多个取景地 → 地图画出推荐路线及顺序。
- **备注**：______

---

## 4. 后续可延伸方向（按需选做）

- **推荐系统**：语义相似度 / 内容推荐。
- **产品化**：用户账号、收藏、分享行程、真实用户反馈；届时再考虑前端迁移 React。

---

## 5. 简历措辞（提前锚定，每一步都问：这步能写进这句话吗？）

> Built a Spring Boot web app that maps verified filming locations of movies:
> - Designed an **async LLM extraction pipeline** cross-validated against Wikidata and OSM geocoding, achieving **X% precision across N films** (hallucination rate reduced from Y% to Z% by the validation layer)
> - Implemented **constrained TSP itinerary optimization** (nearest-neighbour + 2-opt) for multi-location visit planning
> - Shipped with **CI (GitHub Actions), Docker, Testcontainers integration tests**, deployed on Render — [live demo]

X / N / Y / Z 在步骤 7 评测后填入。

---

## 6. AI 协作与模型使用约定

- **头脑风暴 / 定方向 / 架构取舍**：用 Claude（对话）+ 强推理模型。
- **写代码 / 调试 / 重构**：用 Claude Code。
- **典型流程**：在 Claude 里想清楚 → 在 Claude Code 里落地 → 卡在设计层面再回 Claude 讨论。

**重要原则（针对转码简历项目）**：
- AI 可以帮写，但**每一行都要能讲清为什么这么写**，尤其：Spring 依赖注入、分层架构、异常处理、CORS 配置、异步任务设计、事务边界——面试必问。
- 每步跑通后，主动让 AI 解释代码，不囤积不懂的部分往下走。
- 每个亮点准备一个「为什么不用 X」的答案（为什么不直接让 LLM 生成？为什么不上消息队列？为什么 MVP 不用 React？）。

---

## 7. 给 Claude Code 的说明

- 每次开工前先读本文件，确认当前进行到哪一步。
- 每完成一步：更新状态标记，在「备注」里记录实际做法、踩过的坑、关键决定。
- 若技术选型或步骤有调整，同步更新对应章节。
- 不要跳步；MVP 目标锁定在**步骤 5**，且步骤 1–5 时间盒为 2–3 周，脚手架部分不恋战。
