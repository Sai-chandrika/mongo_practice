package com.example.mongo.mongo_practice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author chandrika
 * user
 * @ProjectName otp-sending-users
 * @since 29-09-2023
 */
public class CustomCORSFilter extends OncePerRequestFilter {
    CustomCORSFilter() {

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");  //allow any domain
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");  //allow methods
        response.setHeader("Access-Control-Max-Age", "3600");  //time in ms
        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");  //headers as key
        response.addHeader("Access-Control-Expose-Headers", "xsrf-token");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
