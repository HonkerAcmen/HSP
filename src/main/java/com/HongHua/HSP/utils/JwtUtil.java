package com.HongHua.HSP.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final String KEY = "MJKWDJAWDKSKNAWDNJJJDAWDKawdmdakwm@$__2024__10__18__20__50__50";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

//    创建一个JWT Token
    public static String generateToken(String userEmail, Long id){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userEmail", userEmail);
        claims.put("id", id);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, KEY)
                .compact();
    }

//    解析一个JWT Token
    public static Claims validateToken(String token){
        try{
            return Jwts.parser()
                    .setSigningKey(KEY)
                    .parseClaimsJws(token)
                    .getBody();

        }catch (SignatureException | MalformedJwtException e){
            return null;
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("jwt过期");
        }
    }

//    判断一个Token是否过期
    public static Boolean isTokenTimeout(String token){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(token)
                    .parseClaimsJws(token)
                    .getBody();
            Date expirationDate = claims.getExpiration();
            return expirationDate.before(new Date()); // 返回是否过期
        } catch (ExpiredJwtException e) {
            return true; // 捕获过期异常
        } catch (Exception e) {
            // 处理其他解析错误
            throw new RuntimeException("无效的Token");
        }

    }


}
