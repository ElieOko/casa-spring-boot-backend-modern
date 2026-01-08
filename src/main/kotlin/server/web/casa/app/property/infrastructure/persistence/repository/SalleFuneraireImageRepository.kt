package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.SalleFestiveImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.SalleFuneraireImageEntity

interface SalleFuneraireImageRepository : CoroutineCrudRepository<SalleFuneraireImageEntity, Long>{
    fun findBySalleFuneraireIdIn(salleFuneraireIds: List<Long>): Flow<SalleFuneraireImageEntity>
}