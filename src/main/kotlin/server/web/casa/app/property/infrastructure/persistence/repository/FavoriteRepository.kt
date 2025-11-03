package server.web.casa.app.property.infrastructure.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.FavoriteEntity
import server.web.casa.app.property.infrastructure.persistence.entity.FeatureEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity


interface FavoriteRepository : JpaRepository<FavoriteEntity, Long> {
    @Query("SELECT r FROM FavoriteEntity r WHERE r.property IS NOT NULL AND r.user = :user AND r.feature IS NULL")
    suspend fun findFavoriteByPropertyUser(@Param("user") user: UserEntity): List<FavoriteEntity> ?

    @Query("SELECT r FROM FavoriteEntity r WHERE r.feature IS NOT NULL AND r.user = :user AND r.property IS NULL")
    suspend fun findFavoriteByFeatureUser(@Param("user") user: UserEntity): List<FavoriteEntity> ?

    @Query("SELECT r FROM FavoriteEntity r WHERE r.feature = :feature AND r.property IS NULL")
    suspend fun findOneFavoriteFeatureCount(@Param("feature") feature: FeatureEntity): List<FavoriteEntity> ?

    @Query("SELECT r FROM FavoriteEntity r WHERE r.property = :property  AND r.feature IS NULL")
    suspend fun findOneFavoritePropertyCount(@Param("property") property: PropertyEntity): List<FavoriteEntity> ?

    @Query("SELECT r FROM FavoriteEntity r WHERE r.feature IS NULL")
    suspend fun findAllFavoriteProperty() : List<FavoriteEntity> ?

    @Query("SELECT r FROM FavoriteEntity r WHERE r.property IS NULL")
    suspend fun findAllFavoriteFeature() : List<FavoriteEntity> ?

    @Modifying
    @Query("DELETE FROM FavoriteEntity r WHERE r.id = :id")
    suspend fun deleteByIdFavorite(@Param("id") id: Long): Int

    @Modifying
    @Query("DELETE FROM FavoriteEntity r WHERE r.user = :user AND r.property IS NULL")
    suspend fun deleteAllFavoriteFeatureUser(@Param("user") user: UserEntity): Int

    @Modifying
    @Query("DELETE FROM FavoriteEntity r WHERE r.user = :user AND r.feature IS NULL")
    suspend fun deleteAllFavoritePropertyUser(@Param("user") user: UserEntity): Int

    @Modifying
    @Query("DELETE FROM FavoriteEntity r WHERE r.user = :user")
    suspend fun deleteAllFavoriteUser(@Param("user") user: UserEntity): Int
}