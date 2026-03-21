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
│   ├── admin/               # 管理后台前端
│   │   ├── src/             # 前端源码
│   │   ├── public/          # 静态资源
│   │   ├── dist/            # 构建产物
│   │   ├── package.json     # 前端配置
│   │   ├── vue.config.js    # Vue配置
│   │   ├── .env.development # 开发环境变量
│   │   └── .env.production  # 生产环境变量
│   └── front/               # 用户前端
│       ├── src/             # 前端源码
│       ├── public/          # 静态资源
│       ├── dist/            # 构建产物
│       ├── package.json     # 前端配置
│       └── vue.config.js    # Vue配置
└── DEPLOYMENT.md            # 部署文档
```

## 开发环境部署

### 1. 启动后端服务

```bash
# 进入后端项目目录
cd /Users/young/Desktop/shop/springboot48cmuaub/backend

# 设置 JAVA_HOME 环境变量
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home

# 启动后端服务
mvn spring-boot:run

# 或者使用打包方式启动
mvn clean package -DskipTests
java -jar target/goodmarket-0.0.1-SNAPSHOT.jar
```

**后端服务地址**：`http://localhost:8080/goodmarket`

### 2. 启动前端开发服务器

```bash
# 进入前端项目目录
cd /Users/young/Desktop/shop/springboot48cmuaub/frontend/admin

# 安装依赖（首次运行需要）
npm install

# 启动前端开发服务器
npm run serve
```

**前端服务地址**：`http://localhost:8081`

### 3. 开发环境配置

**前端配置文件**：
- `.env.development`：开发环境变量
- `vue.config.js`：开发服务器配置

**API 请求配置**：
- 开发环境：使用代理 `/goodmarket` → `http://localhost:8080/goodmarket`
- 生产环境：直接请求 `http://localhost:8080/goodmarket`

## 生产环境部署

### 1. 构建前端项目

```bash
# 进入前端项目目录
cd /Users/young/Desktop/shop/springboot48cmuaub/frontend/admin

# 构建生产版本
npm run build

# 构建产物生成在 dist/ 目录
```

### 2. 部署前端静态资源

**方案一：Nginx 部署（推荐）**

```nginx
server {
    listen 80;
    server_name your-domain.com;

    # 前端静态资源
    location / {
        root /path/to/frontend/admin/dist;
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

    # 前端静态资源
    DocumentRoot /path/to/frontend/admin/dist

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
cd /Users/young/Desktop/shop/springboot48cmuaub/backend

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
- Node.js 14+
- npm 6+

## 访问地址

### 开发环境
- **管理后台前端**：http://localhost:8081
- **用户前端**：http://localhost:8083
- **后端 API**：http://localhost:8080/goodmarket

### 生产环境
- **前端**：http://your-domain.com
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

### 2. 前端构建失败

**常见原因**：
- Node.js 版本不兼容
- 依赖安装失败
- 构建配置错误

**解决方案**：
```bash
# 清除缓存重新安装
rm -rf node_modules package-lock.json
npm install

# 使用兼容的 Node.js 版本
nvm install 14
nvm use 14
npm install
```

### 3. 后端启动失败

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

## 总结

本项目已成功改造为前后端分离架构，具备以下特点：

✅ **项目命名**：好物集市（GoodMarket）
✅ **代码分离**：前后端代码完全独立
✅ **独立部署**：前后端可以独立部署和扩展
✅ **开发效率**：支持并行开发和测试
✅ **性能优化**：前端静态资源可以独立优化
✅ **灵活扩展**：支持多端适配和微服务架构

按照本文档的部署指南，可以快速搭建开发环境和生产环境。