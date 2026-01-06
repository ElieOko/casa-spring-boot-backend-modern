package server.web.casa.app.prestation.application

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.ecosystem.application.service.PrestationService
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.toDomain
import server.web.casa.app.prestation.domain.model.FavoritePrestationDTO
import server.web.casa.app.prestation.infrastructure.persistance.entity.FavoritePrestationEntity
import server.web.casa.app.prestation.infrastructure.persistance.repository.FavoritePrestationRepository
import server.web.casa.app.user.application.service.UserService

@Service
class FavoritePrestationService(
    private val repo: FavoritePrestationRepository,
    private val userS: UserService,
    private val prestS: PrestationService
) {
    suspend fun toDTO(it: FavoritePrestationEntity) = FavoritePrestationDTO(
        favorite = it,
        user = userS.findIdUser(it.userId!!),
        prestation = prestS.getById(it.prestationId!!)!!.toDomain()
    )

    suspend fun create(f: FavoritePrestationEntity): FavoritePrestationDTO{
        val create = repo.save(f)
        return toDTO(create)
    }

    suspend fun findAll() = repo.findAll().map { toDTO(it) }.toList()

    suspend fun findById(id: Long): FavoritePrestationDTO? {
       val it = repo.findById(id) ?: return null
        return toDTO(it)
    }

    suspend fun findByUserId(userId: Long): List<FavoritePrestationDTO>? {
        val favorite = repo.findFavoriteByUserId(userId) ?: return null
        return favorite.map { toDTO(it) }.toList()
    }

    suspend fun findByPrestationId(prestationId: Long): List<FavoritePrestationDTO>? {
        val favorite = repo.findFavoriteByPrestationId(prestationId) ?: return null
        return favorite.map { toDTO(it) }.toList()
    }
    suspend fun findByPrestationIdAndUserId(prestationId: Long, userId: Long): List<FavoritePrestationDTO>? {
        val favorite = repo.findFavoriteByPrestationIdAndUserI(
            prestationId,
            userId
        ) ?: return null
        return favorite.map { toDTO(it) }.toList()
    }

    suspend fun deleteById(id: Long) = repo.deleteById(id)

    suspend fun deleteAll() = repo.deleteAll()
}