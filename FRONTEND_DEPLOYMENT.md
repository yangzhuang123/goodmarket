# 好物集市 - 用户前端部署文档

## 项目概述

好物集市用户前端是一个基于 Vue 2.6.11 的单页应用（SPA），提供商品浏览、购物车、订单管理、用户中心等功能。

- **技术栈**: Vue 2.6.11 + Vue Router + Vuex + Element UI
- **构建工具**: Vue CLI 4.5.0
- **后端API**: Spring Boot (端口: 8080)
- **前端端口**: 8082

## 环境要求

### 必需环境
- **Node.js**: >= 12.0.0 (推荐 v14.x 或 v16.x)
- **npm**: >= 6.0.0
- **Python**: >= 3.6 (用于本地测试服务器)

### 可选环境
- **Nginx**: >= 1.18 (生产环境推荐)
- **Apache**: >= 2.4 (生产环境可选)

## 项目结构

```
frontend/front/
├── public/                 # 静态资源
│   ├── css/              # 第三方CSS库
│   ├── js/               # 第三方JS库
│   └── verifys/          # 验证码相关文件
├── src/                   # 源代码
│   ├── assets/           # 项目资源（图片、CSS、JS）
│   ├── common/           # 公共工具函数
│   ├── components/       # 公共组件
│   ├── config/           # 配置文件
│   ├── mixins/           # Vue混入
│   ├── pages/            # 页面组件
│   ├── router/           # 路由配置
│   ├── store/            # Vuex状态管理
│   ├── App.vue           # 根组件
│   └── main.js           # 入口文件
├── dist/                  # 构建输出目录（生产环境）
├── package.json            # 项目依赖配置
├── vue.config.js          # Vue CLI配置
└── babel.config.js        # Babel配置
```

## 部署步骤

### 1. 安装依赖

```bash
cd frontend/front
npm install
```

### 2. 配置后端API地址

编辑 `src/config/config.js` 文件，修改后端API地址：

```javascript
export default {
    baseUrl: 'http://your-backend-server:8080/goodmarket/',
    name: '/goodmarket',
    // ... 其他配置
}
```

**说明：**
- `baseUrl`: 后端API的完整地址（生产环境必须配置）
- `name`: API路径前缀，保持为 `/goodmarket`

### 3. 构建生产版本

```bash
npm run build
```

构建完成后，`dist` 目录包含所有生产环境所需的文件。

### 4. 部署到服务器

#### 方式一：使用Nginx（推荐）

