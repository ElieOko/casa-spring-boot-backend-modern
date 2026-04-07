package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.BureauEntity
import server.web.casa.app.property.infrastructure.persistence.entity.HotelEntity

interface HotelRepository : CoroutineCrudRepository<HotelEntity, Long>{
    @Query("""SELECT * FROM hotels""")
    fun findAllData(): Flow<HotelEntity>

    @Query("""
        SELECT * FROM hotels
        WHERE is_available = true
    """)
    override fun findAll(): Flow<HotelEntity>

    @Query("SELECT * FROM hotels WHERE user_id = :userId")
    suspend fun getAllByUser(userId: Long) : Flow<HotelEntity?>

    @Modifying
    @Query(
        """ UPDATE hotels
    SET is_available = :state
    WHERE user_id = :userId"""
    )
    suspend fun setUpdateIsAvailable(userId: Long, state: Boolean = false): Int

    @Query("""SELECT * FROM hotels WHERE id = :id AND is_available =true""")
    override suspend fun findById(id : Long): HotelEntity?
    @Query("""SELECT * FROM hotels WHERE id = :id""")
    suspend fun findByIdNoRestrict(id : Long): HotelEntity?
}