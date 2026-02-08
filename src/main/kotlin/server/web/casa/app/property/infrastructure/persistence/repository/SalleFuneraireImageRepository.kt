package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.SalleFestiveImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.SalleFuneraireImageEntity

interface SalleFuneraireImageRepository : CoroutineCrudRepository<SalleFuneraireImageEntity, Long>{
    fun findBySalleFuneraireIdIn(salleFuneraireIds: List<Long>): Flow<SalleFuneraireImageEntity>

    @Query("SELECT * FROM salle_funeraire_images  WHERE salle_funeraire_id = :salleId")
    fun findAllBySalleID(@Param("salleId") salleId: Long): Flow<SalleFuneraireImageEntity>

}