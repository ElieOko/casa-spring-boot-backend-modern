package server.web.casa.app.property.application.service.favorite

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.property.application.service.SalleFuneraireService
import server.web.casa.app.property.domain.model.SalleFestive
import server.web.casa.app.property.domain.model.SalleFuneraire
import server.web.casa.app.property.domain.model.favorite.FavoriteFestiveDTO
import server.web.casa.app.property.domain.model.favorite.FavoriteFuneraireDTO
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteFestiveEntity
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteFuneraireEntity
import server.web.casa.app.property.infrastructure.persistence.repository.SalleFuneraireRepository
import server.web.casa.app.property.infrastructure.persistence.repository.favorite.FavoriteFuneraireRepository
import server.web.casa.app.user.application.service.UserService

@Service
class FavoriteFuneraireService(
    private val repository: FavoriteFuneraireRepository,
    private val userS: UserService,
    private val funeS: SalleFuneraireService,
    private val funeR: SalleFuneraireRepository
) {
    suspend fun create(f : FavoriteFuneraireEntity): FavoriteFuneraireDTO {
        val result = repository.save(f)
        return toFavoriteDTO(result)
    }
    suspend fun getById(id: Long): FavoriteFuneraireDTO? {
        val fav = repository.findById(id) ?: return null
        return toFavoriteDTO(fav)
    }
    suspend fun getAll() = repository.findAll().map {
        toFavoriteDTO(it)
    }.toList()

    suspend fun getUserFavorite(user: Long) : List<FavoriteFuneraireDTO>{
        return repository.findFavoriteByUserId(user)?.map {
            toFavoriteDTO(it)
        }?.toList() ?: emptyList()
    }

    suspend fun getFavoriteByFuneId( funeraireId: Long ) : List<FavoriteFuneraireDTO>{
        return repository.findFavoriteByFuneId(funeraireId).let {list-> list?.map{
            toFavoriteDTO(it)
        }?.toList() ?: emptyList() }
    }
    suspend fun getFavoriteIfExist(funeraireId: Long, user: Long): FavoriteFuneraireDTO? {
        val fav:  FavoriteFuneraireEntity? = repository.findFavoriteExist(funeraireId, user)?.firstOrNull()
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

    suspend fun toFavoriteDTO(it: FavoriteFuneraireEntity): FavoriteFuneraireDTO {
        val checkProperty = funeR.findById(it.funeraireId)
        var dto: SalleFuneraire? = null
        if(checkProperty != null){
            dto = funeS.findById(it.funeraireId)
        }
       return FavoriteFuneraireDTO(
            favorite = it,
            user = userS.findIdUser(it.userId),
            salle = funeS.findById(it.funeraireId)
        )
    }

}