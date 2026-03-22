# 好物集市 - 前后端分离部署文档

## 项目架构

本项目已改造为完全的前后端分离架构：

- **项目名称**：好物集市（GoodMarket）
- **后端项目**：Spring Boot 2.2.2.RELEASE + MyBatis Plus
- **前端项目**：Vue.js 2.6.10 + Element UI
- **通信方式**：RESTful API + Axios

## 项目结构

```
goodmarket/
├── backend/                  # 后端项目
│   ├── src/main/java/com/   # 后端代码
│   ├── src/main/resources/  # 后端资源
│   │   ├── mapper/          # MyBatis映射文件
│   │   └── application.yml  # 后端配置
│   ├── db/                  # 数据库脚本
│   └── pom.xml              # Maven 配置
├── frontend/                # 前端项目
│   ├── admin/               # 管理后台前端（商家/管理员使用）
│   │   ├── src/             # 前端源码
│   │   ├── public/          # 静态资源
│   │   ├── dist/            # 构建产物
│   │   ├── package.json     # 前端配置
│   │   ├── vue.config.js    # Vue配置
│   │   ├── .env.development # 开发环境变量
│   │   └── .env.production  # 生产环境变量
│   └── front/               # 用户前端（普通用户使用）
│       ├── src/             # 前端源码
│       ├── public/          # 静态资源
│       ├── dist/            # 构建产物
│       ├── package.json     # 前端配置
│       └── vue.config.js    # Vue配置
└── DEPLOYMENT.md            # 部署文档
```

## 端口分配

| 服务 | 端口 | 说明 | 使用对象 |
|------|------|------|----------|
| 后端 API | 8080 | Spring Boot 后端服务 | 所有前端 |
| 管理后台前端 | 8081 | Vue 开发服务器（如被占用会自动切换） | 商家/管理员 |
| 用户前端 | 8082 | Vue 开发服务器 | 普通用户 |

## 前端项目说明

### 管理后台前端（admin）
- **位置**：`frontend/admin/`
- **端口**：8081
- **使用对象**：商家、管理员
- **主要功能**：
  - 商品管理（添加、编辑、删除商品）
  - 订单管理（查看、处理订单）
  - 用户管理（查看用户信息）
  - 优惠券管理（创建、发放优惠券）
  - 商家管理（审核商家信息）
  - 品牌管理（管理商品品牌）
  - 商品分类管理（管理商品分类）

### 用户前端（front）
- **位置**：`frontend/front/`
- **端口**：8082
- **使用对象**：普通用户
- **主要功能**：
  - 商品浏览（查看商品列表、详情）
  - 购物车管理（添加、删除商品）
  - 订单管理（查看订单状态、订单详情）
  - 个人中心（修改个人信息、收货地址）
  - 优惠券领取（领取、使用优惠券）
  - 商品收藏（收藏喜欢的商品）

## 开发环境部署

### 1. 启动后端服务

```bash
# 进入后端项目目录
cd /Users/young/Desktop/shop/goodmarket/backend

# 设置 JAVA_HOME 环境变量（如需要）
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home

# 启动后端服务
mvn spring-boot:run

# 或者使用打包方式启动
mvn clean package -DskipTests
java -jar target/goodmarket-0.0.1-SNAPSHOT.jar
```

**后端服务地址**：`http://localhost:8080/goodmarket`

### 2. 启动管理后台前端开发服务器

```bash
# 进入管理后台前端项目目录
cd /Users/young/Desktop/shop/goodmarket/frontend/admin

# 安装依赖（首次运行需要）
npm install

# 启动前端开发服务器（Node.js 17+ 需要添加环境变量）
NODE_OPTIONS=--openssl-legacy-provider npm run serve

# 如果使用 Node.js 14-16，可以直接运行
npm run serve
```

**管理后台前端服务地址**：`http://localhost:8081`（端口被占用时会自动切换）

### 3. 启动用户前端开发服务器

