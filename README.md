# 智码工坊 (Smart Code Workshop)

一个基于AI的智能代码生成平台，如同传统工匠精心雕琢每件作品，让每一行代码都经过智能优化。支持多种编程语言和框架的代码自动生成，将创意想法转化为高质量的代码实现。

## 🚀 项目简介

智码工坊是一个全栈应用，旨在通过人工智能技术帮助开发者快速生成高质量的代码。如同传统工坊中经验丰富的师傅，它能够深度理解开发需求，并精心制作出符合最佳实践的代码作品。项目采用前后端分离架构，后端使用Spring Boot，前端使用Vue.js + TypeScript。

## 📋 功能特性

- 🤖 智能代码生成：基于LangChain4j的AI代码生成能力
- 🎨 现代化UI：基于Vue 3 + Ant Design Vue的响应式前端界面
- 🔧 RESTful API：完整的后端API服务，支持Knife4j文档
- 📊 代码模板管理：可自定义代码生成模板
- 🔍 实时预览：生成代码的实时预览功能
- 💾 项目管理：支持项目的创建、保存和管理
- 🔐 权限认证：基于Sa-Token的用户认证和权限管理
- 💬 聊天历史：支持对话记忆和历史记录
- 📸 网页截图：集成Selenium实现网页截图功能
- ☁️ 云存储：支持腾讯云COS对象存储
- 📈 监控指标：集成Prometheus监控和Actuator健康检查

## 🏗️ 技术栈

### 后端技术
- **Java 21** - 编程语言
- **Spring Boot 3.5.0** - 主框架
- **Spring Web** - Web服务
- **MyBatis-Flex** - 数据持久层ORM框架
- **Sa-Token** - 权限认证框架
- **LangChain4j** - AI集成框架
- **Redis** - 缓存和会话存储
- **MySQL** - 关系型数据库
- **Knife4j** - API文档生成
- **Hutool** - Java工具库
- **Caffeine** - 本地缓存
- **JetCache** - 多级缓存框架
- **Selenium** - 网页自动化和截图
- **腾讯云COS** - 对象存储服务
- **Prometheus** - 监控指标
- **Maven** - 项目管理和构建

### 前端技术
- **Vue.js 3** - 前端框架
- **TypeScript** - 类型安全的JavaScript
- **Vue Router** - 路由管理
- **Pinia** - 状态管理
- **Ant Design Vue** - UI组件库
- **Axios** - HTTP客户端
- **Markdown-it** - Markdown渲染
- **Highlight.js** - 代码高亮
- **Vite** - 构建工具

## 📁 项目结构

```
smart-code-workshop/
├── src/                           # 后端源码
│   ├── main/
│   │   ├── java/                  # Java源码
│   │   │   └── com/haitao/generator/
│   │   │       ├── controller/    # 控制器层
│   │   │       ├── service/       # 服务层
│   │   │       ├── core/          # 核心业务逻辑
│   │   │       ├── exception/     # 异常处理
│   │   │       └── mapper/        # 数据访问层
│   │   └── resources/             # 配置文件和资源
│   └── test/                      # 测试代码
├── ai-code-generator-frontend/    # 前端项目
│   ├── public/                    # 静态资源
│   ├── src/
│   │   ├── api/                   # API接口
│   │   ├── assets/                # 静态资源
│   │   ├── components/            # Vue组件
│   │   ├── config/                # 配置文件
│   │   ├── layouts/               # 布局组件
│   │   ├── pages/                 # 页面组件
│   │   ├── router/                # 路由配置
│   │   ├── stores/                # 状态管理
│   │   └── utils/                 # 工具函数
│   ├── package.json               # 前端依赖配置
│   └── vite.config.ts             # Vite配置
├── lib/                           # 本地依赖库
├── target/                        # Maven构建输出
├── pom.xml                        # Maven配置文件
└── README.md                      # 项目说明文档
```

## 🚀 快速开始

### 环境要求

- **Java**: JDK 21 或更高版本
- **Node.js**: 18.0 或更高版本
- **Maven**: 3.8 或更高版本
- **MySQL**: 8.0+
- **Redis**: 6.0+

### 后端启动

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd smart-code-workshop
   ```

2. **配置数据库和Redis**
   ```bash
   # 修改 src/main/resources/application.yml
   # 配置数据库连接信息、Redis连接、AI模型配置等
   ```

3. **安装依赖并启动**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   后端服务将在 `http://localhost:8080` 启动

4. **访问API文档**
   ```
   http://localhost:8080/doc.html
   ```

### 前端启动

1. **进入前端目录**
   ```bash
   cd ai-code-generator-frontend
   ```

2. **安装依赖**
   ```bash
   npm install
   # 或使用 yarn
   yarn install
   ```

3. **启动开发服务器**
   ```bash
   npm run dev
   # 或使用 yarn
   yarn dev
   ```

   前端应用将在 `http://localhost:5173` 启动

## 🔧 开发指南

### 后端开发

- **API文档**: 启动后端服务后访问 `http://localhost:8080/doc.html`
- **数据库**: 使用MyBatis-Flex进行数据持久化
- **配置文件**: `src/main/resources/application.yml`
- **AI集成**: 基于LangChain4j框架集成各种AI模型
- **权限管理**: 使用Sa-Token进行用户认证和权限控制

### 前端开发

- **组件开发**: 在 `src/components/` 目录下创建可复用组件
- **页面开发**: 在 `src/pages/` 目录下创建页面组件
- **API调用**: 使用 `src/api/` 目录下的接口服务
- **状态管理**: 使用Pinia进行全局状态管理
- **类型安全**: 使用TypeScript确保类型安全

### 构建部署

#### 后端构建
```bash
mvn clean package
java -jar target/ai-code-generater-*.jar
```

#### 前端构建
```bash
cd ai-code-generator-frontend
npm run build
```

## 📊 监控和运维

### 健康检查
```bash
# 应用健康状态
curl http://localhost:8080/actuator/health

# Prometheus指标
curl http://localhost:8080/actuator/prometheus
```

### 缓存管理
- **本地缓存**: Caffeine
- **分布式缓存**: Redis + JetCache
- **热点数据**: JD HotKey

## 🔑 核心功能模块

### 1. 用户管理 (UserController)
- 用户注册、登录、权限管理

### 2. 应用管理 (AppController)  
- 代码生成应用的创建和管理

### 3. 聊天历史 (ChatHistoryController)
- AI对话记录的存储和检索

### 4. 静态资源 (StaticResourceController)
- 文件上传、下载和管理

### 5. AI代码生成核心 (AiCodeGeneratorFacade)
- 基于LangChain4j的智能代码生成

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📝 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 [Issue](../../issues)
- 发送邮件至: [your-email@example.com]

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者！

---

**注意**: 请确保在生产环境中正确配置所有必要的环境变量和安全设置。