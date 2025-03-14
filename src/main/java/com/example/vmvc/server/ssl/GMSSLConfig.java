package com.example.vmvc.server.ssl;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class GMSSLConfig {
    private static final Logger logger = LoggerFactory.getLogger(GMSSLConfig.class);
    private static final String BC_PROVIDER = "BC";

    static {
        // 注册Bouncy Castle提供者
        Security.addProvider(new BouncyCastleProvider());
    }

    private final String keystorePath;
    private final String keystorePassword;
    private final String keyPassword;

    public GMSSLConfig(String keystorePath, String keystorePassword, String keyPassword) {
        this.keystorePath = keystorePath;
        this.keystorePassword = keystorePassword;
        this.keyPassword = keyPassword;
    }

    public SSLServerSocketFactory createSSLServerSocketFactory() throws Exception {
        // 加载密钥库
        KeyStore keyStore = loadKeyStore();
        
        // 初始化密钥管理器
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, keyPassword.toCharArray());

        // 初始化信任管理器
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        // 创建并配置SSL上下文
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

        return sslContext.getServerSocketFactory();
    }

    private KeyStore loadKeyStore() throws KeyStoreException, IOException, CertificateException, NoSuchProviderException, NoSuchAlgorithmException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12", BC_PROVIDER);
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            keyStore.load(fis, keystorePassword.toCharArray());
        }
        return keyStore;
    }

    public static class Builder {
        private String keystorePath;
        private String keystorePassword;
        private String keyPassword;

        public Builder keystorePath(String path) {
            this.keystorePath = path;
            return this;
        }

        public Builder keystorePassword(String password) {
            this.keystorePassword = password;
            return this;
        }

        public Builder keyPassword(String password) {
            this.keyPassword = password;
            return this;
        }

        public GMSSLConfig build() {
            return new GMSSLConfig(keystorePath, keystorePassword, keyPassword);
        }
    }
} 