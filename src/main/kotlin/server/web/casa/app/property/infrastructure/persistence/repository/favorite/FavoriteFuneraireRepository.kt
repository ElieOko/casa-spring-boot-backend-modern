package server.web.casa.app.property.infrastructure.persistence.repository.favorite

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteFuneraireEntity

interface FavoriteFuneraireRepository: CoroutineCrudRepository<FavoriteFuneraireEntity, Long>
{
    @Query("SELECT * FROM favorite_funeraires WHERE user_id = :user")
    fun findFavoriteByUserId(@Param("user") user: Long): Flow<FavoriteFuneraireEntity>?

    @Query("SELECT * FROM favorite_funeraires WHERE funeraire_id = :funeraireId")
    fun findFavoriteByFuneId(@Param("funeraireId") funeraireId: Long): Flow<FavoriteFuneraireEntity>?

    @Query("SELECT * FROM favorite_funeraires WHERE funeraire_id = :funeraireId AND user_id = :user")
    fun findFavoriteExist(@Param("funeraireId") funeraireId: Long, @Param("user") user: Long): Flow<FavoriteFuneraireEntity>?

}