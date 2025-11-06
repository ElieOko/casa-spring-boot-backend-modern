package server.web.casa.app.property.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.Favorite
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.property.infrastructure.persistence.mapper.FavoriteMapper
import server.web.casa.app.property.infrastructure.persistence.repository.FavoriteRepository
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import kotlin.collections.map

@Service
class FavoriteService(
    private val repos: FavoriteRepository,
    private val mapper : FavoriteMapper
) {
         fun create(f : Favorite): Favorite {
        val data = mapper.toEntity(f)
        val result = repos.save(data)
        return mapper.toDomain(result)
    }
     fun getAll() : List<Favorite> = repos.findAll().map { mapper.toDomain(it) }.toList()

     fun getUserFavoriteProperty(user: UserEntity) : List<Favorite>?{
        return repos.findFavoriteByPropertyUser(user).let {list-> list?.map{mapper.toDomain(it)}?.toList() ?: emptyList() }
    }
     fun getOneFavoritePropertyCount( property: PropertyEntity ) : List<Favorite>?{
        return repos.findOneFavoritePropertyCount(property).let {list-> list?.map{mapper.toDomain(it)}?.toList() ?: emptyList() }
    }
     fun getFavoriteIfExist( property: PropertyEntity , user: UserEntity) : List<Favorite>?{
        return repos.findFavoriteExist(property, user).let{list-> list?.map{mapper.toDomain(it)}?.toList() ?: emptyList() }
    }
     fun deleteById(favoriteId: Long) {
        return repos.deleteById(favoriteId)
    }
     fun deleteAllFavoriteUser(user: UserEntity) : Int{
        return repos.deleteAllFavoriteUser(user)
    }

}