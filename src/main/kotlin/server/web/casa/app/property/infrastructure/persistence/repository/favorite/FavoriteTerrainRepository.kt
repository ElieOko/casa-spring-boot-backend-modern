package server.web.casa.app.property.infrastructure.persistence.repository.favorite

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteTerrainEntity
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteVacanceEntity

interface FavoriteTerrainRepository: CoroutineCrudRepository<FavoriteTerrainEntity, Long>
{
    @Query("SELECT * FROM favorite_terrains WHERE user_id = :user")
    fun findFavoriteByUserId(@Param("user") user: Long): Flow<FavoriteTerrainEntity>?

    @Query("SELECT * FROM favorite_terrains WHERE terrain_id = :terrainId")
    fun findFavoriteByTerrain(@Param("terrainId") terrainId: Long): Flow<FavoriteTerrainEntity>?

    @Query("SELECT * FROM favorite_terrains WHERE terrain_id = :terrainId AND user_id = :user")
    fun findFavoriteExist(@Param("terrainId") terrainId: Long, @Param("user") user: Long): Flow<FavoriteTerrainEntity>?

}