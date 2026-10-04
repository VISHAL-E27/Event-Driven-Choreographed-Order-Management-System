package com.orderflow.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.PublicKey;

@Component
public class JwtUtils {

    @Value("${jwt.public-key:DEFAULT}")
    private String publicKeyStr;

    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        if ("DEFAULT".equals(publicKeyStr) || publicKeyStr.isBlank()) {
            KeyPair keyPair = RsaKeyUtils.generateRsaKeyPair();
            this.publicKey = keyPair.getPublic();
        } else {
            this.publicKey = RsaKeyUtils.getPublicKeyFromBase64(publicKeyStr);
        }
    }

    public void validateToken(final String token) {
        Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token);
    }

    public Claims getClaims(final String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
