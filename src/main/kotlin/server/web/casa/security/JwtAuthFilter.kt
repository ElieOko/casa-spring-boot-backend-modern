package server.web.casa.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import server.web.casa.app.user.application.TypeAccountService
import server.web.casa.app.user.application.UserService
import server.web.casa.app.user.domain.model.CustomUserDetails
import server.web.casa.app.user.domain.model.TypeAccount
import server.web.casa.app.user.infrastructure.persistence.repository.TypeAccountRepository
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import server.web.casa.utils.Mode

@Component
//@Profile(Mode.DEV)
class JwtAuthFilter(
    private val jwtService: JwtService,
    private val userService: UserService,

): OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.requestURI
        val publicPaths = listOf(
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
//            "/api/users"
        )
        log.info("request level->",request.requestURI)
        if (publicPaths.contains(request.requestURI)) {
            // Route plog.info("request",request.requestURI)ublique → passer directement
            log.info(request.requestURI)
            filterChain.doFilter(request, response)
            return
        }
//        val authHeader = request.getHeader("Authorization")
//        log.info("header->> ${request.getHeader("Authorization")}")
//        log.info("*******************",SecurityContextHolder.getContext().authentication)
//                 //   && SecurityContextHolder.getContext().authentication == null
//        if(authHeader != null && authHeader.startsWith("Bearer ") ) {
//            log.info("okomi ti awa ${request.requestURI}")
//            if(jwtService.validateAccessToken(authHeader)) {
//                log.info("ici")
//                val userId = jwtService.getUserIdFromToken(authHeader)
//                log.info("oyo eza yo $userId")
////                    val user = manager.loadUserByUsername("user")
//                val auth = UsernamePasswordAuthenticationToken(userId, null, emptyList()).apply {
//                        details = WebAuthenticationDetailsSource().buildDetails(request)
//                    }
//                    SecurityContextHolder.getContext().authentication = auth
//                    log.info("auth ${auth.name}")
//                }
//            else{
//                    log.info("Invalid access token for ${request.requestURI}")
//                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is missing")
//                        return
//                }
//            }
//        log.info("JWT Filter triggered for ${request.requestURI}")

        val authHeader = request.getHeader("Authorization")
        log.info("ICI")
        if (authHeader == null || !authHeader.startsWith("Bearer ") || !jwtService.validateAccessToken(authHeader)) {
            log.info("nGGOUSF")
           // filterChain.doFilter(request, response)
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid or missing JWT token")
            return
        }
        val userId = jwtService.getUserIdFromToken(authHeader)
        log.info("nGGOUS")
        val auth = UsernamePasswordAuthenticationToken(userId, null, emptyList()).apply {
            details = WebAuthenticationDetailsSource().buildDetails(request)
        }
        log.info("nOUS")
        filterChain.doFilter(request, response)
        SecurityContextHolder.getContext().authentication = auth
//        filterChain.doFilter(request, response)
//        filterChain.doFilter(request, response)
    }
}