```bash
# 进入用户前端项目目录
cd /Users/young/Desktop/shop/goodmarket/frontend/front

# 安装依赖（首次运行需要）
npm install

# 启动前端开发服务器（Node.js 17+ 需要添加环境变量）
NODE_OPTIONS=--openssl-legacy-provider npm run serve

# 如果使用 Node.js 14-16，可以直接运行
npm run serve
```

**用户前端服务地址**：`http://localhost:8082`（端口被占用时会自动切换）

### 4. 开发环境配置

**前端配置文件**：
- `.env.development`：开发环境变量
- `vue.config.js`：开发服务器配置

**API 请求配置**：
- 开发环境：使用代理 `/goodmarket` → `http://localhost:8080/goodmarket`
- 生产环境：直接请求 `http://localhost:8080/goodmarket`

## 生产环境部署

### 1. 构建前端项目

**构建管理后台前端**：

```bash
# 进入管理后台前端项目目录
cd /Users/young/Desktop/shop/goodmarket/frontend/admin

# 构建生产版本（Node.js 17+ 需要添加环境变量）
NODE_OPTIONS=--openssl-legacy-provider npm run build

# 构建产物生成在 dist/ 目录
```

**构建用户前端**：

```bash
# 进入用户前端项目目录
cd /Users/young/Desktop/shop/goodmarket/frontend/front

# 构建生产版本（Node.js 17+ 需要添加环境变量）
NODE_OPTIONS=--openssl-legacy-provider npm run build

# 构建产物生成在 dist/ 目录
```

### 2. 部署前端静态资源

**方案一：Nginx 部署（推荐）**

```nginx
server {
    listen 80;
    server_name your-domain.com;

    # 管理后台前端静态资源
    location /admin {
        root /path/to/frontend;
        index index.html;
        try_files $uri $uri/ /admin/index.html;
    }

    # 用户前端静态资源
    location / {
        root /path/to/frontend/front/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 后端 API 代理
    location /goodmarket/ {
        proxy_pass http://localhost:8080/goodmarket/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

**方案二：Apache 部署**

```apache
<VirtualHost *:80>
    ServerName your-domain.com

    # 用户前端静态资源
    DocumentRoot /path/to/frontend/front/dist

    <Directory /path/to/frontend/front/dist>
        Options Indexes FollowSymLinks
        AllowOverride All
        Require all granted
    </Directory>

    # 管理后台前端静态资源
    Alias /admin /path/to/frontend/admin/dist

    <Directory /path/to/frontend/admin/dist>
        Options Indexes FollowSymLinks
        AllowOverride All
        Require all granted
    </Directory>

    # 后端 API 代理
    ProxyPass /goodmarket/ http://localhost:8080/goodmarket/
    ProxyPassReverse /goodmarket/ http://localhost:8080/goodmarket/
</VirtualHost>
```

### 3. 部署后端服务

```bash
# 进入后端项目目录
cd /Users/young/Desktop/shop/goodmarket/backend

# 打包项目
mvn clean package -DskipTests

# 运行打包后的 jar 文件
java -jar target/goodmarket-0.0.1-SNAPSHOT.jar

# 或者使用 nohup 后台运行
nohup java -jar target/goodmarket-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

### 4. 生产环境配置

**修改前端 API 地址**：

编辑 `frontend/admin/.env.production` 文件：
```
NODE_ENV=production
VUE_APP_API_BASE_URL=http://your-backend-server.com/goodmarket
```

**修改后端配置**：

编辑 `backend/src/main/resources/application.yml` 文件：
```yaml
spring:
    datasource:
        url: jdbc:mysql://your-database-server:3306/goodmarket?useUnicode=true&characterEncoding=utf-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&useSSL=false
        username: your-username
        password: your-password
```

## 环境要求

### 后端环境
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+

### 前端环境
- Node.js 14+（推荐 Node.js 14-16，Node.js 17+ 需要额外配置）

## 访问地址

### 开发环境
- **管理后台前端**：http://localhost:8081（商家/管理员使用）
- **用户前端**：http://localhost:8082（普通用户使用）
- **后端 API**：http://localhost:8080/goodmarket

