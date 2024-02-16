package com.application.config;



import com.application.service.LoginService;
import com.application.service.LogoutService;
import com.application.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class SpringSecurityConfig {
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final UserDetailsService userDetailsService;
    private final static String HTML_PATH = "/test/";
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
        log.info("-------------------Security coning----------");
        http.authorizeHttpRequests(auth->auth
                        .requestMatchers(HTML_PATH+"userlo.html").permitAll()
                        .requestMatchers(HTML_PATH+"authPage.html").hasRole("Admin")
                        .requestMatchers(HTML_PATH+"mypage.html").authenticated()
                        .requestMatchers(HTML_PATH+"bpage.html").authenticated()
                        .anyRequest().permitAll())
                .formLogin( login -> login
                        .usernameParameter("userEmail")
                        .passwordParameter("password")
                        .loginPage(HTML_PATH + "userlo.html")
                        .loginProcessingUrl("/user/login")
                        .successHandler(loginService)
                        .failureHandler(loginService)
                )
                .logout( logout -> logout
                        .logoutUrl("/user/logout")
                        .logoutSuccessUrl(HTML_PATH + "userlo.html")
                        .addLogoutHandler(logoutService)
                        .logoutSuccessHandler(logoutService)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me"))
                .rememberMe( re -> re
                        .rememberMeParameter("remember")
                        .tokenValiditySeconds(60 * 60 * 24 * 7)
                        .userDetailsService(userDetailsService)
                        .alwaysRemember(false)
                )
                /* .sessionManagement( smg -> smg
                         .maximumSessions(1)
                         .maxSessionsPreventsLogin(true))*/
                .exceptionHandling( e -> e.accessDeniedPage(HTML_PATH+"accessdenied.html"))
                .csrf(AbstractHttpConfigurer::disable);


        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    @Bean
    public PasswordEncoder passwordEncoder() throws Exception {
        return new BCryptPasswordEncoder();
    }
}
