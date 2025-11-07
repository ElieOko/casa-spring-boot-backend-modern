package server.web.casa.config

import jakarta.annotation.PostConstruct
import jakarta.servlet.DispatcherType
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import server.web.casa.app.user.domain.model.UserDetailsServiceImpl
import server.web.casa.exception.CustomAccessDeniedHandler
import server.web.casa.exception.CustomAuthEntryPoint
import server.web.casa.security.JwtAuthFilter
//import server.web.casa.security.JwtAuthFilter
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
//        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)
    }
//    @Bean
//	fun userDetailsService() : UserDetailsService {
//		val user =
//			 User.withDefaultPasswordEncoder()
//				.username("user")
//				.password("password")
//				.roles("USER")
//				.build();
//
//		return InMemoryUserDetailsManager(user);
//	}
    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        log.info("connect->",SecurityContextHolder.getContext().authentication)
      return httpSecurity
            .csrf { csrf -> csrf.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
//          .securityMatcher("/api/**")
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/",
                        "/websocket/*",
                        "/swagger-ui/**",
                        "/swagger-ui.html/**",
                        "/v3/**",
                        "/api/account",
                        "/api/cities",
                        "/api/countries",
                        "/api/communes",
                        "/api/districts",
                        "/api/quartiers",
                        "/auth/login",
                        "/auth/register",
                        "/api/users"
                    ).permitAll()
                    .dispatcherTypeMatchers(
                        DispatcherType.ERROR,
                        DispatcherType.FORWARD
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .exceptionHandling { configurer ->

                configurer
                    .authenticationEntryPoint(customAuthEntryPoint)
//                    .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//                    .accessDeniedHandler(customAccessDeniedHandler)
            }
          .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
          .httpBasic { it.disable() }
          .formLogin { it.disable() }
            .build()
    }
}
