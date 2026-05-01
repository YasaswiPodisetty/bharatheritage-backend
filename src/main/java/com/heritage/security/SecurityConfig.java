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
            // Disable CSRF for APIs
            .csrf(AbstractHttpConfigurer::disable)

            // Enable CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Stateless session (JWT)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Authorization rules
            .authorizeHttpRequests(auth -> auth

                // ✅ Allow root (fixes Render 502)
                .requestMatchers("/").permitAll()

                // ✅ Allow preflight requests (CORS fix)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ✅ Public auth APIs
                .requestMatchers("/api/auth/**").permitAll()

                // ✅ Public GET APIs
                .requestMatchers(HttpMethod.GET, "/api/monuments/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/discussions/**").permitAll()

                // ✅ Admin only
                .requestMatchers(HttpMethod.GET,    "/api/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/users/**").hasRole("ADMIN")

                // ✅ Admin + Content Creator
                .requestMatchers(HttpMethod.POST,   "/api/monuments").hasAnyRole("ADMIN", "CONTENT_CREATOR")
                .requestMatchers(HttpMethod.PUT,    "/api/monuments/**").hasAnyRole("ADMIN", "CONTENT_CREATOR")
                .requestMatchers(HttpMethod.DELETE, "/api/monuments/**").hasAnyRole("ADMIN", "CONTENT_CREATOR")

                // ✅ Authenticated users
                .requestMatchers("/api/activity/**").authenticated()
                .requestMatchers(HttpMethod.POST,   "/api/discussions").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/discussions/**").authenticated()
                .requestMatchers(HttpMethod.GET,    "/api/users/**").authenticated()

                // 🔒 Everything else secured
                .anyRequest().authenticated()
            )

            // Add JWT filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 🔥 IMPORTANT: add your frontend deployed URL here
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://indianheritageandculture.netlify.app",
                "https://indianheritageandculture.netlify.app/"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
