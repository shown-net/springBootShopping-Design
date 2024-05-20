package com.example;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@SpringBootTest
class PageDesginerApplicationTests {
    @Test
    public void userProfileTest() throws JsonProcessingException {
        System.out.println(System.getProperty("user.dir")+ File.separator);

    }
    @Test
    public void jwtGenTest() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("name", "SCUT");
        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, "shown")
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis()))
                .compact();
        System.out.println(jwt);
    }



}
