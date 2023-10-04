package swm.hkcc.LGTM.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
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
import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.dto.ApiResponse;
import swm.hkcc.LGTM.app.modules.auth.utils.jwt.JwtFilter;
import swm.hkcc.LGTM.app.modules.auth.utils.jwt.TokenProvider;

import java.io.IOException;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors(c -> c.configurationSource(corsConfiguration()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/v1/signup/**", "/v1/intro", "/login/**", "/docs/**", "/v3/api-docs/swagger-config", "/admin/**").permitAll()
                .requestMatchers("/chat/**", "/chatroom/**").permitAll()
                .requestMatchers("/sub/**", "/pub/**", "/ws-stomp/**").permitAll()
                .requestMatchers("/js/**", "/css/**", "/images/**").permitAll()
                .requestMatchers("/api/**").hasRole("USER")
                .anyRequest().denyAll()
                .and()
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler((request, response, authException) -> sendErrorResponse(response, ResponseCode.INVALID_ROLE))
                .authenticationEntryPoint((request, response, accessDeniedException) -> sendErrorResponse(response, ResponseCode.INVALID_AUTHENTICATION));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private CorsConfigurationSource corsConfiguration() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("*"));
            config.setAllowedMethods(List.of("*"));
            return config;
        };
    }

    private void sendErrorResponse(HttpServletResponse response, ResponseCode responseCode) throws IOException {
        ApiResponse apiResponse = ApiResponse.of(false, responseCode);
        response.setStatus(responseCode.getHttpStatus().value());
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
