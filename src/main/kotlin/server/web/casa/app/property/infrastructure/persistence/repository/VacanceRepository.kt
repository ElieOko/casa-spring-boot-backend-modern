package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.AgenceEntity
import server.web.casa.app.property.infrastructure.persistence.entity.VacanceEntity

interface VacanceRepository : CoroutineCrudRepository<VacanceEntity, Long>{
    @Query("SELECT * FROM vacances WHERE agence_id = :agenceId")
    suspend fun getAllByAgence(agenceId : Long) : Flow<VacanceEntity?>

}