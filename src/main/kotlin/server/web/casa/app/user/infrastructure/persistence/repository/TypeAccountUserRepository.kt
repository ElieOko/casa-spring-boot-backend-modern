package server.web.casa.app.user.infrastructure.persistence.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.PrestationEntity
import server.web.casa.app.user.infrastructure.persistence.entity.TypeAccountUserEntity

interface TypeAccountUserRepository : CoroutineCrudRepository<TypeAccountUserEntity, Long> {
    @Query("SELECT * FROM type_account_users WHERE user_id = :userId OR type_account_id = :typeAccountId")
    suspend fun findByUserAndAccount(userId: Long, typeAccountId : Long) : TypeAccountUserEntity?
}