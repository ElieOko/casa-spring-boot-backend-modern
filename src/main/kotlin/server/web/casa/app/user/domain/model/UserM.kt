package server.web.casa.app.user.domain.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import server.web.casa.app.user.application.UserService
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity

class CustomUserDetails(private val user: User, private val userService: UserService) : UserDetails {

    override fun getAuthorities(): Collection<out GrantedAuthority> {
//        val roles = userService.account(user.userId)
        return listOf( SimpleGrantedAuthority("USER"))
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getUsername(): String {
        return user.phone
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}

class UserDetailsServiceImpl() : UserDetailsService {

    val userService: UserService? = null
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        val user: User? = userService?.findUsernameOrEmail(email)
        if(user != null) {
            return CustomUserDetails(user, userService)
        }
        throw UsernameNotFoundException("Could not find user")
    }
}