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
public class SecurityConfig {
    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(userAuthenticationEntryPoint))
                .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.POST, "/login", "/register").anonymous()
                        .requestMatchers(HttpMethod.GET, "/users").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/user/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/{id}").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/user/{id}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/movies").permitAll()
                        .requestMatchers(HttpMethod.GET, "/movie/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/movie").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/movie/{id}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/movie/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/movie/{id}/roles").permitAll()
                        .requestMatchers(HttpMethod.POST, "/movie/{movieId}/role").permitAll()
                        .requestMatchers(HttpMethod.POST, "/movie/{movieId}/roles").permitAll()

                        .requestMatchers(HttpMethod.POST, "/movie-genre").permitAll()
                        .requestMatchers(HttpMethod.GET, "/movie-genre/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/movie-genres").permitAll()

                        .requestMatchers(HttpMethod.GET, "/persons").permitAll()
                        .requestMatchers(HttpMethod.GET, "/person/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/person").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/person/{id}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/person/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/person/{id}/roles").permitAll()

                        .requestMatchers(HttpMethod.POST, "/person/{id}/vote").permitAll()
                        .requestMatchers(HttpMethod.GET, "/person/{personId}/vote/{userId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/persons/voted-by/{userId}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/movie-genres").permitAll()
                        .requestMatchers(HttpMethod.GET, "/movie-genre/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/movie-genre").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/movie-genre/{id}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/movie-genre/{id}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/image/{name}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/image").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/image/{name}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/image/{name}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/reviews").permitAll()
                        .requestMatchers(HttpMethod.GET, "/movie/{movieId}/reviews").permitAll()
                        .requestMatchers(HttpMethod.POST, "/movie/{movieId}/review/{userId}").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/movie/{movieId}/review/{reviewId}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/movie/{movieId}/review/{reviewId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/movie/{movieId}/review/{userId}/exists").permitAll()
                        .requestMatchers(HttpMethod.GET, "/movie/{movieId}/review/{userId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/{userId}/reviews").permitAll()
                        .requestMatchers(HttpMethod.POST, "/review/{reviewId}/like").permitAll()
                        .requestMatchers(HttpMethod.GET, "/review/{reviewId}/likes").permitAll()
                        .requestMatchers(HttpMethod.GET, "/review/{reviewId}/like/{userId}/exists").permitAll()

                        .requestMatchers(HttpMethod.GET, "/roles").permitAll()

                        .requestMatchers(HttpMethod.POST, "/movie/favorites/{movieId}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/movie/favorites/{movieId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/movie/favorites/{userId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/movie/favorites/{userId}/isFavorite/{movieId}").permitAll()

                        .requestMatchers(HttpMethod.POST, "/movie/watchlist/{movieId}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/movie/watchlist/{movieId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/movie/watchlist/{userId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/movie/watchlist/{userId}/isInWatchlist/{movieId}").permitAll()

                        .requestMatchers(HttpMethod.POST, "/reports/{reviewId}/user/{userId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/reports").permitAll()
                        .requestMatchers(HttpMethod.GET, "/reports/{reviewId}/user/{userId}/exists").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/reports/{reportId}").permitAll()

                        .requestMatchers(HttpMethod.POST, "/person/{personId}/comment/{userId}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/comment/{commentId}/reply/{userId}").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/comment/{commentId}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/comment/{commentId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/comments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/person/{personId}/comments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/{userId}/comments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/person/{personId}/user/{userId}/has-commented").permitAll()
                        .requestMatchers(HttpMethod.GET, "/comments/search").permitAll()

                        .requestMatchers(HttpMethod.POST, "/comment-reports/{commentId}/user/{userId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/comment-reports").permitAll()
                        .requestMatchers(HttpMethod.GET, "/comment-reports/{commentId}/user/{userId}/exists").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/comment-reports/{reportId}").permitAll()

                        .anyRequest().denyAll()
                );

        return http.build();
    }

    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        return new DefaultMethodSecurityExpressionHandler();
    }
}
