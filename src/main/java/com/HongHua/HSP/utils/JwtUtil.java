package com.HongHua.HSP.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final String KEY = "MJKWDJAWDKSKNAWDNJJJDAWDKawdmdakwm@$__2024__10__18__20__50__50";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

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
        }catch (SignatureException e){
            return null;
        }
        catch (MalformedJwtException e){
            return null;
        }
    }


}
