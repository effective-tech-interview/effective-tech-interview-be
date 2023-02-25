package com.sparcs.teamf.api.auth.config;

import com.sparcs.teamf.api.auth.jwt.JwtAccessDeniedHandler;
import com.sparcs.teamf.api.auth.jwt.JwtAuthenticationEntryPoint;
import com.sparcs.teamf.api.auth.jwt.JwtFilter;
import com.sparcs.teamf.api.auth.jwt.TokenProvider;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().mvcMatchers(
                "/h2-console/**",
                "/actuator/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/docs/**"
        );
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .cors().configurationSource(this::corsConfiguration).and()
                .authorizeRequests()
                .antMatchers("/v1/signup", "/v1/auth/login", "/v1/auth/email/**")
                .permitAll()
                .anyRequest().authenticated().and()
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .build();
    }

    private CorsConfiguration corsConfiguration(HttpServletRequest httpServletRequest) {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.setMaxAge(3600L);
        return config;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
