package server.web.casa.app.reservation.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import reactor.core.publisher.Mono
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationTerrainEntity
import java.time.LocalDate

interface ReservationTerrainRepository : CoroutineCrudRepository<ReservationTerrainEntity, Long>{

    @Query("SELECT * FROM reservation_terrains WHERE created_at = :date")
    fun findAllByDate(@Param("date") date: LocalDate): Flow<ReservationTerrainEntity>

    @Query(
        """
        SELECT * FROM reservation_terrains 
        WHERE EXTRACT(MONTH FROM created_at) = :month 
          AND EXTRACT(YEAR FROM created_at) = :year
    """
    )
     fun findAllByMonthAndYear(@Param("month") month: Int, @Param("year") year: Int): Flow<ReservationTerrainEntity>

     @Query("SELECT * FROM reservation_terrains  WHERE EXTRACT(YEAR FROM created_at) = :year")
    fun findAllByYear(@Param("year") year: Int): Flow<ReservationTerrainEntity>?

    @Query("SELECT * FROM reservation_terrains  WHERE status = :status")
     fun findAllByStatus(@Param("status") staus: ReservationStatus): Flow<ReservationTerrainEntity>?

    @Query("SELECT * FROM reservation_terrains WHERE start_date >= :startDate AND end_date <= :endDate")
     fun findAllInInterval(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): Flow<ReservationTerrainEntity> ?

    @Query("SELECT * FROM reservation_terrains  WHERE start_date = :startDate AND end_date = :endDate AND terrain_id = :terrainId")
    fun findByStartDateAndEndDateProperty(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        @Param("terrainId") terrainId: Long
    ): Flow<ReservationTerrainEntity>?

    @Query("SELECT * FROM reservation_terrains  WHERE user_id = :userId")
     fun findByUser(@Param("userId") userId: Long): Flow<ReservationTerrainEntity>?

    @Query("SELECT * FROM reservation_terrains WHERE terrain_id = :terrainId")
     fun findByProperty(@Param("terrainId") terrainId: Long): Flow<ReservationTerrainEntity>

     @Query("SELECT * FROM reservation_terrains WHERE terrain_id = :terrainId AND user_id = :userId")
    fun findByUserProperty(@Param("terrainId") terrainId: Long,
                           @Param("userId") userId: Long): Flow<ReservationTerrainEntity>

    @Query("""
        SELECT r.* 
        FROM reservation_terrains r
        JOIN terrains p ON r.terrain_id = p.id
        WHERE p.user_id = :userId
    """)
    fun findByHostUserId(@Param("userId") userId: Long): Flow<ReservationTerrainEntity>

    //use with @Transactional when you call it
    @Modifying
    @Query("UPDATE reservation_terrains SET status = :status WHERE id = :id")
     fun updateStatusById(@Param("id") id: Long, @Param("status") status: ReservationStatus): Mono<Int>

    @Modifying
    @Query("UPDATE reservation_terrains SET is_active = :isActive, status = :status, cancellation_reason = :reason WHERE id = :id")
     fun cancelOrKeepReservation(@Param("id") id: Long,
                                 @Param("isActive") isActive: Boolean,
                                 @Param("reason") reason: String?,
                                 @Param("status") status: ReservationStatus): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_terrains WHERE id = :id")
     fun deleteByIdReservation(@Param("id") id: Long): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_terrains WHERE user_id = :userId")
    fun deleteAllByUserReservation(@Param("userId") userId: Long): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_terrains WHERE terrain_id = :terrainId")
    fun deleteAllByPropertyReservation(@Param("terrainId") terrainId: Long): Mono<Int>
}
