package server.web.casa.app.property.application.service.favorite

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import server.web.casa.app.property.application.service.VacanceService
import server.web.casa.app.property.domain.model.favorite.FavoriteVacanceDTO
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteVacanceEntity
import server.web.casa.app.property.infrastructure.persistence.repository.favorite.FavoriteVacanceRepository
import server.web.casa.app.user.application.service.UserService

class FavoriteVacanceService(
    private val repository: FavoriteVacanceRepository,
    private val userS: UserService,
    private val vacanceS: VacanceService
) {
    suspend fun create(f : FavoriteVacanceEntity): FavoriteVacanceDTO {
        val result = repository.save(f)
        return toFavoriteDTO(result)
    }
    suspend fun getById(id: Long): FavoriteVacanceDTO? {
        val fav = repository.findById(id) ?: return null
        return toFavoriteDTO(fav)
    }
    suspend fun getAll() = repository.findAll().map {
        toFavoriteDTO(it)
    }.toList()

    suspend fun getUserFavorite(user: Long) : List<FavoriteVacanceDTO>{
        return repository.findFavoriteByUserId(user)?.map {
            toFavoriteDTO(it)
        }?.toList() ?: emptyList()
    }

    suspend fun getFavoriteByVacanceId( vac: Long ) : List<FavoriteVacanceDTO>{
        return repository.findFavoriteByVacance(vac).let {list-> list?.map{
            toFavoriteDTO(it)
        }?.toList() ?: emptyList() }
    }
    suspend fun getFavoriteIfExist( vac: Long , user: Long) : List<FavoriteVacanceDTO>{
        return repository.findFavoriteExist(vac, user).let{list-> list?.map{toFavoriteDTO(it)}?.toList() ?: emptyList() }
    }
    suspend fun deleteById(favoriteId: Long) {
        return repository.deleteById(favoriteId)
    }
    suspend fun deleteAll() {
        return repository.deleteAll()
    }
    suspend fun deleteAllFavoriteUser(user: Long): Boolean {
        val getAll = repository.findAll().filter{ it.userId == user }.toList()
        for (entity in getAll) {
            repository.delete(entity)
        }
        return true
    }

    suspend fun toFavoriteDTO(it: FavoriteVacanceEntity): FavoriteVacanceDTO =
        FavoriteVacanceDTO(
            favorite = it,
            user = userS.findIdUser(it.userId),
            //vacance = vacanceS.findById(it.vacanceId)
        )

}