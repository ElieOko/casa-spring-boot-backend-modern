package server.web.casa.app.property.infrastructure.persistence.mapper

import org.springframework.stereotype.Component
import server.web.casa.app.property.domain.model.Favorite
import server.web.casa.app.property.infrastructure.persistence.entity.FavoriteEntity
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper


@Component
class FavoriteMapper(
        private val userMapper : UserMapper,
        private  val featureM : FeatureMapper,
        private val propertyMapper: PropertyMapper
    ){
        fun toDomain(f : FavoriteEntity): Favorite {
        return Favorite(
            favoriteId = f.favoriteId,
            user = userMapper.toDomain(f.user),
            property = propertyMapper.toDomain(f.property!!),
            feature = featureM.toDomain(f.feature!!),
            createdAt = f.createdAt,
        )
    }

    fun toEntity(f : Favorite): FavoriteEntity {
        return FavoriteEntity(
            favoriteId = f.favoriteId,
            user = userMapper.toEntity(f.user)!!,
            property = propertyMapper.toEntity(f.property!!),
            feature = featureM.toEntity(f.feature!!),
            createdAt = f.createdAt
        )
    }
}