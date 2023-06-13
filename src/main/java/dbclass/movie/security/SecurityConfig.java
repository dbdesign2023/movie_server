package dbclass.movie.security;

import dbclass.movie.security.handler.JwtAccessDeniedHandler;
import dbclass.movie.security.handler.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Log4j2
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final CorsConfig corsConfig;

    private static final String[] URL_TO_PERMIT = {
            "/v3/api-docs/**", "/swagger-ui/**",    //swagger
            "/admin/signup", "/admin/signin",       //admin
            "/api/**",                              //image api
            "/customer/signup", "/customer/signin",    //customer
            "/movie/detail", "/movie/all", "/movie/cast/detail",  //movie
            "/payment/pay", "/payment/method/list", "/payment/detail/nonmember",   //payment
            "/schedule/date/**", "/schedule/movie/**", "/schedule/allMovie", "/schedule/previous", "/schedule/{id}/seats", "/schedule/{id}/seats/ticket",   //schedule
            "/theater/{id}", "/theater/{id}/seat", "/theater/all",     //theater
            "/ticket/reservation", "/ticket/nonmember/**",  "/ticket/delete"   //ticket
    };
    private static final String[] URL_ADMIN_ONLY = {
            "/admin/**",      //admin
            "/movie/**", "/movie/genre/**", "/movie/rating/**", "/movie/cast/**", "/movie/{movieId}/role/**",   //movie
            "/schedule/add", "/schedule/{id}/modify", "/schedule/{id}/delete",     //schedule
            "/theater/**", "/theater/{id}/**", "/theater/{id}/seat/**", "/theater/{id}/seat/delete"     //theater
    };

    private static final String[] URL_CUSTOMER_ONLY = {
            "/customer/**",             //customer
            "/payment/list", "/payment/detail/member",    //payment
            "/ticket/member/**"    //ticket
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()          //csrf설정 끔
                .sessionManagement()     //세션은 stateless방식
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .cors().configurationSource(corsConfigurationSource())

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()                //jwt를 사용하는 STATELESS방식이므로 session 사용하지 않는다고 명시
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeHttpRequests()
                .requestMatchers(URL_TO_PERMIT).permitAll()
                .requestMatchers(URL_ADMIN_ONLY).hasRole("ADMIN")
                .requestMatchers(URL_CUSTOMER_ONLY).hasRole("USER")
                .anyRequest().authenticated();

        http
                .addFilter(corsConfig.corsFilter())
                .addFilterBefore(new JwtRequestFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*");


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
