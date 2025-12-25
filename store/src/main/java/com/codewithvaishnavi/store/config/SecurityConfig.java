package com.codewithvaishnavi.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Added
import org.springframework.security.crypto.password.PasswordEncoder;   // Added
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Added: Encodes passwords for security
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/style.css").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout") // Redirect to login with a logout parameter
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );
        return http.build();
    }
}