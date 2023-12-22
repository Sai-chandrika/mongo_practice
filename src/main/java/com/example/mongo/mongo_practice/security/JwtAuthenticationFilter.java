package com.example.mongo.mongo_practice.security;
import com.example.mongo.mongo_practice.dto.ApiResponse;
import com.example.mongo.mongo_practice.entity.UserData;
import com.example.mongo.mongo_practice.exception.InvalidDataException;
import com.example.mongo.mongo_practice.repo.UserRepo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author chandrika
 * user
 * @ProjectName otp-sending-users
 * @since 29-09-2023
 */

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtil;
    private final UserRepo userRepo;
    private final String loginTypeValue;
    private final Integer tokenExpirationTime;
    private final Integer thresholdExpirationTime = 2;

    JwtAuthenticationFilter(JwtTokenUtils jwtTokenUtil, UserRepo userRepo, String loginTypeValue, Integer tokenExpirationTime) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepo = userRepo;
        this.loginTypeValue = loginTypeValue;
        this.tokenExpirationTime = tokenExpirationTime;
    }

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filter)
            throws IOException {
        try {
            String authToken = extractAuthToken(req.getHeader("Authorization"));
            String userName = jwtTokenUtil.parseToken(authToken);
            UserData user = validateUserNameType(userName);

            if (user == null) generateUnauthorisedAccess(res);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, Arrays.asList(
                    new SimpleGrantedAuthority(user.getRole().name()))); //match the role with preauthorize in api-controller
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            LocalDateTime tokenTime = DynamicTokenStore.tokenCreationTime;
            if (tokenTime.plusMinutes((long) tokenExpirationTime - thresholdExpirationTime).isBefore(LocalDateTime.now())) {
                res.setHeader("Authorization", jwtTokenUtil.getToken(user));
                DynamicTokenStore.tokenCreationTime = tokenTime;
            }
            filter.doFilter(req, res);
        } catch (Exception e) {
            logger.info(e.getMessage());
            generateUnauthorisedAccess(res);
        }
    }

    private String extractAuthToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public void generateUnauthorisedAccess(HttpServletResponse res) throws IOException {

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ApiResponse resp = new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "UNAUTHORISED");
        String jsonRespString = ow.writeValueAsString(resp);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = res.getWriter();
        writer.write(jsonRespString);
        logger.info("===============================");
    }

    private UserData validateUserNameType(String userName) {
        UserData appUser;
        switch (loginTypeValue) {
            case "EMAIL" -> appUser = userRepo.findByEmail(userName);
            case "USERNAME" -> appUser = userRepo.findByName(userName);
            default -> throw new InvalidDataException("invalid data defining at application properties");
        }
        return appUser;
    }
}
