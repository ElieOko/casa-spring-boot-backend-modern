package server.web.casa.app.property.infrastructure.persistence.repository.favorite

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteHotelEntity

interface FavoriteHotelRepository: CoroutineCrudRepository<FavoriteHotelEntity, Long>
{
    @Query("SELECT * FROM favorite_hotels WHERE user_id = :user")
    fun findFavoriteByUserId(@Param("user") user: Long): Flow<FavoriteHotelEntity>?

    @Query("SELECT * FROM favorite_hotels WHERE hotel_id = :hotelId")
    fun findFavoriteByHotel(@Param("hotelId") hotelId: Long): Flow<FavoriteHotelEntity>?

    @Query("SELECT * FROM favorite_hotels WHERE hotel_id = :hotelId AND user_id = :user")
    fun findFavoriteExist(@Param("hotelId") hotelId: Long, @Param("user") user: Long): Flow<FavoriteHotelEntity>?

}