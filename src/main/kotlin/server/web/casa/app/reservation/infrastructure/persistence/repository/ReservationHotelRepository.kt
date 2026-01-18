package server.web.casa.app.reservation.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import reactor.core.publisher.Mono
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationChambreHotelEntity
import java.time.LocalDate

interface ReservationHotelRepository : CoroutineCrudRepository<ReservationChambreHotelEntity, Long>{

    @Query("SELECT * FROM reservation_hotel_chambres WHERE created_at = :date")
    fun findAllByDate(@Param("date") date: LocalDate): Flow<ReservationChambreHotelEntity>

    @Query(
        """
        SELECT * FROM reservation_hotel_chambres 
        WHERE EXTRACT(MONTH FROM created_at) = :month 
          AND EXTRACT(YEAR FROM created_at) = :year
    """
    )
     fun findAllByMonthAndYear(@Param("month") month: Int, @Param("year") year: Int): Flow<ReservationChambreHotelEntity>

     @Query("SELECT * FROM reservation_hotel_chambres  WHERE EXTRACT(YEAR FROM created_at) = :year")
    fun findAllByYear(@Param("year") year: Int): Flow<ReservationChambreHotelEntity>?

    @Query("SELECT * FROM reservation_hotel_chambres  WHERE status = :status")
     fun findAllByStatus(@Param("status") staus: ReservationStatus): Flow<ReservationChambreHotelEntity>?

    @Query("SELECT * FROM reservation_hotel_chambres WHERE start_date >= :startDate AND end_date <= :endDate")
     fun findAllInInterval(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): Flow<ReservationChambreHotelEntity> ?

    @Query("SELECT * FROM reservation_hotel_chambres  WHERE start_date = :startDate AND end_date = :endDate AND hotel_chambre_id = :hotelID")
    fun findByStartDateAndEndDateProperty(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        @Param("hotelID") hotelID: Long
    ): Flow<ReservationChambreHotelEntity>?

    @Query("SELECT * FROM reservation_hotel_chambres  WHERE user_id = :userId")
     fun findByUser(@Param("userId") userId: Long): Flow<ReservationChambreHotelEntity>?

    @Query("SELECT * FROM reservation_hotel_chambres WHERE hotel_chambre_id = :hotelID")
     fun findByProperty(@Param("hotelID") hotelID: Long): Flow<ReservationChambreHotelEntity>

     @Query("SELECT * FROM reservation_hotel_chambres WHERE hotel_chambre_id = :hotelID AND user_id = :userId")
    fun findByUserProperty(@Param("hotelID") hotelID: Long,
                           @Param("userId") userId: Long): Flow<ReservationChambreHotelEntity>
/*ce query est correct en spring boot CoroutineCrudRepository ??*/
    @Query("""
        SELECT r.* 
        FROM reservation_hotel_chambres r
        JOIN hotel_chambres p ON r.hotel_chambre_id = p.id
        JOIN hotels h ON p.hotel_id = h.id
        WHERE h.user_id = :userId
    """)
    fun findByHostUserId(@Param("userId") userId: Long): Flow<ReservationChambreHotelEntity>

    //use with @Transactional when you call it
    @Modifying
    @Query("UPDATE reservation_hotel_chambres SET status = :status WHERE id = :id")
     fun updateStatusById(@Param("id") id: Long, @Param("status") status: ReservationStatus): Mono<Int>

    @Modifying
    @Query("UPDATE reservation_hotel_chambres SET is_active = :isActive, status = :status, cancellation_reason = :reason WHERE id = :id")
     fun cancelOrKeepReservation(@Param("id") id: Long,
                                 @Param("isActive") isActive: Boolean,
                                 @Param("reason") reason: String?,
                                 @Param("status") status: ReservationStatus): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_hotel_chambres WHERE id = :id")
     fun deleteByIdReservation(@Param("id") id: Long): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_hotel_chambres WHERE user_id = :userId")
    fun deleteAllByUserReservation(@Param("userId") userId: Long): Mono<Int>

    @Modifying
    @Query("DELETE FROM reservation_hotel_chambres WHERE hotel_chambre_id = :hotelID")
    fun deleteAllByPropertyReservation(@Param("hotelID") hotelID: Long): Mono<Int>
}
