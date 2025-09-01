package com.zavan.dedesite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // arquivos públicos (imagens)
                .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                // estáticos comuns (se tiver)
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                // upload restrito (mas também há @PreAuthorize no controller)
                .requestMatchers("/api/uploads/**").hasAnyRole("ADMIN", "AUTHOR")
                // demais rotas do site (ajuste conforme seu fluxo)
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .rememberMe(remember -> remember
                .key("umaChaveSeguraUnica")
                .tokenValiditySeconds(1209600) // 14 dias
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )   
                
            .csrf(csrf -> csrf.disable()) // ️ apenas para dev  proteger depois  
            .headers(headers -> headers.frameOptions(frame -> frame.disable())); // permite H2 console
        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
