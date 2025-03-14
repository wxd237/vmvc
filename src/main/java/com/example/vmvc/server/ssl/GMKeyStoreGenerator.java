package com.example.vmvc.server.ssl;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

public class GMKeyStoreGenerator {
    private static final String BC_PROVIDER = "BC";
    
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void generateKeyStore(String keystorePath, String keystorePassword, String keyPassword) {
        try {
            // 如果密钥库已存在，直接返回
            File keystoreFile = new File(keystorePath);
            if (keystoreFile.exists()) {
                return;
            }

            // 生成SM2密钥对
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", BC_PROVIDER);
            keyPairGenerator.initialize(256);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // 生成证书
            X509Certificate certificate = generateCertificate(keyPair);

            // 创建密钥库
            KeyStore keyStore = KeyStore.getInstance("PKCS12", BC_PROVIDER);
            keyStore.load(null, null);

            // 将私钥和证书存入密钥库
            keyStore.setKeyEntry("gmserver", keyPair.getPrivate(), keyPassword.toCharArray(),
                    new Certificate[]{certificate});

            // 保存密钥库到文件
            try (FileOutputStream fos = new FileOutputStream(keystorePath)) {
                keyStore.store(fos, keystorePassword.toCharArray());
            }
        } catch (Exception e) {
            throw new RuntimeException("生成密钥库失败", e);
        }
    }

    private static X509Certificate generateCertificate(KeyPair keyPair) throws Exception {
        X500Name issuer = new X500Name("CN=GMServer");
        X500Name subject = issuer;
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        Date notBefore = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        Date notAfter = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000L);

        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(
                keyPair.getPublic().getEncoded());

        X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(
                issuer, serial, notBefore, notAfter, subject, publicKeyInfo);

        ContentSigner signer = new JcaContentSignerBuilder("SM3withSM2")
                .setProvider(BC_PROVIDER)
                .build(keyPair.getPrivate());

        return new JcaX509CertificateConverter()
                .setProvider(BC_PROVIDER)
                .getCertificate(certBuilder.build(signer));
    }

    public static void main(String[] args) {
        // 生成测试证书和密钥库
        generateKeyStore("keystore.p12", "123456", "123456");
    }
} 