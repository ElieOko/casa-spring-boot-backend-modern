package server.web.casa.security

import kotlinx.coroutines.coroutineScope
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import server.web.casa.app.user.application.service.AccountService
import server.web.casa.app.user.application.service.AccountUserService
import server.web.casa.app.user.domain.model.UserDto
import server.web.casa.app.user.infrastructure.persistence.mapper.toDomain
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import java.security.Principal

@Service
class Auth(
    private val repository : UserRepository,
    private val account : AccountService,
    private val mutlipleAccount : AccountUserService
) {
    suspend fun user(): Pair<UserDto?, MutableList<Boolean>>?{
        val allowList = mutableListOf<Boolean>()
        SecurityContextHolder.getContext().authentication?.name?.let {
            val userId = it.toInt(16).toLong()
            val data = repository.findById(userId)
            mutlipleAccount.findMultipleAccountUser(userId).forEach{c->allowList.add(account.isAllow(c.accountId))}
            return Pair(data?.toDomain(),allowList)
        }
        return null
    }
    suspend fun userStom(principal: Principal): UserDto? {
        val data = repository.findById(principal.name.toInt().toLong())
        return data?.toDomain()
    }
}