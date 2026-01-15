package server.web.casa.app.reservation.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import reactor.core.publisher.Mono
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationVacanceEntity
import java.time.LocalDate

interface ReservationVacanceRepository : CoroutineCrudRepository<ReservationVacanceEntity, Long>{

    @Query("SELECT * FROM reservation_vacances WHERE created_at = :date")
    fun findAllByDate(@Param("date") date: LocalDate): Flow<ReservationVacanceEntity>

    @Query(
        """
        SELECT * FROM reservation_vacances 
        WHERE EXTRACT(MONTH FROM created_at) = :month 
          AND EXTRACT(YEAR FROM created_at) = :year
    """
    )
     fun findAllByMonthAndYear(@Param("month") month: Int, @Param("year") year: Int): Flow<ReservationVacanceEntity>

     @Query("SELECT * FROM reservation_vacances  WHERE EXTRACT(YEAR FROM created_at) = :year")
    fun findAllByYear(@Param("year") year: Int): Flow<ReservationVacanceEntity>?

    @Query("SELECT * FROM reservation_vacances  WHERE status = :status")
     fun findAllByStatus(@Param("status") staus: ReservationStatus): Flow<ReservationVacanceEntity>?

    @Query("SELECT * FROM reservation_vacances WHERE start_date >= :startDate AND end_date <= :endDate")
     fun findAllInInterval(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): Flow<ReservationVacanceEntity> ?

    @Query("SELECT * FROM reservation_vacances  WHERE start_date = :startDate AND end_date = :endDate AND vacance_id = :vacanceId")
    fun findByStartDateAndEndDateProperty(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        @Param("vacanceId") vacanceId: Long
    ): Flow<ReservationVacanceEntity>?

    @Query("SELECT * FROM reservation_vacances  WHERE user_id = :userId")
     fun findByUser(@Param("userId") userId: Long): Flow<ReservationVacanceEntity>?

    @Query("SELECT * FROM reservation_vacances WHERE vacance_id = :vacanceId")
     fun findByProperty(@Param("vacanceId") vacanceId: Long): Flow<ReservationVacanceEntity>

     @Query("SELECT * FROM reservation_vacances WHERE vacance_id = :vacanceId AND user_id = :userId")
    fun findByUserProperty(@Param("vacanceId") vacanceId: Long,
                           @Param("userId") userId: Long): Flow<ReservationVacanceEntity>

    @Query("""
        SELECT r.* 
        FROM reservation_vacances r
        JOIN vacances p ON r.vacance_id = p.id
        WHERE p.user_id = :userId
    """)
    fun findByHostUserId(@Param("userId") userId: Long): Flow<ReservationVacanceEntity>

    //use with @Transactional when you call it
    @Modifying
    @Query("UPDATE reservation_vacances SET status = :status WHERE id = :id")
     fun updateStatusById(@Param("id") id: Long, @Param("status") status: ReservationStatus): Mono<Int>

    @Modifying
    @Query("UPDATE reservation_vacances SET is_active = :isActive, status = :status, cancellation_reason = :reason WHERE id = :id")
     fun cancelOrKeepReservation(@Param("id") id: Long,
                                 @Param("isActive") isActive: Boolean,
                                 @Param("reason") reason: String?,
                                 @Param("status") status: ReservationStatus): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_vacances WHERE id = :id")
     fun deleteByIdReservation(@Param("id") id: Long): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_vacances WHERE user_id = :userId")
    fun deleteAllByUserReservation(@Param("userId") userId: Long): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_vacances WHERE vacance_id = :vacanceId")
    fun deleteAllByPropertyReservation(@Param("vacanceId") vacanceId: Long): Mono<Int>
}
