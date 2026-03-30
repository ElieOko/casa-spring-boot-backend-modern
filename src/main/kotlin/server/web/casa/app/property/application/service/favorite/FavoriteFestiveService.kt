package server.web.casa.app.property.application.service.favorite

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.property.application.service.SalleFestiveService
import server.web.casa.app.property.domain.model.Bureau
import server.web.casa.app.property.domain.model.SalleFestive
import server.web.casa.app.property.domain.model.favorite.FavoriteBureauDTO
import server.web.casa.app.property.domain.model.favorite.FavoriteFestiveDTO
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteBureauEntity
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteFestiveEntity
import server.web.casa.app.property.infrastructure.persistence.repository.SalleFestiveRepository
import server.web.casa.app.property.infrastructure.persistence.repository.favorite.FavoriteFestiveRepository
import server.web.casa.app.user.application.service.UserService

@Service
class FavoriteFestiveService (
    private val repository: FavoriteFestiveRepository,
    private val userS: UserService,
    private val festS: SalleFestiveService,
    private val festR : SalleFestiveRepository
) {
    suspend fun create(f : FavoriteFestiveEntity): FavoriteFestiveDTO {
        val result = repository.save(f)
        return toFavoriteDTO(result)
    }
    suspend fun getById(id: Long): FavoriteFestiveDTO? {
        val fav = repository.findById(id) ?: return null
        return toFavoriteDTO(fav)
    }
    suspend fun getAll() = repository.findAll().map {
        toFavoriteDTO(it)
    }.toList()

    suspend fun getUserFavorite(user: Long) : List<FavoriteFestiveDTO>{
        return repository.findFavoriteByUserId(user)?.map {
            toFavoriteDTO(it)
        }?.toList() ?: emptyList()
    }

    suspend fun getFavoriteByFestId( festId: Long ) : List<FavoriteFestiveDTO>{
        return repository.findFavoriteByFestId(festId).let {list-> list?.map{
            toFavoriteDTO(it)
        }?.toList() ?: emptyList() }
    }
    suspend fun getFavoriteIfExist(festId: Long, user: Long): FavoriteFestiveDTO? {
        val fav:  FavoriteFestiveEntity? = repository.findFavoriteExist(festId, user)?.firstOrNull()
        return fav?.let { toFavoriteDTO(it) }
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

    suspend fun toFavoriteDTO(it: FavoriteFestiveEntity): FavoriteFestiveDTO {
        val checkProperty = festR.findById(it.festiveId)
        var dto: SalleFestive? = null
        if(checkProperty != null){
            dto = festS.findById(it.festiveId)
        }
       return FavoriteFestiveDTO(
            favorite = it,
            user = userS.findIdUser(it.userId),
            salle = dto
        )
    }

}