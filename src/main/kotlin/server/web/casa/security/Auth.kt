package server.web.casa.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import server.web.casa.app.user.domain.model.UserDto
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import java.security.Principal

@Service
class Auth(
    private val repository : UserRepository,
    private val mapper : UserMapper
) {
    fun user(): UserDto? {
        SecurityContextHolder.getContext().authentication?.name?.let {
            val data = repository.findById(it.toInt().toLong()).orElse(null)
            return mapper.toDomain(data)
        }
        return null
    }
    fun userStom(principal: Principal): UserDto? {
        val data = repository.findById(principal.name.toInt().toLong()).orElse(null)
        return mapper.toDomain(data)
    }
}