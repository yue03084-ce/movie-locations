# 进度记录 progress.md

> **每天开始**：第一件事读本文件。
> **每天结束**：最后一件事更新本文件（进度、结论、未解决问题、文件清单）。
> 规划性内容看 `PROJECT_PLAN.md`；本文件记录**实际进展和状态**。

---

## 当前状态

- **进行到**：步骤 3（写第一个 API，返回假数据）—— 尚未开始写代码
- **已完成**：步骤 0（Git/GitHub）、步骤 1（环境）、步骤 2（Spring Boot 空项目）
- **MVP 目标**：完成到步骤 5（前后端连通），时间盒 2–3 周
- **协作模式约定**：简单基础的代码本人手敲、Claude 讲概念；脚手架和后期大量代码可让 Claude 直接写

---

## 已确定的结论

- 项目方向：电影取景地地图 + 旅行规划。**MVP 只做电影**，书/歌砍掉。
- 两大差异化亮点：① LLM 抽取 + 交叉验证管线；② 带约束的 TSP 路线优化（接金融工程背景）。
- 技术栈：Spring Boot (Java) 后端 + HTML/JS/Leaflet 前端 + PostgreSQL（步骤 6 才引入）。
- 本地环境：JDK 23（≥17 满足要求）、IntelliJ IDEA。
- Spring Boot 版本选 **3.5.16**（不用 4.x，为了教程/资料/AI 兼容性好）。
- 包名 `com.example.movielocations`（不能有连字符）；Artifact 名 `movie-locations`。
- 唯一依赖：**Spring Web**（其余按步骤逐步加）。
- GitHub 用户名 `yue03084-ce`；push 用 **Personal Access Token** 当密码（GitHub 不支持账户密码）。
- Git 原则：步骤 0 之后所有工作都在 Git 上，每完成一步 commit 一次。

---

## 未解决的问题 / 待办

- [ ] 步骤 3：写 `LocationController`（`@RestController` + `GET /api/locations?movie=xxx`，返回写死 JSON）
- [ ] 步骤 3：写第一个单元测试（MockMvc 测该接口），跑通 `mvn test`
- [ ] README 里的架构草图待补（手画拍照即可）
- [ ] PROJECT_PLAN.md 里步骤 0/1/2 的状态标记可更新为 `[x]`
- [ ] （远期）步骤 7 需要的数据源账号：TMDB API key、Wikidata/Nominatim 用法调研

---

## 文件清单（Git 已跟踪）

| 文件 | 作用 |
|---|---|
| `pom.xml` | Maven 项目配置（依赖：Spring Web） |
| `src/main/java/.../MovieLocationsApplication.java` | 项目启动入口（`main` 方法） |
| `src/main/resources/application.properties` | 应用配置（目前空） |
| `src/test/java/.../MovieLocationsApplicationTests.java` | 自动生成的测试类骨架 |
| `README.md` | 项目简介 + 目标架构 |
| `PROJECT_PLAN.md` | 完整落地计划（8 步 + 简历措辞） |
| `progress.md` | 本文件，每日进度记录 |
| `.gitignore` | 忽略 target/、.idea、.DS_Store 等 |
| `mvnw` / `mvnw.cmd` / `.mvn/` | Maven wrapper（无需改动） |

**尚未创建**：`LocationController.java`（步骤 3 要建）、前端 HTML（步骤 4）。

---

## 每日日志

### 2026-07-07（第 1 天）
- 完成步骤 0/1/2：环境确认、Spring Initializr 生成项目、成功启动（Tomcat on 8080，`Started ... in 0.6s`）。
- 建好 Git 仓库并 push 到 GitHub（`yue03084-ce/movie-locations`），用 PAT 认证。
- 补 README、`.gitignore` 加 `.DS_Store`。
- 修订了 PROJECT_PLAN.md（加入 Git、单元测试、Wikidata 验证、异步管线、TSP、CI/CD 等）。
- **下一步**：步骤 3，手敲 `LocationController`。
