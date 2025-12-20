package server.web.casa.app.property.application.service

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.Favorite
import server.web.casa.app.property.infrastructure.persistence.entity.FavoriteEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.property.infrastructure.persistence.mapper.*
import server.web.casa.app.property.infrastructure.persistence.repository.FavoriteRepository
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import kotlin.collections.map

@Service
class FavoriteService(
    private val repository: FavoriteRepository,
) {
    suspend fun create(f : FavoriteEntity): FavoriteEntity {
        //val data = f.toEntity()
        val result = repository.save(f)
        return result
    }
    suspend fun getAll() = repository.findAll().map { it }.toList()

    suspend fun getUserFavoriteProperty(user: Long) : List<FavoriteEntity>?{
        return repository.findFavoriteByPropertyUser(user)?.map { it }?.toList() ?: emptyList()
    }

    suspend fun getOneFavoritePropertyCount( property: Long ) : List<FavoriteEntity>?{
        return repository.findOneFavoritePropertyCount(property).let {list-> list?.map{it}?.toList() ?: emptyList() }
    }
    suspend fun getFavoriteIfExist( property: Long , user: Long) : List<FavoriteEntity>?{
        return repository.findFavoriteExist(property, user).let{list-> list?.map{it}?.toList() ?: emptyList() }
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
}