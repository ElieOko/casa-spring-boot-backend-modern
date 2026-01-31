package server.web.casa.app.reservation.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import reactor.core.publisher.Mono
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationFuneraireEntity
import java.time.LocalDate

interface ReservationFuneraireRepository : CoroutineCrudRepository<ReservationFuneraireEntity, Long>{

    @Query("SELECT * FROM reservation_funeraires WHERE created_at = :date")
    fun findAllByDate(@Param("date") date: LocalDate): Flow<ReservationFuneraireEntity>

    @Query(
        """
        SELECT * FROM reservation_funeraires 
        WHERE EXTRACT(MONTH FROM created_at) = :month 
          AND EXTRACT(YEAR FROM created_at) = :year
    """
    )
     fun findAllByMonthAndYear(@Param("month") month: Int, @Param("year") year: Int): Flow<ReservationFuneraireEntity>

     @Query("SELECT * FROM reservation_funeraires  WHERE EXTRACT(YEAR FROM created_at) = :year")
    fun findAllByYear(@Param("year") year: Int): Flow<ReservationFuneraireEntity>?

    @Query("SELECT * FROM reservation_funeraires  WHERE status = :status")
     fun findAllByStatus(@Param("status") staus: ReservationStatus): Flow<ReservationFuneraireEntity>?

    @Query("SELECT * FROM reservation_funeraires WHERE start_date >= :startDate AND end_date <= :endDate")
     fun findAllInInterval(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): Flow<ReservationFuneraireEntity> ?

    @Query("SELECT * FROM reservation_funeraires  WHERE start_date = :startDate AND end_date = :endDate AND funeraire_id = :funeraireId")
    fun findByStartDateAndEndDateProperty(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        @Param("funeraireId") funeraireId: Long
    ): Flow<ReservationFuneraireEntity>?

    @Query("SELECT * FROM reservation_funeraires  WHERE user_id = :userId")
     fun findByUser(@Param("userId") userId: Long): Flow<ReservationFuneraireEntity>?

    @Query("SELECT * FROM reservation_funeraires WHERE funeraire_id = :funeraireId")
     fun findByProperty(@Param("funeraireId") funeraireId: Long): Flow<ReservationFuneraireEntity>

     @Query("SELECT * FROM reservation_funeraires WHERE funeraire_id = :funeraireId AND user_id = :userId")
    fun findByUserProperty(@Param("funeraireId") funeraireId: Long,
                           @Param("userId") userId: Long): Flow<ReservationFuneraireEntity>

    @Query("""
        SELECT r.* 
        FROM reservation_funeraires r
        JOIN funeraires p ON r.funeraire_id = p.id
        WHERE p.user_id = :userId
    """)
    fun findByHostUserId(@Param("userId") userId: Long): Flow<ReservationFuneraireEntity>

    //use with @Transactional when you call it
    @Modifying
    @Query("UPDATE reservation_funeraires SET status = :status WHERE id = :id")
     fun updateStatusById(@Param("id") id: Long, @Param("status") status: ReservationStatus): Mono<Int>

    @Modifying
    @Query("UPDATE reservation_funeraires SET is_active = :isActive, status = :status, cancellation_reason = :reason WHERE id = :id")
     fun cancelOrKeepReservation(@Param("id") id: Long,
                                 @Param("isActive") isActive: Boolean,
                                 @Param("reason") reason: String?,
                                 @Param("status") status: ReservationStatus): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_funeraires WHERE id = :id")
     fun deleteByIdReservation(@Param("id") id: Long): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_funeraires WHERE user_id = :userId")
    fun deleteAllByUserReservation(@Param("userId") userId: Long): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_funeraires WHERE funeraire_id = :funeraireId")
    fun deleteAllByPropertyReservation(@Param("funeraireId") funeraireId: Long): Mono<Int>
}
