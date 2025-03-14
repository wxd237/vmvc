package com.example.vmvc.server;

import com.example.vmvc.handler.RequestHandler;
import com.example.vmvc.server.ssl.GMSSLConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private final int port;
    private final RequestHandler requestHandler;
    private final GMSSLConfig sslConfig;
    private volatile boolean running = true;

    public HttpServer(int port, RequestHandler requestHandler) {
        this(port, requestHandler, null);
    }

    public HttpServer(int port, RequestHandler requestHandler, GMSSLConfig sslConfig) {
        this.port = port;
        this.requestHandler = requestHandler;
        this.sslConfig = sslConfig;
    }

    public void start() {
        try {
            ServerSocket serverSocket;
            if (sslConfig != null) {
                serverSocket = createSSLServerSocket();
                logger.info("HTTPS服务器启动在端口: {}", port);
            } else {
                serverSocket = new ServerSocket();
                serverSocket.bind(new InetSocketAddress(port));
                logger.info("HTTP服务器启动在端口: {}", port);
            }

            try (serverSocket) {
                // 使用虚拟线程
                try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                    while (running) {
                        Socket clientSocket = serverSocket.accept();
                        executor.submit(() -> handleClient(clientSocket));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("服务器启动失败", e);
        }
    }

    private ServerSocket createSSLServerSocket() throws Exception {
        ServerSocketFactory ssf = sslConfig.createSSLServerSocketFactory();
        SSLServerSocket sslServerSocket = (SSLServerSocket) ssf.createServerSocket(port);
        
        // 配置支持的协议和密码套件
        sslServerSocket.setEnabledProtocols(new String[] {"TLSv1.3"});
        sslServerSocket.setNeedClientAuth(false);
        
        return sslServerSocket;
    }

    private void handleClient(Socket clientSocket) {
        try (clientSocket) {
            requestHandler.handle(clientSocket.getInputStream(), clientSocket.getOutputStream());
        } catch (IOException e) {
            logger.error("处理客户端请求失败", e);
        }
    }

    public void stop() {
        running = false;
    }
} 