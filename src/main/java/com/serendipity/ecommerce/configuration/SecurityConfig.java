package com.serendipity.ecommerce.configuration;

import com.serendipity.ecommerce.filter.CustomAuthorizationFilter;
import com.serendipity.ecommerce.handler.CustomAccessDeniedHandler;
import com.serendipity.ecommerce.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final BCryptPasswordEncoder encoder;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final CustomAuthorizationFilter customAuthorizationFilter;
    private static final String[] PUBLIC_URLS = {
            "/api/v1/usuario/login/**",
            "/api/v1/usuario/register/**",
            "/api/v1/usuario/verify/code/**",
            "/api/v1/usuario/reset-password/**",
            "/api/v1/usuario/verify/password/**",
            "/api/v1/usuario/verify/account/**",
            "/api/v1/usuario/refresh/token/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable);
        http.sessionManagement((session) -> session.sessionCreationPolicy(STATELESS));
        http.authorizeHttpRequests((request) -> request.requestMatchers(PUBLIC_URLS).permitAll());
        http.authorizeHttpRequests((request) ->
                request.requestMatchers(DELETE, "/api/v1/usuario/delete/**")
                        .hasAnyAuthority("DELETE:USUARIO"));
        http.authorizeHttpRequests((request) ->
                request.requestMatchers(DELETE, "/api/v1/producto/delete/**")
                        .hasAnyAuthority("DELETE:PRODUCTO"));
        http.exceptionHandling((exception) -> exception.accessDeniedHandler(customAccessDeniedHandler).authenticationEntryPoint(customAuthenticationEntryPoint));
        http.authorizeHttpRequests((request) -> request.anyRequest().authenticated());
        http.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return new ProviderManager(authProvider);
    }
}
