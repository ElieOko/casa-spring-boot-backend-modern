package server.web.casa.app.property.infrastructure.persistence.repository.favorite

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteVacanceEntity

interface FavoriteVacanceRepository: CoroutineCrudRepository<FavoriteVacanceEntity, Long>
{
    @Query("SELECT * FROM favorite_vacances WHERE user_id = :user")
    fun findFavoriteByUserId(@Param("user") user: Long): Flow<FavoriteVacanceEntity>?

    @Query("SELECT * FROM favorite_vacances WHERE vacance_id = :vacanceId")
    fun findFavoriteByVacance(@Param("vacanceId") vacanceId: Long): Flow<FavoriteVacanceEntity>?

    @Query("SELECT * FROM favorite_vacances WHERE vacance_id = :vacanceId AND user_id = :user")
    fun findFavoriteExist(@Param("vacanceId") vacanceId: Long, @Param("user") user: Long): Flow<FavoriteVacanceEntity>?

}