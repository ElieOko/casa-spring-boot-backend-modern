package server.web.casa.app.property.infrastructure.persistence.repository.favorite

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteBureauEntity

interface FavoriteBureauRepository: CoroutineCrudRepository<FavoriteBureauEntity, Long>
{
    @Query("SELECT * FROM favorite_bureau WHERE user_id = :user")
    fun findFavoriteByUserId(@Param("user") user: Long): Flow<FavoriteBureauEntity>?

    @Query("SELECT * FROM favorite_bureau WHERE bureau_id = :bureauId")
    fun findFavoriteByFestId(@Param("bureauId") bureauId: Long): Flow<FavoriteBureauEntity>?

    @Query("SELECT * FROM favorite_bureau WHERE bureau_id = :bureauId AND user_id = :user")
    fun findFavoriteExist(@Param("bureauId") bureauId: Long, @Param("user") user: Long): Flow<FavoriteBureauEntity>?

}