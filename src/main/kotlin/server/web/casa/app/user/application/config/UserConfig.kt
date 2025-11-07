package server.web.casa.app.user.application.config

import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import java.util.Collections


//@Configuration
//class UserDetailsServiceConfig(private val userRepository: UserRepository) {
//
//    @Bean
//    fun userDetailsService(): UserDetailsService = UserDetailsService { username ->
//        val user = userRepository.findByPhoneOrEmail(username)
//            ?: throw RuntimeException("User not found")
//        org.springframework.security.core.userdetails.User
//            .withUsername(user.phone)
//            .password(user.password)
//            .roles() // Pass roles to Spring's User object
//            .build()
//    }
//}

//@Service
//class CustomUserDetailsService : UserDetailsService {
//    private val userRepository: UserRepository? = null
//
//    @Throws(UsernameNotFoundException::class)
//    override fun loadUserByUsername(username: String): UserDetails {
//        val user = userRepository?.findByPhoneOrEmail(username)
//
//        if (user == null) {
//            throw UsernameNotFoundException("User not found with username: " + username)
//        }
//        return User(
//            user.phone, user.password,
//            Collections.singletonList(SimpleGrantedAuthority("USER"))
//        )
//    }
//}