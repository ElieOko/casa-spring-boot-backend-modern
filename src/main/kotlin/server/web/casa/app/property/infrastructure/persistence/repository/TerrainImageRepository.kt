package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.TerrainImageEntity

interface TerrainImageRepository  : CoroutineCrudRepository<TerrainImageEntity, Long> {
    fun findByTerrainIdIn(terrainIds: List<Long>): Flow<TerrainImageEntity>
    @Query("SELECT * FROM terrain_images WHERE terrain_id = :terrainId")
    fun findByTerrainId(@Param("terrainId") terrainId: Long): Flow<TerrainImageEntity>


}