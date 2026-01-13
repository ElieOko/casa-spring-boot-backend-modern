package server.web.casa.app.reservation.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import reactor.core.publisher.Mono
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationFestiveEntity
import java.time.LocalDate

interface ReservationFestiveRepository : CoroutineCrudRepository<ReservationFestiveEntity, Long>{

    @Query("SELECT * FROM reservation_fetives WHERE created_at = :date")
    fun findAllByDate(@Param("date") date: LocalDate): Flow<ReservationFestiveEntity>

    @Query(
        """
        SELECT * FROM reservation_fetives 
        WHERE EXTRACT(MONTH FROM created_at) = :month 
          AND EXTRACT(YEAR FROM created_at) = :year
    """
    )
     fun findAllByMonthAndYear(@Param("month") month: Int, @Param("year") year: Int): Flow<ReservationFestiveEntity>

     @Query("SELECT * FROM reservation_fetives  WHERE EXTRACT(YEAR FROM created_at) = :year")
    fun findAllByYear(@Param("year") year: Int): Flow<ReservationFestiveEntity>?

    @Query("SELECT * FROM reservation_fetives  WHERE status = :status")
     fun findAllByStatus(@Param("status") staus: ReservationStatus): Flow<ReservationFestiveEntity>?

    @Query("SELECT * FROM reservation_fetives WHERE start_date >= :startDate AND end_date <= :endDate")
     fun findAllInInterval(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): Flow<ReservationFestiveEntity> ?

    @Query("SELECT * FROM reservation_fetives  WHERE start_date = :startDate AND end_date = :endDate AND festive_id = :festiveId")
    fun findByStartDateAndEndDateProperty(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        @Param("festiveId") festiveId: Long
    ): Flow<ReservationFestiveEntity>?

    @Query("SELECT * FROM reservation_fetives  WHERE user_id = :userId")
     fun findByUser(@Param("userId") userId: Long): Flow<ReservationFestiveEntity>?

    @Query("SELECT * FROM reservation_fetives WHERE festive_id = :festiveId")
     fun findByProperty(@Param("festiveId") festiveId: Long): Flow<ReservationFestiveEntity>

     @Query("SELECT * FROM reservation_fetives WHERE festive_id = :festiveId AND user_id = :userId")
    fun findByUserProperty(@Param("festiveId") festiveId: Long,
                           @Param("userId") userId: Long): Flow<ReservationFestiveEntity>

    @Query("""
        SELECT r.* 
        FROM reservation_fetives r
        JOIN properties p ON r.festive_id = p.id
        WHERE p.user_id = :userId
    """)
    fun findByHostUserId(@Param("userId") userId: Long): Flow<ReservationFestiveEntity>

    //use with @Transactional when you call it
    @Modifying
    @Query("UPDATE reservation_fetives SET status = :status WHERE id = :id")
     fun updateStatusById(@Param("id") id: Long, @Param("status") status: ReservationStatus): Mono<Int>

    @Modifying
    @Query("UPDATE reservation_fetives SET is_active = :isActive, status = :status, cancellation_reason = :reason WHERE id = :id")
     fun cancelOrKeepReservation(@Param("id") id: Long,
                                 @Param("isActive") isActive: Boolean,
                                 @Param("reason") reason: String?,
                                 @Param("status") status: ReservationStatus): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_fetives WHERE id = :id")
     fun deleteByIdReservation(@Param("id") id: Long): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_fetives WHERE user_id = :userId")
    fun deleteAllByUserReservation(@Param("userId") userId: Long): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_fetives WHERE festive_id = :festiveId")
    fun deleteAllByPropertyReservation(@Param("festiveId") festiveId: Long): Mono<Int>
}
