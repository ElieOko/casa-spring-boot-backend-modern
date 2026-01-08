package server.web.casa.app.property.infrastructure.persistence.repository


import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import server.web.casa.app.property.infrastructure.persistence.entity.FavoriteEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity


interface FavoriteRepository : CoroutineCrudRepository<FavoriteEntity, Long> {
    @Query("SELECT * FROM favorites WHERE user_id = :user")
    fun findFavoriteByPropertyUser(@Param("user") user: Long): Flow<FavoriteEntity>?

    @Query("SELECT * FROM favorites WHERE property_id = :property")
    fun findOneFavoritePropertyCount(@Param("property") property: Long): Flow<FavoriteEntity>?

    @Query("SELECT * FROM favorites WHERE property_id = :property AND user_id = :user")
    fun findFavoriteExist(@Param("property") property: Long, @Param("user") user: Long): Flow<FavoriteEntity>?

    @Modifying
    @Query("DELETE FROM favorites WHERE id = :id")
    fun deleteByIdFavorite(@Param("id") id: Long): Int

    @Transactional
    @Modifying
    @Query("DELETE FROM favorites WHERE user_id = :user")
    fun deleteAllFavoriteUser(@Param("user") user: Long): Int
}