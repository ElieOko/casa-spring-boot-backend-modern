package server.web.casa.app.prestation.application

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.ecosystem.application.service.PrestationService
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.toDomain
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.prestation.domain.model.SollicitationDTO
import server.web.casa.app.prestation.infrastructure.persistance.entity.SollicitationEntity
import server.web.casa.app.prestation.infrastructure.persistance.repository.SollicitationRepository
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.user.application.service.UserService

@Service
class SollicitationService(
    private val p: SollicitationRepository,
    private val userS: UserService,
    private  val devs: DeviseService,
    private val prestS: PrestationService
) {

    suspend fun create(s: SollicitationEntity): SollicitationDTO{
        val entity = p.save(s)
        return toDto(entity)
    }
    suspend fun findAll() = p.findAll().map{toDto(it)}.toList()

    suspend fun findById(id: Long): SollicitationDTO?{
        val entity = p.findById(id) ?: return null
        return toDto(entity)
    }
    suspend fun findByUserId(id: Long): List<SollicitationDTO>?{
        val entity = p.findByUserId(id)
        return entity?.map{toDto(it)}?.toList()
    }

    suspend fun updateStatus(entity: SollicitationEntity, status: ReservationStatus): SollicitationDTO?{
            entity.status = status.toString()
            p.save(entity)
        return toDto( p.findById(entity.id!!)!!)
    }

    suspend fun deleteById(id: Long): Boolean {
        val entity = p.deleteById(id)
        return true
    }
    suspend fun toDto(it: SollicitationEntity) = SollicitationDTO(
        sollicitation = it,
        user = userS.findIdUser(it.userId!!),
        devise = devs.getById(it.deviseId)!!,
        prestation = prestS.getById(it.prestationId!!)!!.toDomain()
    )
}