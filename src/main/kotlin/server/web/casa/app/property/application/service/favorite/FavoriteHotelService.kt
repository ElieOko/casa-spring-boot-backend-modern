package server.web.casa.app.property.application.service.favorite

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import server.web.casa.app.property.application.service.HotelService
import server.web.casa.app.property.domain.model.favorite.FavoriteHotelDTO
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteHotelEntity
import server.web.casa.app.property.infrastructure.persistence.repository.favorite.FavoriteHotelRepository
import server.web.casa.app.user.application.service.UserService

class FavoriteHotelService(
    private val repository: FavoriteHotelRepository,
    private val userS: UserService,
    private val hotS: HotelService
) {
    suspend fun create(f : FavoriteHotelEntity): FavoriteHotelDTO {
        val result = repository.save(f)
        return toFavoriteDTO(result)
    }
    suspend fun getById(id: Long): FavoriteHotelDTO? {
        val fav = repository.findById(id) ?: return null
        return toFavoriteDTO(fav)
    }
    suspend fun getAll() = repository.findAll().map {
        toFavoriteDTO(it)
    }.toList()

    suspend fun getUserFavorite(user: Long) : List<FavoriteHotelDTO>{
        return repository.findFavoriteByUserId(user)?.map {
            toFavoriteDTO(it)
        }?.toList() ?: emptyList()
    }

    suspend fun getFavoriteByHotel( hotelId: Long ) : List<FavoriteHotelDTO>{
        return repository.findFavoriteByHotel(hotelId).let {list-> list?.map{
            toFavoriteDTO(it)
        }?.toList() ?: emptyList() }
    }
    suspend fun getFavoriteIfExist( hotelId: Long , user: Long) : List<FavoriteHotelDTO>{
        return repository.findFavoriteExist(hotelId, user).let{list-> list?.map{toFavoriteDTO(it)}?.toList() ?: emptyList() }
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

    suspend fun toFavoriteDTO(it: FavoriteHotelEntity): FavoriteHotelDTO =
        FavoriteHotelDTO(
            favorite = it,
            user = userS.findIdUser(it.userId),
            //hotel = hotS.findById(it.hotelId)
        )

}