package server.web.casa.config

import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import server.web.casa.security.JwtAuthFilter
import server.web.casa.utils.Mode

@Configuration
@EnableWebSecurity
@Profile(Mode.DEV)
class ServerConfig(
    private val jwtAuthFilter: JwtAuthFilter
) {
    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity
            .csrf { csrf -> csrf.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.GET,"/clients")
                    .permitAll()

                    .requestMatchers(
                        "/",
                        "/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html/**",
                        "/v3/**"
                    )
                    .permitAll()

                    .requestMatchers("/api/*")
                    .permitAll()

                    .requestMatchers("/auth/*")
                    .permitAll()

                    .requestMatchers(HttpMethod.POST,"/clients")
                    .permitAll()

                    .dispatcherTypeMatchers(
                        DispatcherType.ERROR,
                        DispatcherType.FORWARD
                    )
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }
            .exceptionHandling { configurer ->
                configurer
                    .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            }
            .addFilterBefore(jwtAuthFilter,
                UsernamePasswordAuthenticationFilter::class.java)
            .httpBasic(Customizer.withDefaults())
        return httpSecurity.build()
    }
}