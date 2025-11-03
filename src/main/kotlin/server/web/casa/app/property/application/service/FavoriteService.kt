package server.web.casa.app.property.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.Favorite
import server.web.casa.app.property.infrastructure.persistence.entity.FeatureEntity
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.property.infrastructure.persistence.mapper.FavoriteMapper
import server.web.casa.app.property.infrastructure.persistence.repository.FavoriteRepository
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity

@Service
class FavoriteService(
    private val repos: FavoriteRepository,
    private val mapper : FavoriteMapper
) {
        suspend fun create(f : Favorite): Favorite {
        val data = mapper.toEntity(f)
        val result = repos.save(data)
        return mapper.toDomain(result)
    }
    suspend fun getAll() : List<Favorite> = repos.findAll().map { mapper.toDomain(it) }.toList()

    suspend fun getUserFavoriteFeature(user: UserEntity) : List<Favorite>?{
        return repos.findFavoriteByFeatureUser( user).let {list-> list?.map{mapper.toDomain(it)}?.toList() ?: emptyList() }
    }

    suspend fun getUserFavoriteProperty(user: UserEntity) : List<Favorite>?{
        return repos.findFavoriteByPropertyUser(user).let {list-> list?.map{mapper.toDomain(it)}?.toList() ?: emptyList() }
    }

    suspend fun getOneFavoriteFeatureCount( feature : FeatureEntity) : List<Favorite>?{
        return repos.findOneFavoriteFeatureCount(feature).let {list-> list?.map{mapper.toDomain(it)}?.toList() ?: emptyList() }
    }

    suspend fun getOneFavoritePropertyCount( property: PropertyEntity ) : List<Favorite>?{
        return repos.findOneFavoritePropertyCount(property).let {list-> list?.map{mapper.toDomain(it)}?.toList() ?: emptyList() }
    }

    suspend fun getAllFavoriteProperty() : List<Favorite>?{
        return repos.findAllFavoriteProperty().let {list-> list?.map{mapper.toDomain(it)}?.toList() ?: emptyList() }
    }

    suspend fun getAllFavoriteFeature() : List<Favorite>?{
        return repos.findAllFavoriteFeature().let {list-> list?.map{mapper.toDomain(it)}?.toList() ?: emptyList() }
    }
    suspend fun deleteById(favoriteId: Long) : Int{
        return repos.deleteByIdFavorite(favoriteId)
    }

    suspend fun deleteAllFavoriteFeatureUser(user: UserEntity) : Int{
        return repos.deleteAllFavoriteFeatureUser(user)
    }

    suspend fun deleteAllFavoritePropertyUser(user: UserEntity) : Int{
        return repos.deleteAllFavoritePropertyUser(user)
    }

    suspend fun deleteAllFavoriteUser(user: UserEntity) : Int{
        return repos.deleteAllFavoriteUser(user)
    }

}