1. 将 `dist` 目录上传到服务器
2. 配置Nginx：

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    # 前端静态文件
    location / {
        root /path/to/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
    
    # 后端API代理
    location /goodmarket/ {
        proxy_pass http://backend-server:8080/goodmarket/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

3. 重启Nginx：
```bash
nginx -t              # 测试配置
nginx -s reload       # 重载配置
```

#### 方式二：使用Apache

1. 启用 `mod_rewrite` 模块
2. 配置 `.htaccess` 文件：

```apache
RewriteEngine On
RewriteBase /
RewriteRule ^index\.html$ - [L]
RewriteCond %{REQUEST_FILENAME} !-f
RewriteCond %{REQUEST_FILENAME} !-d
RewriteRule . /index.html [L]
```

3. 配置虚拟主机：

```apache
<VirtualHost *:80>
    ServerName your-domain.com
    DocumentRoot /path/to/dist
    
    <Directory /path/to/dist>
        Options Indexes FollowSymLinks
        AllowOverride All
        Require all granted
    </Directory>
    
    # 后端API代理（需要启用mod_proxy）
    ProxyPass /goodmarket/ http://backend-server:8080/goodmarket/
    ProxyPassReverse /goodmarket/ http://backend-server:8080/goodmarket/
</VirtualHost>
```

#### 方式三：使用Node.js服务器

1. 安装 `serve` 包：
```bash
npm install -g serve
```

2. 启动服务器：
```bash
cd dist
serve -s . -l 8082
```

#### 方式四：使用Python简单HTTP服务器（仅用于测试）

```bash
cd dist
python3 -m http.server 8082
```

**注意：** 此方式仅用于开发测试，不推荐用于生产环境。

### 5. 配置HTTPS（可选但推荐）

#### 使用Let's Encrypt免费证书

```bash
# 安装certbot
sudo apt-get install certbot python3-certbot-nginx

# 获取证书
sudo certbot --nginx -d your-domain.com

# 自动续期
sudo certbot renew --dry-run
```

#### Nginx HTTPS配置示例

```nginx
server {
    listen 443 ssl http2;
    server_name your-domain.com;
    
    ssl_certificate /etc/letsencrypt/live/your-domain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/your-domain.com/privkey.pem;
    
    # 前端静态文件
    location / {
        root /path/to/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
    
    # 后端API代理
    location /goodmarket/ {
        proxy_pass http://backend-server:8080/goodmarket/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}

# HTTP重定向到HTTPS
server {
    listen 80;
    server_name your-domain.com;
    return 301 https://$server_name$request_uri;
}
```

## 配置说明

### Vue CLI配置 (vue.config.js)

```javascript
module.exports = {
    // 生产环境公共路径
    publicPath: process.env.NODE_ENV === 'production' ? '././' : '/',
    
    // 开发服务器配置
    devServer: {
        host: "0.0.0.0",
        port: 8082,
        proxy: {
            '/goodmarket': {
                target: 'http://localhost:8080',
                changeOrigin: true,
                secure: false
            }
        }
    }
}
```

### API请求配置 (src/main.js)

```javascript
// 生产环境使用完整URL，开发环境使用相对路径
Vue.http.options.root = process.env.NODE_ENV === 'production' 
    ? config.baseUrl 
    : config.name;
```

### 路由配置 (src/router/router.js)

```javascript
export default new VueRouter({
    routes: [
        {
            path: '/',
            redirect: '/index/home'
        },
        {
            path: '/index',
            component: Index,
            children: [
                // 子路由...
            ]
        },
        {
            path: '/login',
            component: Login
        },
        {
            path: '/register',
            component: Register
        }
    ]
})
```

## 常见问题

### 1. 构建失败

**问题：** `npm run build` 报错

**解决方案：**
```bash
# 清除缓存
rm -rf node_modules package-lock.json
npm install

# 重新构建
npm run build
```

### 2. API请求404错误

**问题：** 前端无法访问后端API

**解决方案：**
- 检查 `src/config/config.js` 中的 `baseUrl` 是否正确
- 确认后端服务是否正常运行
- 检查防火墙是否阻止了8080端口
- 确认Nginx/Apache的代理配置是否正确

### 3. 页面刷新404

**问题：** 刷新页面时出现404错误

**解决方案：**
- Nginx：确保配置了 `try_files $uri $uri/ /index.html;`
- Apache：确保配置了正确的 `mod_rewrite` 规则

### 4. 图片无法加载

**问题：** 页面中的图片无法显示

**解决方案：**
- 检查图片路径是否正确
- 确认 `baseUrl` 配置是否包含正确的协议（http/https）
- 检查后端文件上传接口是否正常工作

### 5. 登录后刷新页面丢失登录状态

**问题：** 刷新页面后需要重新登录

**解决方案：**
- 检查浏览器是否允许使用localStorage
- 确认后端Token验证逻辑是否正确
- 检查 `src/main.js` 中的Token设置代码

## 性能优化

### 1. 开启Gzip压缩

**Nginx配置：**
```nginx
gzip on;
gzip_vary on;
gzip_min_length 1024;
gzip_types text/plain text/css text/xml text/javascript application/x-javascript application/xml+rss application/json application/javascript;
```

### 2. 启用浏览器缓存

**Nginx配置：**
```nginx
location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2)$ {
    expires 1y;
    add_header Cache-Control "public, immutable";
}
```

### 3. CDN加速

将静态资源（图片、CSS、JS）上传到CDN，修改 `vue.config.js`：

```javascript
module.exports = {
    publicPath: 'https://cdn.your-domain.com/'
}
```

## 监控与日志

### 1. 访问日志

**Nginx：**
```nginx
access_log /var/log/nginx/frontend-access.log;
error_log /var/log/nginx/frontend-error.log;
```

### 2. 错误监控

集成Sentry等错误监控工具：

```javascript
import * as Sentry from "@sentry/vue";

Sentry.init({
    Vue: Vue,
    dsn: "your-sentry-dsn",
    environment: process.env.NODE_ENV
});
```

## 安全建议

1. **启用HTTPS**：使用SSL证书保护数据传输
2. **配置CORS**：在后端正确配置跨域策略
3. **定期更新依赖**：运行 `npm audit` 检查安全漏洞
4. **设置安全头**：在Nginx中添加安全响应头
5. **限制文件上传**：配置后端文件上传大小限制

## 备份与恢复

### 备份

```bash
# 备份构建文件
tar -czf frontend-backup-$(date +%Y%m%d).tar.gz dist/

# 备份配置文件
cp src/config/config.js config-backup-$(date +%Y%m%d).js
```

### 恢复

```bash
# 恢复构建文件
tar -xzf frontend-backup-YYYYMMDD.tar.gz

# 恢复配置文件
cp config-backup-YYYYMMDD.js src/config/config.js
```

## 维护说明

### 定期任务

1. **每日**：检查服务器日志，监控系统性能
2. **每周**：运行 `npm audit` 检查依赖安全
3. **每月**：更新依赖包，重新构建并测试
4. **每季度**：审查安全配置，更新SSL证书

### 更新部署流程

1. 在测试环境验证新版本
2. 备份当前生产版本
3. 部署新版本
4. 运行冒烟测试
5. 监控错误日志
6. 如有问题，快速回滚到备份版本

## 联系支持

如有问题，请联系技术支持团队：
- 邮箱：support@goodmarket.com
- 电话：400-XXX-XXXX

## 附录

### 快速部署命令

```bash
# 完整部署流程
cd frontend/front
npm install
npm run build
scp -r dist/* user@server:/var/www/frontend/
ssh user@server "nginx -s reload"
```

### Docker部署（可选）

```dockerfile
# Dockerfile
FROM node:14-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build-stage /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

```bash
# 构建并运行Docker镜像
docker build -t goodmarket-frontend .
docker run -d -p 8082:80 --name frontend goodmarket-frontend
```

---

**文档版本：** 1.0.0  
**最后更新：** 2026-03-22  
**维护人员：** 技术团队
