package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.BureauFeatureEntity
import server.web.casa.app.property.infrastructure.persistence.entity.FestiveFeatureEntity
import server.web.casa.app.property.infrastructure.persistence.entity.FuneraireFeatureEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyFeatureEntity

interface FuneraireFeatureRepository : CoroutineCrudRepository<FuneraireFeatureEntity, Long>{
    fun findByFuneraireIdIn(funeraireIds: List<Long>): Flow<FuneraireFeatureEntity>
}
