package server.web.casa.app.user.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.PrestationEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyFeatureEntity
import server.web.casa.app.user.infrastructure.persistence.entity.TypeAccountUserEntity

interface TypeAccountUserRepository : CoroutineCrudRepository<TypeAccountUserEntity, Long> {
    @Query("SELECT * FROM type_account_users WHERE user_id = :userId AND type_account_id = :typeAccountId")
    suspend fun findByUserAndAccount(userId: Long, typeAccountId : Long) : TypeAccountUserEntity?
    fun findByUserIdIn(userIds: List<Long>): Flow<TypeAccountUserEntity>
}