### 生产环境
- **用户前端**：http://your-domain.com（普通用户访问）
- **管理后台前端**：http://your-domain.com/admin（商家/管理员访问）
- **后端 API**：http://your-backend-server.com/goodmarket

## 常见问题

### 1. 前端无法访问后端 API

**检查项**：
- 后端服务是否正常启动
- API 地址配置是否正确
- CORS 配置是否正确
- 网络连接是否正常

**解决方案**：
- 检查后端日志
- 检查浏览器控制台错误
- 验证 API 地址配置

### 2. 前端构建/启动失败 - Node.js 版本问题

**错误信息**：
```
Error: error:0308010C:digital envelope routines::unsupported
```

**原因**：Node.js 17+ 版本的 OpenSSL 与旧版 webpack 不兼容

**解决方案**：

方法一：使用环境变量（推荐）
```bash
# 启动开发服务器
NODE_OPTIONS=--openssl-legacy-provider npm run serve

# 构建生产版本
NODE_OPTIONS=--openssl-legacy-provider npm run build
```

方法二：降级 Node.js 版本
```bash
# 使用 nvm 切换到兼容版本
nvm install 16
nvm use 16
npm install
npm run serve
```

方法三：修改 package.json（永久解决）
```json
{
  "scripts": {
    "serve": "NODE_OPTIONS=--openssl-legacy-provider vue-cli-service serve",
    "build": "NODE_OPTIONS=--openssl-legacy-provider vue-cli-service build"
  }
}
```

### 3. 前端构建失败 - 依赖问题

**常见原因**：
- 依赖安装失败
- 构建配置错误

**解决方案**：
```bash
# 清除缓存重新安装
rm -rf node_modules package-lock.json
npm install
```

### 4. 后端启动失败

**常见原因**：
- JDK 版本不匹配
- 数据库连接失败
- 端口被占用

**解决方案**：
```bash
# 检查 JDK 版本
java -version

# 检查端口占用
lsof -i :8080

# 检查数据库连接
mysql -h localhost -u root -p
```

### 5. 前端请求 500 错误

**原因**：后端服务未启动或数据库连接失败

**解决方案**：
1. 确认后端服务已在 8080 端口运行
2. 检查数据库是否正常启动
3. 检查数据库连接配置是否正确

## 优势对比

### 前后端分离的优势
1. **开发效率**：前后端独立开发，互不干扰
2. **部署灵活**：前后端可以独立部署和扩展
3. **性能优化**：前端静态资源可以独立优化和缓存
4. **技术选型**：前后端可以使用不同的技术栈
5. **团队协作**：前后端开发人员可以并行工作

### 注意事项
1. **跨域问题**：需要配置 CORS 或使用代理
2. **部署复杂度**：需要维护两套部署环境
3. **调试难度**：前后端分离增加了调试复杂度
4. **版本管理**：需要协调前后端版本兼容性

## 快速启动命令汇总

```bash
# 终端1：启动后端
cd /Users/young/Desktop/shop/goodmarket/backend
mvn spring-boot:run

# 终端2：启动管理后台前端
cd /Users/young/Desktop/shop/goodmarket/frontend/admin
NODE_OPTIONS=--openssl-legacy-provider npm run serve

# 终端3：启动用户前端
cd /Users/young/Desktop/shop/goodmarket/frontend/front
NODE_OPTIONS=--openssl-legacy-provider npm run serve
```

## 总结

本项目已成功改造为前后端分离架构，具备以下特点：

✅ **项目命名**：好物集市（GoodMarket）
✅ **代码分离**：前后端代码完全独立
✅ **独立部署**：前后端可以独立部署和扩展
✅ **开发效率**：支持并行开发和测试
✅ **性能优化**：前端静态资源可以独立优化
✅ **灵活扩展**：支持多端适配和微服务架构

按照本文档的部署指南，可以快速搭建开发环境和生产环境。
