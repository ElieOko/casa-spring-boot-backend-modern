package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.AgenceEntity
import server.web.casa.app.property.infrastructure.persistence.entity.HotelEntity

interface HotelRepository : CoroutineCrudRepository<HotelEntity, Long>{
    @Query("SELECT * FROM hotels WHERE user_id = :userId")
    suspend fun getAllByUser(userId: Long) : Flow<HotelEntity?>

    @Modifying
    @Query(
        """ UPDATE salle_festives
    SET is_available = :state
    WHERE user_id = :userId"""
    )
    suspend fun setUpdateIsAvailable(userId: Long, state: Boolean = false): Int
}