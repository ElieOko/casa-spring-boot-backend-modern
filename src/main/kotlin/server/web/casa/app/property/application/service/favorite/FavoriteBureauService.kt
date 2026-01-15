package server.web.casa.app.property.application.service.favorite

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.property.application.service.BureauService
import server.web.casa.app.property.domain.model.favorite.FavoriteBureauDTO
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteBureauEntity
import server.web.casa.app.property.infrastructure.persistence.repository.favorite.FavoriteBureauRepository
import server.web.casa.app.user.application.service.UserService

@Service
class FavoriteBureauService (
    private val repository: FavoriteBureauRepository,
    private val userS: UserService,
    private val brxS: BureauService
) {
    suspend fun create(f : FavoriteBureauEntity): FavoriteBureauDTO {
        val result = repository.save(f)
        return toFavoriteDTO(result)
    }
    suspend fun getById(id: Long): FavoriteBureauDTO? {
        val fav = repository.findById(id) ?: return null
        return toFavoriteDTO(fav)
    }
    suspend fun getAll() = repository.findAll().map {
        toFavoriteDTO(it)
    }.toList()

    suspend fun getUserFavorite(user: Long) : List<FavoriteBureauDTO>{
        return repository.findFavoriteByUserId(user)?.map {
            toFavoriteDTO(it)
        }?.toList() ?: emptyList()
    }

    suspend fun getFavoriteByFestId( festId: Long ) : List<FavoriteBureauDTO>{
        return repository.findFavoriteByFestId(festId).let {list-> list?.map{
            toFavoriteDTO(it)
        }?.toList() ?: emptyList() }
    }
    suspend fun getFavoriteIfExist( festId: Long , user: Long) : List<FavoriteBureauDTO>{
        return repository.findFavoriteExist(festId, user).let{list-> list?.map{toFavoriteDTO(it)}?.toList() ?: emptyList() }
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

    suspend fun toFavoriteDTO(it: FavoriteBureauEntity): FavoriteBureauDTO =
        FavoriteBureauDTO(
            favorite = it,
            user = userS.findIdUser(it.userId),
            bureau = brxS.findById(it.bureauId)
        )

}