package server.web.casa.app.prestation.infrastructure.persistance.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.prestation.infrastructure.persistance.entity.FavoritePrestationEntity

interface FavoritePrestationRepository  : CoroutineCrudRepository<FavoritePrestationEntity, Long>
{
    @Query("SELECT * FROM favorite_prestations WHERE user_id = :userId")
    fun findFavoriteByUserId(@Param("userId") userId: Long): Flow<FavoritePrestationEntity>?

    @Query("SELECT * FROM favorite_prestations WHERE prestation_id = :prestationId")
    fun findFavoriteByPrestationId(@Param("prestationId") prestationId: Long): Flow<FavoritePrestationEntity>?

    @Query("SELECT * FROM favorite_prestations WHERE prestation_id = :prestationId AND user_id = :userId")
    fun findFavoriteByPrestationIdAndUserI(@Param("prestationId") prestationId: Long, @Param("userId") userId: Long): Flow<FavoritePrestationEntity>?

}