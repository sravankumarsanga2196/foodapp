package com.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final JwtAuthenticationEntryPoint entryPoint;

    // ✅ SECURITY FILTER CHAIN
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // ✅ Enable CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // ❌ Disable CSRF (for APIs)
            .csrf(csrf -> csrf.disable())

            // ✅ Stateless session (JWT)
            .sessionManagement(sess ->
                    sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // ✅ Exception handling
            .exceptionHandling(ex ->
                    ex.authenticationEntryPoint(entryPoint))

            // ✅ API Authorization
            .authorizeHttpRequests(auth -> auth

                // 🔓 PUBLIC APIs
                .requestMatchers(
                        "/api/user/generate-otp",
                        "/api/user/verify-otp",

                        // Swagger
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()

                // 🔐 PROTECTED APIs
                .requestMatchers("/api/user/**").authenticated()
                .requestMatchers("/api/restaurants/**").authenticated()
                .requestMatchers("/api/menu/**").authenticated()
                .requestMatchers("/api/cart/**").authenticated()
                .requestMatchers("/api/orders/**").authenticated()
                .requestMatchers("/api/payments/**").authenticated()
                .requestMatchers("/api/location/**").authenticated()

                // 🔐 Any other request
                .anyRequest().authenticated()
            )

            // ✅ Add JWT Filter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ✅ GLOBAL CORS CONFIG
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);

        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://your-frontend.vercel.app"
        ));

        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}