package com.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {
    private static final String signKey = "scutWeb";
    private static final Long expire = 1000 * 60 * 60 * 24 * 2L; //24 * 2个小时的过期时间

    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
    }

    public static Map<String, Object> parseJWT(String jwt) {
        return Jwts.parser()
                //设置指定密钥
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
