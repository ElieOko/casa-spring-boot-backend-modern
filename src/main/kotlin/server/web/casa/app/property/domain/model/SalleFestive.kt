package server.web.casa.app.property.domain.model

import jakarta.validation.constraints.NotNull
import org.springframework.data.relational.core.mapping.Column
import server.web.casa.app.ecosystem.domain.request.ImageRequest
import server.web.casa.app.payment.domain.model.Devise
import server.web.casa.app.property.domain.model.dto.GeoDTO
import server.web.casa.app.property.domain.model.dto.LocalAddressDTO
import server.web.casa.app.property.infrastructure.persistence.entity.SalleFestiveEntity
import java.time.LocalDate

class SalleFestive(
    var id: Long? = null,
    val userId : Long? = null,
    val deviseId : Long? = null,
    val title: String,
    val description: String? = "",
    val usage: String? = "conference, mariage, etc",
    val capacityPeople: Long? = null,
    val electric: Int? = 0,
    val water : Int? = 0,
    val transactionType: String = "",
    var propertyTypeId: Long? = 0,
    val price : Double? = null,
    val address: String,
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    var cityId: Long? = null,
    var sold: Boolean = false,
    val postalCode: String? = "",
    var communeId: Long? = null,
    var quartierId: Long? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    var isAvailable: Boolean = true,
    var isDecore: Boolean = false,
    var pisteDanse: Boolean = false,
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now()
)


class SalleFestiveDTO(
    @NotNull
    val userId : Long? = 0,
    val deviseId : Long? = 0,
    @NotNull
    val title: String,
    val description: String? = "",
    val usage: String? = "conference, mariage, etc",
    @NotNull
    val capacityPeople: Long? = null,
    val electric: Int? = 0,
    @NotNull
    val price : Double? = null,
    @NotNull
    val address: String,
    @NotNull
    val transactionType: String,
    @NotNull
    val propertyTypeId: Long? = 0,
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    var cityId: Long? = 0,
    val water : Int? = 0,
    val postalCode: String? = "",
    var communeId: Long? = 0,
    var quartierId: Long? = 0,
    val latitude: Double? = null,
    val longitude: Double? = null,
    var isAvailable: Boolean = true,
    var isDecore: Boolean = false,
    var pisteDanse: Boolean = false,
)

fun SalleFestive.toEntity()= SalleFestiveEntity(
    id = this.id,
    userId = this.userId,
    deviseId = this.deviseId,
    title = this.title,
    description = this.description,
    usage = this.usage,
    capacityPeople = this.capacityPeople,
    electric = this.electric,
    price = this.price,
    address = this.address,
    postalCode = this.postalCode,
    pisteDanse = this.pisteDanse,
    communeId = this.communeId,
    quartierId = this.quartierId,
    sold = this.sold,
    cityId = this.cityId,
    updatedAt = this.updatedAt,
    createdAt = this.createdAt,
    isDecore = this.isDecore,
    isAvailable = this.isAvailable,
    latitude = this.latitude,
    longitude = this.longitude,
    communeValue = this.communeValue,
    quartierValue = this.quartierValue,
    countryValue = this.countryValue,
    cityValue = this.cityValue,
    transactionType = this.transactionType,
    propertyTypeId = this.propertyTypeId,
)

fun SalleFestiveDTO.toDomain()= SalleFestive(
    userId = this.userId,
    deviseId = this.deviseId,
    title = this.title,
    description = this.description,
    usage = this.usage,
    capacityPeople = this.capacityPeople,
    electric = this.electric,
    price = this.price,
    water = this.water,
    address = this.address,
    postalCode = this.postalCode,
    pisteDanse = this.pisteDanse,
    communeId = this.communeId,
    quartierId = this.quartierId,
    cityId = this.cityId,
    isDecore = this.isDecore,
    isAvailable = this.isAvailable,
    latitude = this.latitude,
    longitude = this.longitude,
    communeValue = this.communeValue,
    quartierValue = this.quartierValue,
    countryValue = this.countryValue,
    cityValue = this.cityValue,
    transactionType = this.transactionType,
    propertyTypeId = this.propertyTypeId,
)
data class SalleFestiveDTOMaster(
    val festive: FestiveDTO,
    val devise: Devise?,
    val postBy : String,
    val images : List<SalleFestiveImage?>,
    val feature: List<Feature>,
    val address :AddressDTO,
    val geoZone : GeoDTO,
    val localAddress : LocalAddressDTO,
    val typeProperty: PropertyType,
)

data class FestiveDTO(
    var id: Long? = null,
    val userId : Long? = null,
    val deviseId : Long? = null,
    val title: String,
    val description: String? = "",
    val usage: String? = "conference, mariage, etc",
    val capacityPeople: Long? = null,
    val electric: Int? = 0,
    val water : Int? = 0,
    val propertyTypeId: Long? = 0,
    val transactionType: String,
    val price : Double? = null,
    var isAvailable: Boolean = true,
    var isDecore: Boolean = false,
    var pisteDanse: Boolean = false,
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now()
)
data class SalleFestiveRequest(
    @NotNull
    val festive :SalleFestiveDTO ,
    @NotNull
    val images : List<ImageRequest>,
    val features : List<FeatureRequest> = emptyList(),
)

fun SalleFestive.toDTO() = FestiveDTO(
    id = this.id,
    userId = this.userId,
    deviseId = this.deviseId,
    title = this.title,
    description = this.description,
    usage = this.usage,
    capacityPeople = this.capacityPeople,
    electric = this.electric,
    price = this.price,
    isAvailable = this.isAvailable,
    isDecore = this.isDecore,
    pisteDanse = this.pisteDanse,
    water = this.water,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    transactionType = this.transactionType,
    propertyTypeId = this.propertyTypeId,
)