package server.web.casa.config

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.*
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import server.web.casa.exception.*
import server.web.casa.security.JwtAuthFilter
import server.web.casa.utils.Mode

@Profile(Mode.DEV)
@EnableWebSecurity
@Configuration
class ServerConfig(
    private val customAuthEntryPoint: CustomAuthEntryPoint,
    private val  customAccessDeniedHandler: CustomAccessDeniedHandler,
    private val jwtAuthFilter: JwtAuthFilter
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    @PostConstruct
    fun init() {
        log.info("✅ ServerConfig is active")
    }
    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
      return httpSecurity
            .csrf { csrf -> csrf.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { configurer ->
                configurer
                    .authenticationEntryPoint(customAuthEntryPoint)
                    .accessDeniedHandler(customAccessDeniedHandler)
            }
          .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
          .httpBasic { it.disable() }
          .formLogin { it.disable() }
            .build()
    }
}
