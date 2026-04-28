package com.heritage.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public — auth endpoints
                .requestMatchers("/api/auth/**").permitAll()
                // Public — read monuments and discussions
                .requestMatchers(HttpMethod.GET, "/api/monuments/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/discussions/**").permitAll()
                // Admin only — user management
                .requestMatchers(HttpMethod.GET,    "/api/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/users/**").hasRole("ADMIN")
                // Content Creator + Admin — create/edit/delete monuments
                .requestMatchers(HttpMethod.POST,   "/api/monuments").hasAnyRole("ADMIN", "CONTENT_CREATOR")
                .requestMatchers(HttpMethod.PUT,    "/api/monuments/**").hasAnyRole("ADMIN", "CONTENT_CREATOR")
                .requestMatchers(HttpMethod.DELETE, "/api/monuments/**").hasAnyRole("ADMIN", "CONTENT_CREATOR")
                // Any authenticated user — activity, discussions write, own profile
                .requestMatchers("/api/activity/**").authenticated()
                .requestMatchers(HttpMethod.POST,   "/api/discussions").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/discussions/**").authenticated()
                .requestMatchers(HttpMethod.GET,    "/api/users/**").authenticated()
                // Everything else requires authentication
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}
