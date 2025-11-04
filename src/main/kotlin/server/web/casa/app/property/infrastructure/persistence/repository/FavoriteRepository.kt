package server.web.casa.app.property.infrastructure.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.FavoriteEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity


interface FavoriteRepository : JpaRepository<FavoriteEntity, Long> {
    @Query("SELECT r FROM FavoriteEntity r WHERE r.user = :user")
    suspend fun findFavoriteByPropertyUser(@Param("user") user: UserEntity): List<FavoriteEntity> ?

    @Query("SELECT r FROM FavoriteEntity r WHERE r.property = :property")
    suspend fun findOneFavoritePropertyCount(@Param("property") property: PropertyEntity): List<FavoriteEntity> ?

    @Query("SELECT r FROM FavoriteEntity r WHERE r.property = :property AND r.user = :user")
    suspend fun findFavoriteExist(@Param("property") property: PropertyEntity, @Param("user") user: UserEntity): FavoriteEntity?

    @Modifying
    @Query("DELETE FROM FavoriteEntity r WHERE r.id = :id")
    suspend fun deleteByIdFavorite(@Param("id") id: Long): Int

    @Modifying
    @Query("DELETE FROM FavoriteEntity r WHERE r.user = :user")
    suspend fun deleteAllFavoriteUser(@Param("user") user: UserEntity): Int
}