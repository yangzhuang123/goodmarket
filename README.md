# GoodMarket 好物集市

## 项目简介

GoodMarket（好物集市）是一个完整的电商平台项目，采用前后端分离架构设计。该项目旨在为用户提供商品浏览、购买、支付等一站式购物体验，同时为管理员提供商品管理、订单处理、用户管理等后台功能。

### 主要功能
- 用户注册与登录
- 商品浏览与搜索
- 购物车管理
- 订单创建与管理
- 地址管理
- 优惠券管理
- 实时聊天功能
- 新闻资讯管理
- 后台管理系统

## 技术栈

### 后端技术
- Spring Boot 2.2.2.RELEASE
- MyBatis 2.1.1 + MyBatis-Plus 2.3
- MySQL
- Shiro 1.3.2（权限管理）
- Spring WebSocket（实时通信）
- 百度AI SDK 4.4.1（人工智能服务）

### 前端技术
- Vue 2.6.10
- Element UI 2.13.0
- Vue Router 3.1.5
- Vuex 3.2.0
- Axios 0.19.2
- ECharts 5.4.1
- Swiper 5.2.0

## 项目结构

```
goodmarket/
├── backend/             # 后端Spring Boot项目
│   ├── src/             # 源代码目录
│   │   ├── main/java/com/
│   │   │   ├── annotation/     # 自定义注解
│   │   │   ├── config/         # 配置类
│   │   │   ├── controller/     # 控制器
│   │   │   ├── dao/            # 数据访问层
│   │   │   ├── entity/         # 实体类
│   │   │   ├── interceptor/    # 拦截器
│   │   │   ├── service/        # 服务层
│   │   │   ├── utils/          # 工具类
│   │   │   ├── ws/             # WebSocket相关
│   │   │   └── SpringbootSchemaApplication.java  # 应用入口
│   │   └── resources/
│   │       ├── mapper/          # MyBatis映射文件
│   │       └── application.yml  # 应用配置
│   ├── db/              # 数据库脚本
│   └── pom.xml          # Maven依赖配置
├── frontend/            # 前端Vue项目
│   ├── admin/           # 后台管理系统
│       ├── public/      # 静态资源
│       ├── src/         # 源代码
│       │   ├── assets/  # 资源文件
│       │   ├── components/  # 组件
│       │   ├── icons/   # 图标
│       │   ├── mixins/  # 混入
│       │   ├── router/  # 路由
│       │   ├── store/   # Vuex状态管理
│       │   ├── utils/   # 工具类
│       │   ├── views/   # 页面
│       │   ├── App.vue  # 根组件
│       │   └── main.js  # 入口文件
│       └── package.json # 前端依赖配置
└── README.md            # 项目说明文档
```

## 环境要求

### 后端环境
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+

### 前端环境
- Node.js 10.0+
- npm 6.0+

## 安装与运行

### 1. 后端部署

1. **初始化数据库**
   - 打开MySQL，创建数据库 `goodmarket`
   - 执行 `backend/db/goodmarket.sql` 脚本初始化表结构

2. **配置数据库连接**
   - 修改 `backend/src/main/resources/application.yml` 中的数据库连接信息：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/goodmarket?useUnicode=true&characterEncoding=utf-8&useSSL=false
       username: root
       password: 123456
   ```

3. **构建项目**
   ```bash
   cd backend
   mvn clean package
   ```

4. **运行项目**
   ```bash
   java -jar target/goodmarket-0.0.1-SNAPSHOT.jar
   ```
   后端服务默认运行在 `http://localhost:8080`

### 2. 前端部署

1. **安装依赖**
   ```bash
   cd frontend/admin
   npm install
   ```

2. **开发环境运行**
   ```bash
   npm run serve
   ```
   前端开发服务器默认运行在 `http://localhost:8081`

3. **生产环境构建**
   ```bash
   npm run build
   ```
   构建产物将生成在 `frontend/admin/dist` 目录，可部署到Web服务器

## 核心功能模块

### 1. 用户管理
- 用户注册与登录
- 用户信息修改
- 密码重置
- 管理员用户管理

### 2. 商品管理
- 商品信息CRUD操作
- 商品分类管理
- 品牌管理
- 商家管理

### 3. 购物车管理
- 添加商品到购物车
- 修改购物车商品数量
- 删除购物车商品
- 查看购物车商品列表

### 4. 订单管理
- 创建订单
- 订单状态管理
- 订单查询
- 订单统计

### 5. 地址管理
- 添加收货地址
- 修改收货地址
- 删除收货地址
- 设置默认地址

### 6. 优惠券管理
- 优惠券创建与管理
- 优惠券发放
- 优惠券使用
- 优惠券查询

### 7. 聊天模块
- 实时消息发送与接收
- 聊天记录管理
- 客服在线状态管理

### 8. 新闻模块
- 新闻发布与管理
- 新闻分类
- 新闻查询
- 新闻展示

## 接口文档

后端API接口遵循RESTful设计风格，主要接口包括：

- **用户相关**：`/yonghu/*`、`/users/*`
- **商品相关**：`/shangpinxinxi/*`、`/shangpinfenlei/*`、`/pinpai/*`
- **购物车相关**：`/cart/*`
- **订单相关**：`/orders/*`
- **地址相关**：`/address/*`
- **优惠券相关**：`/coupon/*`、`/mycoupon/*`
- **聊天相关**：`/chat/*`、`/chathelper/*`
- **新闻相关**：`/news/*`、`/newstype/*`

## 注意事项

1. **数据库配置**：确保MySQL服务已启动，且数据库连接信息正确
2. **端口冲突**：默认后端使用8080端口，前端开发服务器使用8081端口，如需修改请调整配置
3. **依赖安装**：前端依赖安装可能需要较长时间，请耐心等待
4. **权限管理**：管理员账号需要在数据库中手动创建，默认管理员账号密码可参考数据库脚本

## 项目维护

- **日志管理**：后端使用Spring Boot默认日志配置，可在`application.yml`中调整
- **性能优化**：可通过缓存、SQL优化等方式提升系统性能
- **安全措施**：建议在生产环境中配置HTTPS，加强密码加密等安全措施

## 联系方式

如有问题或建议，欢迎联系项目维护者。

---

**GoodMarket - 好物集市，让购物更简单！**