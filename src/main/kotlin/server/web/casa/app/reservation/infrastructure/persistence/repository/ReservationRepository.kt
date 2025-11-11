package server.web.casa.app.reservation.infrastructure.persistence.repository

import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationEntity
import org.springframework.data.jpa.repository.*
import org.springframework.data.repository.query.Param
import jakarta.transaction.Transactional
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import java.time.LocalDate

interface ReservationRepository : JpaRepository<ReservationEntity, Long>{

    @Query("SELECT r FROM ReservationEntity r WHERE r.createdAt = :date")
    fun findAllByDate(@Param("date") date: LocalDate): List<ReservationEntity>

    @Query(
        """
        SELECT * FROM reservations 
        WHERE EXTRACT(MONTH FROM created_at) = :month 
          AND EXTRACT(YEAR FROM created_at) = :year
    """,
        nativeQuery = true
    )
     fun findAllByMonthAndYear(@Param("month") month: Int, @Param("year") year: Int): List<ReservationEntity>?

     @Query("SELECT r FROM ReservationEntity r WHERE EXTRACT(YEAR FROM r.createdAt) = :year")
    fun findAllByYear(@Param("year") year: Int): List<ReservationEntity>?

    @Query("SELECT r FROM ReservationEntity r WHERE r.status = :status")
     fun findAllByStatus(@Param("status") staus: ReservationStatus): List<ReservationEntity>?

    @Query("SELECT r FROM ReservationEntity r WHERE r.startDate <= :endDate AND r.endDate >= :startDate")
     fun findAllInInterval(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): List<ReservationEntity> ?

    @Query("SELECT r FROM ReservationEntity r WHERE r.startDate = :endDate AND r.endDate = :startDate AND r.property = :property")
    fun findByStartDateAndEndDateProperty(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate,
        @Param("property") property: PropertyEntity
    ): List<ReservationEntity> ?

    @Query("SELECT r FROM ReservationEntity r WHERE r.user = :user")
     fun findByUser(@Param("user") user: UserEntity): List<ReservationEntity>?

    @Query("SELECT r FROM ReservationEntity r WHERE r.property = :property")
     fun findByProperty(@Param("property") property: PropertyEntity): List<ReservationEntity> ?

     @Query("SELECT r FROM ReservationEntity r WHERE r.property = :property AND r.user = :user")
    fun findByUserProperty(@Param("property") property: PropertyEntity, @Param("user") user: UserEntity): List<ReservationEntity> ?

    //use with @Transactional when you call it
    @Transactional
    @Modifying
    @Query("UPDATE ReservationEntity r SET r.status = :status WHERE r.id = :id")
     fun updateStatusById(@Param("id") id: Long, @Param("status") status: ReservationStatus): Int

    @Transactional
    @Modifying
    @Query("UPDATE ReservationEntity r SET r.isActive = :isActive, r.status = :status, r.cancellationReason = :reason WHERE r.id = :id")
     fun cancelOrKeepReservation(@Param("id") id: Long,
                                 @Param("isActive") isActive: Boolean,
                                 @Param("reason") reason: String?,
                                 @Param("status") status: ReservationStatus): Int

     @Transactional
     @Modifying
     @Query("DELETE FROM ReservationEntity r WHERE r.id = :id")
     fun deleteByIdReservation(@Param("id") id: Long): Int

    @Transactional
    @Modifying
    @Query("DELETE FROM ReservationEntity r WHERE r.user = :user")
    fun deleteAllByUserReservation(@Param("user") user: UserEntity): Int

    @Transactional
    @Modifying
    @Query("DELETE FROM ReservationEntity r WHERE r.property = :property")
    fun deleteAllByPropertyReservation(@Param("property") property: PropertyEntity): Int
}
