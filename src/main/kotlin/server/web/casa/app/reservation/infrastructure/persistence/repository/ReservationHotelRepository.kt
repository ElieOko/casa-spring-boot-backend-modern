package server.web.casa.app.reservation.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import reactor.core.publisher.Mono
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationHotelEntity
import java.time.LocalDate

interface ReservationHotelRepository : CoroutineCrudRepository<ReservationHotelEntity, Long>{

    @Query("SELECT * FROM reservation_hotels WHERE created_at = :date")
    fun findAllByDate(@Param("date") date: LocalDate): Flow<ReservationHotelEntity>

    @Query(
        """
        SELECT * FROM reservation_hotels 
        WHERE EXTRACT(MONTH FROM created_at) = :month 
          AND EXTRACT(YEAR FROM created_at) = :year
    """
    )
     fun findAllByMonthAndYear(@Param("month") month: Int, @Param("year") year: Int): Flow<ReservationHotelEntity>

     @Query("SELECT * FROM reservation_hotels  WHERE EXTRACT(YEAR FROM created_at) = :year")
    fun findAllByYear(@Param("year") year: Int): Flow<ReservationHotelEntity>?

    @Query("SELECT * FROM reservation_hotels  WHERE status = :status")
     fun findAllByStatus(@Param("status") staus: ReservationStatus): Flow<ReservationHotelEntity>?

    @Query("SELECT * FROM reservation_hotels WHERE start_date >= :startDate AND end_date <= :endDate")
     fun findAllInInterval(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): Flow<ReservationHotelEntity> ?

    @Query("SELECT * FROM reservation_hotels  WHERE start_date = :startDate AND end_date = :endDate AND hotel_id = :hotelID")
    fun findByStartDateAndEndDateProperty(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        @Param("hotelID") hotelID: Long
    ): Flow<ReservationHotelEntity>?

    @Query("SELECT * FROM reservation_hotels  WHERE user_id = :userId")
     fun findByUser(@Param("userId") userId: Long): Flow<ReservationHotelEntity>?

    @Query("SELECT * FROM reservation_hotels WHERE hotel_id = :hotelID")
     fun findByProperty(@Param("hotelID") hotelID: Long): Flow<ReservationHotelEntity>

     @Query("SELECT * FROM reservation_hotels WHERE hotel_id = :hotelID AND user_id = :userId")
    fun findByUserProperty(@Param("hotelID") hotelID: Long,
                           @Param("userId") userId: Long): Flow<ReservationHotelEntity>

    @Query("""
        SELECT r.* 
        FROM reservation_hotels r
        JOIN hotels p ON r.hotel_id = p.id
        WHERE p.user_id = :userId
    """)
    fun findByHostUserId(@Param("userId") userId: Long): Flow<ReservationHotelEntity>

    //use with @Transactional when you call it
    @Modifying
    @Query("UPDATE reservation_hotels SET status = :status WHERE id = :id")
     fun updateStatusById(@Param("id") id: Long, @Param("status") status: ReservationStatus): Mono<Int>

    @Modifying
    @Query("UPDATE reservation_hotels SET is_active = :isActive, status = :status, cancellation_reason = :reason WHERE id = :id")
     fun cancelOrKeepReservation(@Param("id") id: Long,
                                 @Param("isActive") isActive: Boolean,
                                 @Param("reason") reason: String?,
                                 @Param("status") status: ReservationStatus): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_hotels WHERE id = :id")
     fun deleteByIdReservation(@Param("id") id: Long): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_hotels WHERE user_id = :userId")
    fun deleteAllByUserReservation(@Param("userId") userId: Long): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_hotels WHERE hotel_id = :hotelID")
    fun deleteAllByPropertyReservation(@Param("hotelID") hotelID: Long): Mono<Int>
}
