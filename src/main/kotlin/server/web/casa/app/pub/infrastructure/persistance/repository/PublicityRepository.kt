package server.web.casa.app.pub.infrastructure.persistance.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.threeten.bp.LocalTime
import server.web.casa.app.pub.infrastructure.persistance.entity.PublicityEntity
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationEntity
import java.time.LocalDate

interface PublicityRepository : CoroutineCrudRepository<PublicityEntity, Long>
{
    @Query("SELECT * FROM publicities  WHERE user_id = :userId")
    fun findByUserId(@Param("userId") userId: Long): Flow<PublicityEntity>?

    @Query("SELECT * FROM publicities  WHERE created_at = :createdAt")
    fun findByCreated(@Param("createdAt") createdAt: LocalDate): Flow<PublicityEntity>?

}
