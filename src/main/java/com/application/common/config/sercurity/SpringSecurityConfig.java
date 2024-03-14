package com.application.common.config.sercurity;



import com.application.common.config.jwt.JwtAuthFilter;
import com.application.common.config.jwt.JwtTokenProvider;
import com.application.service.auth.service.LoginService;
import com.application.service.auth.service.LogoutService;
import com.application.service.auth.service.CustomDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class SpringSecurityConfig {
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final CustomDetailsService customDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final static String HTML_PATH = "/user/";
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
        log.info("-------------------Security coning----------");
        http
                .httpBasic(HttpBasicConfigurer::disable)
                .authorizeHttpRequests(auth->auth
                        .requestMatchers(HTML_PATH+"mypage").hasRole("USER")
                        .requestMatchers(HTML_PATH+"admin").hasRole("ADMIN")
//                        .requestMatchers(HTML_PATH+"userlo.html").permitAll()
//                        .requestMatchers(HTML_PATH+"authPage.html").hasRole("Admin")
//                        .requestMatchers(HTML_PATH+"mypage.html").authenticated()
//                        .requestMatchers(HTML_PATH+"bpage.html").authenticated()
                        .anyRequest().permitAll())
                .formLogin( login -> login
                        .usernameParameter("userEmail")
                        .passwordParameter("password")
                        .loginPage(HTML_PATH + "userlo.html")
                        .loginProcessingUrl("/user/loginf")
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
                .exceptionHandling(e->{
                    e.accessDeniedPage("/user/login");
                })
                .sessionManagement( s-> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring()
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }
    @Bean
    public PasswordEncoder passwordEncoder() throws Exception {
        return new BCryptPasswordEncoder();
    }
}
