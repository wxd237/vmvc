# 虚拟线程MVC框架 (VMVC)

一个基于Java 21虚拟线程的轻量级MVC框架，支持HTTPS和国密算法。

## 特性

- 基于Java 21虚拟线程的高性能并发处理
- 内置HTTPS支持，自动生成证书
- 支持国密算法（通过Bouncy Castle实现）
- 简洁的MVC架构设计
- JSON响应支持
- 可扩展的控制器系统

## 系统要求

- JDK 21或更高版本
- Maven 3.6或更高版本

## 快速开始

### 1. 克隆项目

```bash
git clone [your-repository-url]
cd vmvc
```

### 2. 编译项目

```bash
mvn clean package
```

### 3. 运行服务器

```bash
java -jar target/vmvc-1.0-SNAPSHOT.jar
```

服务器将在8443端口启动，并自动生成HTTPS证书。

### 4. 测试服务

访问 `https://localhost:8443/hello` 来测试服务器。

> 注意：由于使用自签名证书，浏览器可能会显示安全警告，这是正常的。

## 项目结构

```
src/main/java/com/example/vmvc/
├── Main.java                 # 程序入口
├── server/                   # 服务器核心
│   ├── HttpServer.java      # HTTP/HTTPS服务器
│   └── ssl/                 # SSL相关配置
├── mvc/                     # MVC核心组件
│   ├── Controller.java      # 控制器接口
│   ├── DispatcherServlet.java # 请求分发器
│   ├── Request.java        # 请求封装
│   └── Response.java       # 响应封装
└── example/                # 示例代码
    └── HelloController.java # 示例控制器
```

## 开发指南

### 创建新控制器

1. 实现 `Controller` 接口：

```java
public class MyController implements Controller {
    @Override
    public void handle(Request request, Response response) throws IOException {
        // 实现你的业务逻辑
    }
}
```

2. 注册控制器：

```java
dispatcherServlet.registerController("/path", new MyController());
```

### 配置HTTPS

框架会自动处理HTTPS配置，包括：
- 自动生成自签名证书
- 配置TLS
- 启用加密通信

## 安全说明

- 默认使用TLS 1.3协议
- 使用PKCS12格式存储证书
- 支持国密算法套件
- 生产环境建议使用正式的SSL证书

## 性能优化

框架使用Java 21的虚拟线程特性，可以高效处理大量并发连接：
- 每个请求使用独立的虚拟线程
- 低内存占用
- 高并发性能

## 贡献指南

欢迎提交Pull Request或Issue。在提交代码前，请确保：
1. 代码符合项目的编码规范
2. 添加必要的测试用例
3. 更新相关文档

## 许可证

[选择合适的开源许可证] 