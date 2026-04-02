package com.nakshatra.spring_deploy_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // Inside your SecurityFilterChain bean
http.csrf(csrf -> csrf.disable())
    .headers(headers -> headers.frameOptions(frame -> frame.disable())) // Allows H2 UI
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/h2-console/**", "/signup", "/api/users/register", "/login", "/api/users/**").permitAll()
    .anyRequest().authenticated()
)
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/", true)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .permitAll()
        )
        .headers(headers -> headers.frameOptions(frame -> frame.disable())); // Required for H2 Console display
        
    return http.build();
}

    @Bean
public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance(); 
}
}