package server.web.casa.app.property.infrastructure.persistence.repository


import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import server.web.casa.app.property.infrastructure.persistence.entity.FavoriteEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity


interface FavoriteRepository : CoroutineCrudRepository<FavoriteEntity, Long> {
    @Query("SELECT r FROM FavoriteEntity r WHERE r.user = :user")
    fun findFavoriteByPropertyUser(@Param("user") user: UserEntity): List<FavoriteEntity> ?

    @Query("SELECT r FROM FavoriteEntity r WHERE r.property = :property")
    fun findOneFavoritePropertyCount(@Param("property") property: PropertyEntity): List<FavoriteEntity> ?

    @Query("SELECT r FROM FavoriteEntity r WHERE r.property = :property AND r.user = :user")
    fun findFavoriteExist(@Param("property") property: PropertyEntity, @Param("user") user: UserEntity): List<FavoriteEntity> ?

    @Modifying
    @Query("DELETE FROM FavoriteEntity r WHERE r.id = :id")
    fun deleteByIdFavorite(@Param("id") id: Long): Int

    @Transactional
    @Modifying
    @Query("DELETE FROM FavoriteEntity r WHERE r.user = :user")
    fun deleteAllFavoriteUser(@Param("user") user: UserEntity): Int
}