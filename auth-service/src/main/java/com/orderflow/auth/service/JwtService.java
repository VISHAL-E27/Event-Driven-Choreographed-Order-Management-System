package com.orderflow.auth.service;

import com.orderflow.auth.util.RsaKeyUtils;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.private-key:GENERATE}")
    private String privateKeyStr;

    @Value("${jwt.expiration-ms:3600000}")
    private long expirationMs;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        if ("GENERATE".equals(privateKeyStr) || privateKeyStr.isBlank()) {
            KeyPair keyPair = RsaKeyUtils.generateRsaKeyPair();
            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
        } else {
            this.privateKey = RsaKeyUtils.getPrivateKeyFromBase64(privateKeyStr);
        }
    }

    public String generateToken(String username, String email, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(username)
                .claim("email", email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public String getPublicKeyBase64() {
        if (publicKey != null) {
            return Base64.getEncoder().encodeToString(publicKey.getEncoded());
        }
        return null;
    }
}
