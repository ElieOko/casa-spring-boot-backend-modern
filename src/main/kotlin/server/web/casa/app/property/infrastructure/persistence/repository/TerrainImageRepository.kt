package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.*
interface TerrainImageRepository  : CoroutineCrudRepository<TerrainImageEntity, Long> {
    fun findByTerrainIdIn(terrainIds: List<Long>): Flow<TerrainImageEntity>
}