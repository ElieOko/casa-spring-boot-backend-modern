package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.TerrainEntity
import server.web.casa.app.property.infrastructure.persistence.entity.VacanceEntity

interface VacanceRepository : CoroutineCrudRepository<VacanceEntity, Long>{
    @Query("""SELECT * FROM vacances""")
    fun findAllData(): Flow<VacanceEntity>

    @Query("""
        SELECT * FROM vacances
        WHERE is_available = true
    """)
    override fun findAll(): Flow<VacanceEntity>

    @Query("SELECT * FROM vacances WHERE agence_id = :agenceId")
    suspend fun getAllByAgence(agenceId : Long) : Flow<VacanceEntity?>

    @Query("""SELECT * FROM vacances WHERE id = :id AND is_available =true""")
    override suspend fun findById(id : Long): VacanceEntity?
    @Query("""SELECT * FROM vacances WHERE id = :id""")
    suspend fun findByIdNoRestrict(id : Long): VacanceEntity?

}