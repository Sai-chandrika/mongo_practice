package com.example.mongo.mongo_practice.security;
import com.example.mongo.mongo_practice.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author chandrika
 * user
 * @ProjectName otp-sending-users
 * @since 29-09-2023
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityWebConfiguration {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private UserRepo userRepo;
    @Value("${appUser.login.type}")
    private String loginTypeValue;
    @Value("${login.expiration.time.in.minutes}")
    private Integer tokenExpirationTime;

    private final String[] PUBLIC_RESOURCE_AND_URL = {"/",
            "/user/sign-up",
            "/user/login",
            "/file/upload",
            "/file/upload/file",
            "/file/uploadVideo",
            "/file/view/{fileId}",
            "/file/{videoId}",
            "/file/video/upload",
            "/file/play/{video}",
            "/file/videos/{videoId}",
            "/file/view/{video}",
            "/file/upload/image",
            "/file/image/{imageId}",
            "/file/download/{fileId}",
            "/file/download-file/{id}",
            "/restaurant/add-restaurant",
            "/restaurant/get-all-restaurants",
            "/menu/menu-add",
            "/order/order-save",
            "/menu/get-all-menu/{restaurantId}",
            "/review/review-save",
            "/v3/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/v2/api-docs/**",
            "/api-docs/**",
    };

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(a->a.anyRequest().authenticated())
                .exceptionHandling(e->e.accessDeniedHandler(accessDeniedHandler()))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtils, userRepo, loginTypeValue, tokenExpirationTime), BasicAuthenticationFilter.class)   //token method
                .addFilterBefore(new CustomCORSFilter(), ChannelProcessingFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.debug(true)
                .ignoring()
                .requestMatchers(PUBLIC_RESOURCE_AND_URL);
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

}
