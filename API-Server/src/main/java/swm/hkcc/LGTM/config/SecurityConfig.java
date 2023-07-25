package swm.hkcc.LGTM.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import swm.hkcc.LGTM.app.modules.auth.utils.jwt.JwtFilter;
import swm.hkcc.LGTM.app.modules.auth.utils.jwt.TokenProvider;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors(c -> {
                            CorsConfigurationSource source = request -> {
                                CorsConfiguration config = new CorsConfiguration();
                                config.setAllowedOrigins(
                                        List.of("*")
                                );
                                config.setAllowedMethods(
                                        List.of("*")
                                );
                                return config;
                            };
                            c.configurationSource(source);
                        }
                )
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/v1/**").hasRole("USER")
                .requestMatchers("/v1/signup/**", "/v1/intro").permitAll()
                .requestMatchers("/login/**").permitAll() // for temporary test
                .requestMatchers("/docs/**", "/v3/api-docs/swagger-config").permitAll()
                .anyRequest().denyAll()
                .and()
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(403);
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("text/html; charset=UTF-8");
                    response.getWriter().write("Invalid role");
                })
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(401);
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("text/html; charset=UTF-8");
                    response.getWriter().write("Invalid authentication");
                });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

