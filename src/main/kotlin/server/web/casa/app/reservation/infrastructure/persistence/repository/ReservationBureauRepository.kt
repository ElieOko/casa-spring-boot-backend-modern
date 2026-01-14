package server.web.casa.app.reservation.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import reactor.core.publisher.Mono
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationBureauEntity
import java.time.LocalDate

interface ReservationBureauRepository : CoroutineCrudRepository<ReservationBureauEntity, Long>{

    @Query("SELECT * FROM reservations WHERE created_at = :date")
    fun findAllByDate(@Param("date") date: LocalDate): Flow<ReservationBureauEntity>

    @Query(
        """
        SELECT * FROM reservations 
        WHERE EXTRACT(MONTH FROM created_at) = :month 
          AND EXTRACT(YEAR FROM created_at) = :year
    """
    )
     fun findAllByMonthAndYear(@Param("month") month: Int, @Param("year") year: Int): Flow<ReservationBureauEntity>?

     @Query("SELECT * FROM reservations  WHERE EXTRACT(YEAR FROM created_at) = :year")
    fun findAllByYear(@Param("year") year: Int): Flow<ReservationBureauEntity>?

    @Query("SELECT * FROM reservations  WHERE status = :status")
     fun findAllByStatus(@Param("status") staus: ReservationStatus): Flow<ReservationBureauEntity>?

    @Query("SELECT * FROM reservations WHERE start_date >= :startDate AND end_date <= :endDate")
     fun findAllInInterval(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): Flow<ReservationBureauEntity> ?

    @Query("SELECT * FROM reservations  WHERE start_date = :startDate AND end_date = :endDate AND property_id = :propertyId")
    fun findByStartDateAndEndDateProperty(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        @Param("propertyId") propertyId: Long
    ): Flow<ReservationBureauEntity>?

    @Query("SELECT * FROM reservations  WHERE user_id = :userId")
     fun findByUser(@Param("userId") userId: Long): Flow<ReservationBureauEntity>?

    @Query("SELECT * FROM reservations WHERE property_id = :propertyId")
     fun findByProperty(@Param("propertyId") propertyId: Long): Flow<ReservationBureauEntity> ?

     @Query("SELECT * FROM reservations WHERE property_id = :propertyId AND user_id = :userId")
    fun findByUserProperty(@Param("propertyId") propertyId: Long,
                           @Param("userId") userId: Long): Flow<ReservationBureauEntity> ?

    @Query("""
        SELECT r.* 
        FROM reservations r
        JOIN bureau p ON r.property_id = p.id
        WHERE p.user_id = :userId
    """)
    fun findByHostUserId(@Param("userId") userId: Long): Flow<ReservationBureauEntity>

    //use with @Transactional when you call it
    @Modifying
    @Query("UPDATE reservations SET status = :status WHERE id = :id")
     fun updateStatusById(@Param("id") id: Long, @Param("status") status: ReservationStatus): Mono<Int>

    @Modifying
    @Query("UPDATE reservations SET is_active = :isActive, status = :status, cancellation_reason = :reason WHERE id = :id")
     fun cancelOrKeepReservation(@Param("id") id: Long,
                                 @Param("isActive") isActive: Boolean,
                                 @Param("reason") reason: String?,
                                 @Param("status") status: ReservationStatus): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservations WHERE id = :id")
     fun deleteByIdReservation(@Param("id") id: Long): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservations WHERE user_id = :userId")
    fun deleteAllByUserReservation(@Param("userId") userId: Long): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservations WHERE property_id = :propertyId")
    fun deleteAllByPropertyReservation(@Param("propertyId") propertyId: Long): Mono<Int>
}
