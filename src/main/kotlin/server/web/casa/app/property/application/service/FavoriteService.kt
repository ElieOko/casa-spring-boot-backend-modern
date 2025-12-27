package server.web.casa.app.property.application.service

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.Favorite
import server.web.casa.app.property.domain.model.FavoriteDTO
import server.web.casa.app.property.infrastructure.persistence.entity.FavoriteEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.property.infrastructure.persistence.mapper.*
import server.web.casa.app.property.infrastructure.persistence.repository.FavoriteRepository
import server.web.casa.app.reservation.domain.model.ReservationDTO
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import kotlin.collections.map

@Service
class FavoriteService(
    private val repository: FavoriteRepository,
    private val propS: PropertyService
) {
    suspend fun create(f : FavoriteEntity): FavoriteDTO {
        //val data = f.toEntity()
        val result = repository.save(f)
        return toFavoriteDTO(result)
    }
    suspend fun getAll() = repository.findAll().map {
        toFavoriteDTO(it)
    }.toList()

    suspend fun getUserFavoriteProperty(user: Long) : List<FavoriteDTO>?{
        return repository.findFavoriteByPropertyUser(user)?.map {
               toFavoriteDTO(it)
        }?.toList() ?: emptyList()
    }

    suspend fun getOneFavoritePropertyCount( property: Long ) : List<FavoriteDTO>?{
        return repository.findOneFavoritePropertyCount(property).let {list-> list?.map{
            toFavoriteDTO(it)
        }?.toList() ?: emptyList() }
    }
    suspend fun getFavoriteIfExist( property: Long , user: Long) : List<FavoriteDTO>?{
        return repository.findFavoriteExist(property, user).let{list-> list?.map{toFavoriteDTO(it)}?.toList() ?: emptyList() }
    }
    suspend fun deleteById(favoriteId: Long) {
        return repository.deleteById(favoriteId)
    }
    suspend fun deleteAllFavoriteUser(user: Long): Boolean {
       val getAll = repository.findAll().filter{ it.userId == user }.toList()
        for (entity in getAll) {
            repository.delete(entity) // delete attend FavoriteEntity
        }
        return true
    }

    suspend fun toFavoriteDTO(it: FavoriteEntity): FavoriteDTO =
        FavoriteDTO(
            favorite = it,
            property = propS.findByIdProperty(it.propertyId!!).first
        )

}