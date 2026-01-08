package server.web.casa.app.ecosystem.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.PrestationImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyImageRoomEntity

interface PrestationImageRepository : CoroutineCrudRepository<PrestationImageEntity, Long>{
    fun findByPrestationIdIn(prestationImageIds: List<Long>): Flow<PrestationImageEntity>
}
