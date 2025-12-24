package server.web.casa.app.prestation.application

import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.prestation.infrastructure.persistance.entity.SollicitationEntity
import server.web.casa.app.prestation.infrastructure.persistance.repository.SollicitationRepository
import server.web.casa.app.reservation.domain.model.ReservationStatus

@Service
class SollicitationService(
    private val p: SollicitationRepository
) {
    suspend fun create(s: SollicitationEntity): SollicitationEntity{
        val entity = p.save(s)
        return entity
    }
    suspend fun findAll(): List<SollicitationEntity>{
        val entity = p.findAll()
        return entity.toList()
    }

    suspend fun findById(id: Long): SollicitationEntity?{
        val entity = p.findById(id)
        return entity
    }
    suspend fun updateStatus(entity: SollicitationEntity, status: ReservationStatus): SollicitationEntity?{
            entity.status = status.toString()
            p.save(entity)
        return p.findById(entity.id!!)
    }

    suspend fun deleteById(id: Long): Boolean {
        val entity = p.deleteById(id)
        return true
    }
}