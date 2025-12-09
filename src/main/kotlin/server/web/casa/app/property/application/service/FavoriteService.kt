package server.web.casa.app.property.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.Favorite
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.property.infrastructure.persistence.mapper.*
import server.web.casa.app.property.infrastructure.persistence.repository.FavoriteRepository
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import kotlin.collections.map

@Service
class FavoriteService(
    private val repos: FavoriteRepository,
) {
    fun create(f : Favorite): Favorite { val data = f.toEntity()
        val result = repos.save(data)
        return result.toDomain()
    }
    fun getAll() : List<Favorite> = repos.findAll().map { it.toDomain() }.toList()

    fun getUserFavoriteProperty(user: UserEntity) : List<Favorite>?{
        return repos.findFavoriteByPropertyUser(user).let {list-> list?.map{it.toDomain()}?.toList() ?: emptyList() }
    }
    fun getOneFavoritePropertyCount( property: PropertyEntity ) : List<Favorite>?{
        return repos.findOneFavoritePropertyCount(property).let {list-> list?.map{it.toDomain()}?.toList() ?: emptyList() }
    }
    fun getFavoriteIfExist( property: PropertyEntity , user: UserEntity) : List<Favorite>?{
        return repos.findFavoriteExist(property, user).let{list-> list?.map{it.toDomain()}?.toList() ?: emptyList() }
    }
    fun deleteById(favoriteId: Long) {
        return repos.deleteById(favoriteId)
    }
    fun deleteAllFavoriteUser(user: UserEntity) : Int{
        return repos.deleteAllFavoriteUser(user)
    }
}