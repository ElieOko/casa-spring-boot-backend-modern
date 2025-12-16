package server.web.casa.app.property.application.service

import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.Favorite
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.property.infrastructure.persistence.mapper.*
import server.web.casa.app.property.infrastructure.persistence.repository.FavoriteRepository
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import kotlin.collections.map

@Service
class FavoriteService(
    private val repository: FavoriteRepository,
) {
    suspend fun create(f : Favorite): Favorite { val data = f.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    fun getAll() = repository.findAll().map { it.toDomain() }

    fun getUserFavoriteProperty(user: UserEntity) : List<Favorite>?{
        return repository.findFavoriteByPropertyUser(user).let {list-> list?.map{it.toDomain()}?.toList() ?: emptyList() }
    }
    fun getOneFavoritePropertyCount( property: PropertyEntity ) : List<Favorite>?{
        return repository.findOneFavoritePropertyCount(property).let {list-> list?.map{it.toDomain()}?.toList() ?: emptyList() }
    }
    fun getFavoriteIfExist( property: PropertyEntity , user: UserEntity) : List<Favorite>?{
        return repository.findFavoriteExist(property, user).let{list-> list?.map{it.toDomain()}?.toList() ?: emptyList() }
    }
    suspend fun deleteById(favoriteId: Long) {
        return repository.deleteById(favoriteId)
    }
    fun deleteAllFavoriteUser(user: UserEntity) : Int{
        return repository.deleteAllFavoriteUser(user)
    }
}