package server.web.casa.config

import jakarta.servlet.DispatcherType
import org.slf4j.LoggerFactory
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
import server.web.casa.exception.CustomAccessDeniedHandler
import server.web.casa.exception.CustomAuthEntryPoint
import server.web.casa.security.JwtAuthFilter
import server.web.casa.utils.Mode
import kotlin.math.log

@Configuration
@EnableWebSecurity
@Profile(Mode.DEV)
class ServerConfig(
    private val jwtAuthFilter: JwtAuthFilter,
    private val customAuthEntryPoint: CustomAuthEntryPoint,
    private val  customAccessDeniedHandler: CustomAccessDeniedHandler
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity
            .csrf { csrf -> csrf.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/",
                        "/websocket/*",
                        "/swagger-ui/**",
                        "/swagger-ui.html/**",
                        "/v3/**"
                    )
                    .permitAll()
                    .requestMatchers("/api/*")
                    .permitAll()
                    .requestMatchers("/auth/login","/auth/register")
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
                log.info("____________${HttpStatus.UNAUTHORIZED}")
                configurer
                    .authenticationEntryPoint(customAuthEntryPoint)
                    .accessDeniedHandler(customAccessDeniedHandler)
            }
            .addFilterBefore(jwtAuthFilter,
                UsernamePasswordAuthenticationFilter::class.java)
            .httpBasic(Customizer.withDefaults())
        return httpSecurity.build()
    }
}