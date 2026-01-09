package server.web.casa.app.property.infrastructure.persistence.repository.favorite

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteFestiveEntity

interface FavoriteFestiveRepository: CoroutineCrudRepository<FavoriteFestiveEntity, Long>
{
    @Query("SELECT * FROM favorite_festives WHERE user_id = :user")
    fun findFavoriteByUserId(@Param("user") user: Long): Flow<FavoriteFestiveEntity>?

    @Query("SELECT * FROM favorite_festives WHERE festive_id = :festiveId")
    fun findFavoriteByFestId(@Param("festiveId") festiveId: Long): Flow<FavoriteFestiveEntity>?

    @Query("SELECT * FROM favorite_festives WHERE festive_id = :festiveId AND user_id = :user")
    fun findFavoriteExist(@Param("festiveId") festiveId: Long, @Param("user") user: Long): Flow<FavoriteFestiveEntity>?

}