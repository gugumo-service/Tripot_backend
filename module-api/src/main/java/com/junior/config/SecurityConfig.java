package com.junior.config;

import com.junior.security.JwtUtil;
import com.junior.security.filter.JWTFilter;
import com.junior.security.oauth2.CustomSuccessHandler;
import com.junior.service.CustomOAuth2UserService;
import com.junior.service.UserDetailsServiceImpl;
import com.junior.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOauth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                //oauth2
                .oauth2Login((oauth2) -> oauth2.userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOauth2UserService))
                        .successHandler(customSuccessHandler)
                        .loginProcessingUrl("api/v1/login/oauth2/**")
                )


                .sessionManagement((session) -> session.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //filter
                .addFilterAfter(new JWTFilter(jwtUtil, userDetailsService), OAuth2LoginAuthenticationFilter.class)

                //uri 권한 설정
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/v1/login/**").permitAll()
                        //닉네임 중복 여부 확인
                        .requestMatchers("/api/v1/members/nicknames/check-valid").permitAll()
                        .anyRequest().authenticated());


        return httpSecurity.build();

    }


}
