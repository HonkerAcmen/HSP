package com.HongHua.HSP.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String KEY = "MJKWDJAWDKSKNAWDNJJJDAWDKawdmdakwm@$__2024__10__18__20__50__50";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;

        // 从 Cookie 中获取 token
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        // 验证 token 的路径和格式
        String path = request.getServletPath();
        if ("/auth/register".equals(path) || "/auth/login".equals(path) || "/h2-console".equals(path) || "/error".equals(path)) {
            filterChain.doFilter(request, response); // 不过滤
            return;
        }

        if (jwt != null) {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(KEY)
                        .parseClaimsJws(jwt)
                        .getBody();
                Date jwtDate = claims.getExpiration();
                if (jwtDate.before(new Date())) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "令牌超时");
                    return;
                }

                String userEmail = (String) claims.get("userEmail");
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userEmail, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (ExpiredJwtException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "令牌超时");
                return;
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "令牌无效");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未找到 token");
            return;
        }
        filterChain.doFilter(request, response);
    }

}
