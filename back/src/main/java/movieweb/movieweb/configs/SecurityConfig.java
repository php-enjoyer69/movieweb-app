package movieweb.movieweb.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import movieweb.movieweb.entryPoints.UserAuthenticationEntryPoint;
import movieweb.movieweb.filters.JwtAuthFilter;
import movieweb.movieweb.providers.UserAuthenticationProvider;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig
{
  private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
  private final UserAuthenticationProvider userAuthenticationProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
  {
    http
        .exceptionHandling(customizer -> customizer.authenticationEntryPoint(userAuthenticationEntryPoint))
        .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests((requests) -> requests
            .requestMatchers(HttpMethod.POST, "/login", "/register").anonymous()
                .requestMatchers(HttpMethod.GET, "/users").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/user/{id}").permitAll()

            .requestMatchers(HttpMethod.GET, "/movies").permitAll()
            .requestMatchers(HttpMethod.GET, "/movie/{id}").permitAll()
            .requestMatchers(HttpMethod.POST, "/movie").permitAll()
            .requestMatchers(HttpMethod.PATCH, "/movie/{id}").permitAll()
            .requestMatchers(HttpMethod.DELETE, "/movie/{id}").permitAll()

            .requestMatchers(HttpMethod.GET, "/image/{name}").permitAll()
            .requestMatchers(HttpMethod.POST, "/image").permitAll()
            .requestMatchers(HttpMethod.PATCH, "/image/{name}").permitAll()
            .requestMatchers(HttpMethod.DELETE, "/image/{name}").permitAll()

            .anyRequest().denyAll()
        );

    return http.build();
  }

  @Bean
  static MethodSecurityExpressionHandler methodSecurityExpressionHandler()
  {
    DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
    return expressionHandler;
  }
}