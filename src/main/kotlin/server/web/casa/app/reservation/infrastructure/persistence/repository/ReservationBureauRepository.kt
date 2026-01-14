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

    @Query("SELECT * FROM reservation_bureau WHERE created_at = :date")
    fun findAllByDate(@Param("date") date: LocalDate): Flow<ReservationBureauEntity>

    @Query(
        """
        SELECT * FROM reservation_bureau 
        WHERE EXTRACT(MONTH FROM created_at) = :month 
          AND EXTRACT(YEAR FROM created_at) = :year
    """
    )
     fun findAllByMonthAndYear(@Param("month") month: Int, @Param("year") year: Int): Flow<ReservationBureauEntity>?

     @Query("SELECT * FROM reservation_bureau  WHERE EXTRACT(YEAR FROM created_at) = :year")
    fun findAllByYear(@Param("year") year: Int): Flow<ReservationBureauEntity>?

    @Query("SELECT * FROM reservation_bureau  WHERE status = :status")
     fun findAllByStatus(@Param("status") staus: ReservationStatus): Flow<ReservationBureauEntity>?

    @Query("SELECT * FROM reservation_bureau WHERE start_date >= :startDate AND end_date <= :endDate")
     fun findAllInInterval(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): Flow<ReservationBureauEntity> ?

    @Query("SELECT * FROM reservation_bureau  WHERE start_date = :startDate AND end_date = :endDate AND bureau_id = :bureauId")
    fun findByStartDateAndEndDateProperty(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        @Param("bureauId") bureauId: Long
    ): Flow<ReservationBureauEntity>?

    @Query("SELECT * FROM reservation_bureau  WHERE user_id = :userId")
     fun findByUser(@Param("userId") userId: Long): Flow<ReservationBureauEntity>?

    @Query("SELECT * FROM reservation_bureau WHERE bureau_id = :bureauId")
     fun findByProperty(@Param("bureauId") bureauId: Long): Flow<ReservationBureauEntity> ?

     @Query("SELECT * FROM reservation_bureau WHERE bureau_id = :bureauId AND user_id = :userId")
    fun findByUserProperty(@Param("bureauId") bureauId: Long,
                           @Param("userId") userId: Long): Flow<ReservationBureauEntity> ?

    @Query("""
        SELECT r.* 
        FROM reservation_bureau r
        JOIN bureau p ON r.bureau_id = p.id
        WHERE p.user_id = :userId
    """)
    fun findByHostUserId(@Param("userId") userId: Long): Flow<ReservationBureauEntity>

    //use with @Transactional when you call it
    @Modifying
    @Query("UPDATE reservation_bureau SET status = :status WHERE id = :id")
     fun updateStatusById(@Param("id") id: Long, @Param("status") status: ReservationStatus): Mono<Int>

    @Modifying
    @Query("UPDATE reservation_bureau SET is_active = :isActive, status = :status, cancellation_reason = :reason WHERE id = :id")
     fun cancelOrKeepReservation(@Param("id") id: Long,
                                 @Param("isActive") isActive: Boolean,
                                 @Param("reason") reason: String?,
                                 @Param("status") status: ReservationStatus
    ): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_bureau WHERE id = :id")
     fun deleteByIdReservation(@Param("id") id: Long): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_bureau WHERE user_id = :userId")
    fun deleteAllByUserReservation(@Param("userId") userId: Long): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_bureau WHERE bureau_id = :bureauId")
    fun deleteAllByPropertyReservation(@Param("bureauId") bureauId: Long): Mono<Int>
}
