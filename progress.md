# 进度记录 progress.md

> **每天开始**：第一件事读本文件。
> **每天结束**：最后一件事更新本文件（进度、结论、未解决问题、文件清单）。
> 规划性内容看 `PROJECT_PLAN.md`；本文件记录**实际进展和状态**。

---

## 当前状态

- **进行到**：步骤 6 进行中（数据库部分过半）。**下次第一件事**：Controller 构造器注入 Repository、删假数据改 `repository.findByMovie(movie)`，重启后访问 `/api/locations?movie=xxx` 应返回 `[]`（空表）
- **已完成**：步骤 0–5（★ MVP 已达成）；步骤 6 已完成：Postgres 安装建库、JPA 依赖与配置、Location 实体（自动建表已验证）、LocationRepository
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

- [ ] 步骤 6 剩余：① Controller 改查库（进行中）② data.sql 种子数据（2–3 部电影、十来行）③ Testcontainers ④ Dockerfile + GitHub Actions ⑤ 部署 Render/Railway
- [ ] 旧的 LocationControllerTest 会因 Controller 改造而需调整（@WebMvcTest 需 mock Repository）——改完 Controller 记得跑一下测试看是否红
- [ ] JS 基础自学（zh.javascript.info 前几章 + async/DOM 小节，时间盒 3–5 小时）
- [ ] README 里的架构草图待补（手画拍照即可）
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

| `src/main/java/.../LocationController.java` | 第一个 API：`GET /api/locations?movie=xxx`，record DTO，返回写死数据 |
| `src/test/java/.../LocationControllerTest.java` | MockMvc 单元测试（`@WebMvcTest`） |
| `src/main/resources/static/index.html` | 前端地图页：Leaflet + OSM，fetch 后端 API 打点，搜索框 + 清旧 marker |
| `src/main/java/.../Location.java` | JPA 实体（@Entity，对应 location 表） |
| `src/main/java/.../LocationRepository.java` | Spring Data 接口（findByMovie 派生查询，实现运行时生成） |

---

## 每日日志

### 2026-07-07（第 1 天）
- 完成步骤 0/1/2：环境确认、Spring Initializr 生成项目、成功启动（Tomcat on 8080，`Started ... in 0.6s`）。
- 建好 Git 仓库并 push 到 GitHub（`yue03084-ce/movie-locations`），用 PAT 认证。
- 补 README、`.gitignore` 加 `.DS_Store`。
- 修订了 PROJECT_PLAN.md（加入 Git、单元测试、Wikidata 验证、异步管线、TSP、CI/CD 等）。
- **下一步**：步骤 3，手敲 `LocationController`。

### 2026-07-08（第 2 天）
- 完成步骤 3（本人手敲，Claude 讲概念）：`LocationController`（record DTO + `@RequestParam`）+ `LocationControllerTest`（`@WebMvcTest` + MockMvc），测试通过（349ms）。
- 概念课：客户端/服务器模型、Spring 路由（`@RestController`/`@GetMapping`）、对象→JSON 自动序列化（Jackson）、依赖注入（`@Autowired`）、HTTP 状态码、`@WebMvcTest` vs `@SpringBootTest`。
- 踩坑：`jsonPath` 误导入 `MockRestRequestMatchers` 导致需要强转 → 换成 `MockMvcResultMatchers` 后解决。经验：断言需要强转 ≈ import 错了。
- Mockito/JDK23 动态 agent 警告确认无害。
- **下一步**：步骤 4，单 HTML + Leaflet 地图打 2 个假 marker。

### 2026-07-11（第 3 天）
- 完成步骤 4（本人手敲）：`static/index.html`，Leaflet 地图 + OSM 底图 + 2 个 marker（东京柏悦酒店、涩谷十字路口），点击弹出名字。
- 概念课：客户端渲染三件套（HTML 结构 / CSS 外观 / JS 行为）、CDN 引库 ≈ Maven 引依赖、CSS 放 head JS 放 body 末尾的原因、Leaflet 三句核心 API。
- 踩坑：HTML/CSS 里误用 `//` 注释（三种语言注释语法不同）；社区版 IDEA 不支持 JS 高亮导致错误难发现。
- IDE 升级：申请 JetBrains Educational Pack（免费，至 2027-07），换用 IntelliJ IDEA Ultimate。
- 补基础方向确定：计算机网络只补应用层子集（HTTP → URL/端口 → DNS → TCP/UDP → HTTPS），资源《图解 HTTP》+ MDN。
- **下一步**：步骤 5（★ MVP 里程碑），前端 fetch 后端 API、循环打点，处理 CORS。

### 2026-07-12（第 4 天）★ MVP 达成
- 完成步骤 5（本人手敲）：删写死 marker，改为 `fetch` 后端 API + `response.json()` + 循环打点；加 input/button，`addEventListener('click')` 触发搜索。
- 概念课：fetch 与地址栏请求的区别（页面跳转 vs 后台拿数据）、任何程序都能发 HTTP 请求（客户端/服务器是角色不是身份）、`async/await`、模板字符串 `` `${}` ``、DOM（`document.getElementById`）、事件监听（JS 箭头函数 ≈ Java lambda）、CORS 与同源策略（本项目同源故未触发，面试话术已备进 PROJECT_PLAN 备注）。
- 踩坑：URL `movie = xxx` 带空格导致参数名错；函数定义了没调用；误跑测试类当启动应用（测试断言完即退出、不监听端口）→ 学会区分跑测试 vs 跑应用、认 `Started ...` 日志、F12 排查三步法（连接拒绝→404→Console）。
- **下一步**：先做小优化（清旧 marker），然后步骤 6：PostgreSQL + JPA + Testcontainers + Docker + CI + 上线。

### 2026-07-15（第 5 天）
- 完成"清旧 marker"优化：`markers` 数组登记图钉，`loadLocations` 开头 `removeLayer` 清场；Controller 临时按 movie 参数返回不同数据验证通过。
- 概念课：顶层代码只跑一次 vs 事件驱动（前后端对称）、数组存引用（同一对象才能精确摘除）、`console.log` + F12 Console 调试、改 Java 要重启 / 改静态文件要 ⌘F9 rebuild / 页面状态刷新即清零。
- 决定补 JS 基础：范围锁定 6 项（let/const、对象与数组、函数、常用操作、async/fetch、DOM 三件事），资源 zh.javascript.info。
- **下一步**：步骤 6。可考虑先加 spring-boot-devtools 改善开发体验。

### 2026-07-21（第 6 天）步骤 6 开工
- 环境：装 Homebrew（含 PATH 配置进 .zprofile）→ `brew install postgresql@16` + `brew services start`（后台常驻，5432）→ `createdb movielocations`。
- JPA：pom 加 data-jpa + postgresql 驱动；application.properties 配 datasource（本机默认系统用户名免密）+ `ddl-auto=update` + `show-sql=true`；手敲 `Location` 实体，启动后 Hibernate 自动建表（psql `\d location` 验证：五列 + 主键）；手敲 `LocationRepository` 接口，日志确认 `Found 1 JPA repository interfaces`。
- 概念课：数据库是独立常驻服务器（主机:端口定位，与 8080 同构）、"手动操作一次性 vs 代码可复现"、ORM/JPA/Hibernate/Spring Data 关系、实体映射与无参构造器（record 不可用作实体）、方法名派生查询与运行时动态生成实现类、分层架构（Controller 管 HTTP / Repository 管数据，画了请求旅程图）、构造器注入优于字段注入（已讲，待实操）。
- **下次开工**：Controller 注入 Repository 改真查库 → data.sql 种子数据 → Testcontainers → Docker/CI → 部署。
