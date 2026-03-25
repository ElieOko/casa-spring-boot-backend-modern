package server.web.casa.app.property.application.service.favorite

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.property.application.service.BureauService
import server.web.casa.app.property.domain.model.Bureau
import server.web.casa.app.property.domain.model.favorite.FavoriteBureauDTO
import server.web.casa.app.property.infrastructure.persistence.entity.BureauImageEntity
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteBureauEntity
import server.web.casa.app.property.infrastructure.persistence.repository.BureauRepository
import server.web.casa.app.property.infrastructure.persistence.repository.favorite.FavoriteBureauRepository
import server.web.casa.app.user.application.service.UserService

@Service
class FavoriteBureauService (
    private val repository: FavoriteBureauRepository,
    private val userS: UserService,
    private val brxS: BureauService,
    private val brxR: BureauRepository
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

    suspend fun getFavoriteByBureauId( bureauId: Long ) : List<FavoriteBureauDTO>{
        return repository.findFavoriteByBureauId(bureauId).let {list-> list?.map{
            toFavoriteDTO(it)
        }?.toList() ?: emptyList() }
    }
    suspend fun getFavoriteIfExist(bureauId: Long, user: Long): FavoriteBureauDTO? {
        val fav: FavoriteBureauEntity? = repository.findFavoriteExist(bureauId, user)?.firstOrNull()
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

    suspend fun toFavoriteDTO(it: FavoriteBureauEntity): FavoriteBureauDTO {
        val checkProperty = brxR.findById(it.bureauId)
        var dto: Bureau? = null
        var img: List<BureauImageEntity>?= null
        if(checkProperty != null){
            dto = brxS.findById(it.bureauId)
            img = brxS.getImageByBureauID(it.bureauId)
        }
       return FavoriteBureauDTO(
           favorite = it,
           user = userS.findIdUser(it.userId),
           bureau = dto,
           bureauImage = img
       )
    }
}