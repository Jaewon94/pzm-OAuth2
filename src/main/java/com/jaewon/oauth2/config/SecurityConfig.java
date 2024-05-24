package com.jaewon.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/home", "/css/**", "/js/**", "/images/**", "/highLightTitle.png").permitAll()
                .anyRequest().authenticated()
        )
        // [구글 로그인] : http://localhost:8081/oauth2/authorization/google ---> 구글로 로그인을 시도하는 URL
        // http://localhost8081/member 요청 -> 로그인?
        .oauth2Login(oauth2 -> oauth2
                .loginPage("/")
                .defaultSuccessUrl("/")
                .permitAll()
        )
        .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        );
        return http.build();
    }
}
