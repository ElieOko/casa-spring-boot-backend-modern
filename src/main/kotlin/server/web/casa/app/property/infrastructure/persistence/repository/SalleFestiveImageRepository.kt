package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.SalleFestiveImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.VacanceImageEntity

interface SalleFestiveImageRepository : CoroutineCrudRepository<SalleFestiveImageEntity, Long>{
    fun findBySalleFestiveIdIn(salleFestiveIds: List<Long>): Flow<SalleFestiveImageEntity>
}