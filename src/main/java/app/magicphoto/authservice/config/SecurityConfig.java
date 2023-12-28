package app.magicphoto.authservice.config;

import app.magicphoto.authservice.config.filter.JwtAuthenticationFilter;
import app.magicphoto.authservice.config.provider.AccessCodeAuthenticationProvider;
import app.magicphoto.authservice.config.provider.PasswordAuthenticationProvider;
import app.magicphoto.authservice.error.security.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AccessCodeAuthenticationProvider accessCodeAuthProvider;
    private final PasswordAuthenticationProvider passwordAuthProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomAuthenticationEntryPoint authEntryPoint;

    @Autowired
    public SecurityConfig(AccessCodeAuthenticationProvider accessCodeAuthProvider,
                          PasswordAuthenticationProvider passwordAuthProvider,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomAuthenticationEntryPoint authEntryPoint) {
        this.accessCodeAuthProvider = accessCodeAuthProvider;
        this.passwordAuthProvider = passwordAuthProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager() {
        return new ProviderManager(passwordAuthProvider, accessCodeAuthProvider);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((customCsrf) -> customCsrf.ignoringRequestMatchers(
                        "/api/register", "/api/login/**", "/api/info"))
                .authorizeHttpRequests((auth) -> {
                    auth.requestMatchers("/api/register", "/api/login/**").permitAll()
                            .anyRequest().authenticated();
                })
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handler -> handler.authenticationEntryPoint(authEntryPoint))
                .formLogin(AbstractHttpConfigurer::disable);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
