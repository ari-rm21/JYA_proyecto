package com.JYA_proyecto.JYA_proyecto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                // Páginas públicas
                .requestMatchers("/", "/index", "/tienda/**", "/contacto", "/nosotros", "/politica",
                               "/css/**", "/js/**", "/images/**", "/webjars/**", "/registro/**")
                .permitAll()
                
                // Carrito requiere usuario logueado
                .requestMatchers("/carrito/**", "/checkout/**")
                .hasAnyRole("USER", "ADMIN")
                
                // Administración solo para administradores
                .requestMatchers("/admin/**", "/usuario/**")
                .hasRole("ADMIN")
                
                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}