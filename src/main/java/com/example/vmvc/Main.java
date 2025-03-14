package com.example.vmvc;

import com.example.vmvc.example.HelloController;
import com.example.vmvc.mvc.DispatcherServlet;
import com.example.vmvc.server.HttpServer;
import com.example.vmvc.server.ssl.GMKeyStoreGenerator;
import com.example.vmvc.server.ssl.GMSSLConfig;

public class Main {
    private static final String KEYSTORE_PATH = "gmserver.p12";
    private static final String KEYSTORE_PASSWORD = "123456";
    private static final String KEY_PASSWORD = "123456";

    public static void main(String[] args) {
        // 自动生成证书
        GMKeyStoreGenerator.generateKeyStore(KEYSTORE_PATH, KEYSTORE_PASSWORD, KEY_PASSWORD);
        
        // 创建分发器servlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        
        // 注册控制器
        dispatcherServlet.registerController("/hello", new HelloController());
        
        // 配置HTTPS
        GMSSLConfig sslConfig = new GMSSLConfig.Builder()
            .keystorePath(KEYSTORE_PATH)
            .keystorePassword(KEYSTORE_PASSWORD)
            .keyPassword(KEY_PASSWORD)
            .build();
        
        // 创建并启动HTTPS服务器
        HttpServer server = new HttpServer(8443, dispatcherServlet, sslConfig);
        server.start();
    }
} 