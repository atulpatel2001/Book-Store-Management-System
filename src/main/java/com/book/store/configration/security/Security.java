package com.book.store.configration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class Security {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;
    @Bean
    UserDetailsService getUserDetailsService() {
        return new CustomerDetailServiceImple();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    DaoAuthenticationProvider getDaoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.getUserDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->auth.requestMatchers("/admin/**")
                        .hasRole("ADMIN")
                        .requestMatchers("/purchase-book/**")
                        .hasRole("CUSTOMER")
                        .requestMatchers("/manager/**")
                        .hasRole("EMPLOYEE-M")
                        .requestMatchers("/staff/**")
                        .hasRole("EMPLOYEE-S")
                        .requestMatchers("/**")
                        .permitAll()
                        .anyRequest().authenticated()).formLogin(form-> {
                    form.loginPage("/signin").loginProcessingUrl("/doLogin").defaultSuccessUrl("/Book-Store/customer/index", true).successHandler(this.customAuthenticationSuccessHandler);
                }).logout(log ->log.logoutSuccessHandler(this.customLogoutSuccessHandler));

        return httpSecurity.build();
    }

}
