package server.web.casa.app.property.domain.model.dto

import server.web.casa.app.address.domain.model.City
import server.web.casa.app.address.domain.model.Commune
import server.web.casa.app.address.domain.model.Quartier
import server.web.casa.app.payment.domain.model.Devise
import server.web.casa.app.property.domain.model.AddressDTO
import server.web.casa.app.property.domain.model.Feature
import server.web.casa.app.property.domain.model.Property
import server.web.casa.app.property.domain.model.PropertyType
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity

class PropertyMasterDTO(
    val property: PropertyDTO,
    val images: Images? = null,
    val postBy: String,
    val address: AddressDTO,
    val devise: Devise?,
    val localAddress: LocalAddressDTO,
    val geoZone: GeoDTO,
    val typeProperty: PropertyType,
    val features: List<Feature> = emptyList(),
    image: String,
)



fun Property.toDto() = PropertyMasterDTO(
    property = PropertyDTO(
        propertyId = this.propertyId,
        userId = this.user,
        title = this.title,
        description = this.description,
        price = this.price,
        surface = this.surface,
        rooms = this.rooms,
        bedrooms = this.bedrooms,
        electric = this.electric,
        water = this.water,
        kitchen = this.kitchen,
        livingRoom = this.livingRoom,
        guarantee = this.guarantee,
        bathroom = this.bathroom,
        floor = this.floor,
        sold = this.sold,
        deviseId = this.deviseId,
        transactionType = this.transactionType,
        isAvailable = this.isAvailable,
        createdAt = this.createdAt,
    ),
    postBy = "",
    address = AddressDTO(
        address = this.address,
        communeValue = this.communeValue,
        quartierValue = this.quartierValue,
        cityValue = this.cityValue,
        countryValue = this.countryValue,
        postalCode = this.postalCode,
    ),
    devise = Devise(id = this.deviseId,"","",0.0),
    localAddress = LocalAddressDTO(
        city = City(this.city, 0, ""),
        commune = Commune(this.commune, 0, emptyList(), ""),
        quartier = Quartier(this.quartier, "")
    ),
    geoZone = GeoDTO(this.latitude, this.longitude),
    typeProperty = PropertyType(this.propertyTypeId, ""),,
)

fun PropertyMasterDTO.toEntity() = PropertyEntity(
    id = this.property.propertyId,

    title = this.property.title,
    description = this.property.description,
    price = this.property.price,
    surface = this.property.surface,
    rooms = this.property.rooms,
    bedrooms = this.property.bedrooms,
    kitchen = this.property.kitchen,
    livingRoom = this.property.livingRoom,
    bathroom = this.property.bathroom,
    electric = this.property.electric,
    water = this.property.water,
    floor = this.property.floor,
    address = this.address.address,
    communeValue = this.address.communeValue,
    quartierValue = this.address.quartierValue,
    cityValue = this.address.cityValue,
    countryValue = this.address.countryValue,
    cityId = this.localAddress.city?.cityId,
    postalCode = this.address.postalCode,
    communeId = this.localAddress.commune?.communeId,
    quartierId = this.localAddress.quartier?.quartierId,
    sold = this.property.sold,
    transactionType = this.property.transactionType,
    guarantee = this.property.guarantee,
    propertyTypeId = this.typeProperty.propertyTypeId,
    user = this.property.userId,
    latitude = this.geoZone.lat,
    longitude = this.geoZone.lng,
    isAvailable = this.property.isAvailable,
    deviseId = this.property.deviseId,

)

data class LocalAddressDTO(
    val city : City?,
    val commune : Commune?,
    val quartier: Quartier?
)