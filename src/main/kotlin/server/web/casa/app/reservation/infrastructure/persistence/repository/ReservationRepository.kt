package server.web.casa.app.reservation.infrastructure.persistence.repository

import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationEntity
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.transaction.annotation.Transactional
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import reactor.core.publisher.Mono

interface ReservationRepository : CoroutineCrudRepository<ReservationEntity, Long>{

    @Query("SELECT * FROM reservations WHERE created_at = :date")
    fun findAllByDate(@Param("date") date: LocalDate): Flow<ReservationEntity>

    @Query(
        """
        SELECT * FROM reservations 
        WHERE EXTRACT(MONTH FROM created_at) = :month 
          AND EXTRACT(YEAR FROM created_at) = :year
    """
    )
     fun findAllByMonthAndYear(@Param("month") month: Int, @Param("year") year: Int): Flow<ReservationEntity>?

     @Query("SELECT * FROM reservations  WHERE EXTRACT(YEAR FROM created_at) = :year")
    fun findAllByYear(@Param("year") year: Int): Flow<ReservationEntity>?

    @Query("SELECT * FROM reservations  WHERE status = :status")
     fun findAllByStatus(@Param("status") staus: ReservationStatus): Flow<ReservationEntity>?

    @Query("SELECT * FROM reservations WHERE start_date >= :startDate AND end_date <= :endDate")
     fun findAllInInterval(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): Flow<ReservationEntity> ?

    @Query("SELECT * FROM reservations  WHERE start_date = :startDate AND end_date = :endDate AND property_id = :propertyId")
    fun findByStartDateAndEndDateProperty(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        @Param("propertyId") propertyId: Long
    ): Flow<ReservationEntity>?

    @Query("SELECT * FROM reservations  WHERE user_id = :userId")
     fun findByUser(@Param("userId") userId: Long): Flow<ReservationEntity>?

    @Query("SELECT * FROM reservations WHERE property_id = :propertyId")
     fun findByProperty(@Param("propertyId") propertyId: Long): Flow<ReservationEntity> ?

     @Query("SELECT * FROM reservations WHERE property_id = :propertyId AND user_id = :userId")
    fun findByUserProperty(@Param("propertyId") propertyId: Long,
                           @Param("userId") userId: Long): Flow<ReservationEntity> ?

    @Query("""
        SELECT r.* 
        FROM reservations r
        JOIN properties p ON r.property_id = p.id
        WHERE p.user_id = :userId
    """)
    fun findByHostUserId(@Param("userId") userId: Long): Flow<ReservationEntity>

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
