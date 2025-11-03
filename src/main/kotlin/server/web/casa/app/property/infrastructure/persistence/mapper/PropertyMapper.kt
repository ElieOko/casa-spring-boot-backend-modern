package server.web.casa.app.property.infrastructure.persistence.mapper

import org.springframework.stereotype.Component
import server.web.casa.app.address.infrastructure.persistence.mapper.CityMapper
import server.web.casa.app.address.infrastructure.persistence.mapper.CommuneMapper
import server.web.casa.app.address.infrastructure.persistence.mapper.QuartierMapper
import server.web.casa.app.property.domain.model.Property
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper

@Component
class PropertyMapper(
    private val cityMapper: CityMapper,
    private val userMapper: UserMapper,
    private val propertyImageLivingRoomMapper : PropertyImageLivingRoomMapper,
    private val propertyImageRoomMapper : PropertyImageRoomMapper,
    private val propertyImageMapper: PropertyImageMapper,
    private val propertyImageKitchenMapper: PropertyImageKitchenMapper,
    private val propertyTypeMapper: PropertyTypeMapper,
    private val communeMapper: CommuneMapper,
    private val featureMapper: FeatureMapper,
    private val quartierMapper: QuartierMapper
) {
    fun toDomain(p : PropertyEntity) : Property {
        return Property(
            propertyId = p.propertyId,
            title = p.title,
            description = p.description,
            price = p.price,
            surface = p.surface,
            rooms = p.rooms,
            bedrooms = p.bedrooms,
            kitchen = p.kitchen,
            livingRoom = p.livingRoom,
            bathroom = p.bathroom,
            water = p.water,
            floor = p.floor,
            address = p.address,
            city = cityMapper.toDomain(p.city) ,
            postalCode = p.postalCode,
            commune = communeMapper.toDomain(p.commune),
            quartier = quartierMapper.toDomain(p.quartier),
            sold = p.sold,
            electric = p.electric,
            transactionType = p.transactionType,
            propertyType = propertyTypeMapper.toDomain(p.propertyType),
            user = userMapper.toDomain(p.user),
            latitude = p.latitude,
            longitude = p.longitude,
            isAvailable = p.isAvailable,
            features =  p.features.stream().map { featureMapper.toDomain(it) }.toList(),
            propertyImage = p.propertyImage.map { propertyImageMapper.toDomain(it)  }.toList()  ,
            propertyImageRoom = p.propertyImageRoom.stream().map { propertyImageRoomMapper.toDomain(it) }.toList(),
            propertyImageLivingRoom = p.propertyImageLivingRoom.stream().map { propertyImageLivingRoomMapper.toDomain(it) }.toList(),
            propertyImageKitchen = p.propertyImageKitchen.stream().map { propertyImageKitchenMapper.toDomain(it) }.toList(),
            createdAt = p.createdAt,
            updatedAt = p.updatedAt
        )
    }
    fun toEntity(p : Property) : PropertyEntity {
        return PropertyEntity(
            propertyId = p.propertyId,
            title = p.title,
            electric = p.electric,
            water = p.water,
            description = p.description,
            price = p.price,
            surface = p.surface,
            rooms = p.rooms,
            bedrooms = p.bedrooms,
            kitchen = p.kitchen,
            livingRoom = p.livingRoom,
            bathroom = p.bathroom,
            floor = p.floor,
            address = p.address,
            city = cityMapper.toEntity(p.city) ,
            postalCode = p.postalCode,
            commune = communeMapper.toEntity(p.commune),
            quartier = quartierMapper.toEntity(p.quartier),
            sold = p.sold,
            transactionType = p.transactionType,
            propertyType = propertyTypeMapper.toEntity(p.propertyType),
            user = userMapper.toEntity(p.user),
            latitude = p.latitude,
            longitude = p.longitude,
            isAvailable = p.isAvailable,
            features = p.features.stream().map { featureMapper.toEntity(it) }.toList(),
            createdAt = p.createdAt,
            updatedAt = p.updatedAt
        )
    }
}