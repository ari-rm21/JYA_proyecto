package com.JYA_proyecto.JYA_proyecto.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService uds;

    public SecurityConfig(@Qualifier("userDetailsService") UserDetailsService uds) {
        this.uds = uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder enc) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds);
        p.setPasswordEncoder(enc);
        return p;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationProvider ap) throws Exception {
        http
          .authenticationProvider(ap) // forzar provider
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/", "/index", "/tienda/**", "/contacto", "/nosotros", "/politica",
                               "/css/**", "/js/**", "/images/**", "/webjars/**", "/login", "/registro/**")
              .permitAll()
              .requestMatchers("/carrito/**", "/checkout/**", "/comprar/**").hasAnyRole("USER","ADMIN")
              .requestMatchers("/admin/**", "/productos/**").hasRole("ADMIN")
              .anyRequest().authenticated()
          )
          .formLogin(f -> f
              .loginPage("/login")
              .usernameParameter("username")
              .passwordParameter("password")
              .defaultSuccessUrl("/", true)
              .failureUrl("/login?error")
              .permitAll()
          )
          .logout(l -> l.logoutUrl("/logout").logoutSuccessUrl("/").permitAll());

        return http.build();
    }
}
