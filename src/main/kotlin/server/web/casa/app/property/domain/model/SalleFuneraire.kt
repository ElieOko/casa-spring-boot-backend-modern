package server.web.casa.app.property.domain.model

import jakarta.validation.constraints.NotNull
import server.web.casa.app.ecosystem.domain.request.ImageRequest
import server.web.casa.app.payment.domain.model.Devise
import server.web.casa.app.property.domain.model.dto.GeoDTO
import server.web.casa.app.property.domain.model.dto.LocalAddressDTO
import server.web.casa.app.property.infrastructure.persistence.entity.SalleFuneraireEntity
import java.time.LocalDate

class SalleFuneraire(
    var id: Long? = null,
    val userId : Long? = null,
    val deviseId : Long? = null,
    val title: String,
    val description: String? = "",
    val capacityPeople: String? = "",
    val transactionType: String = "",
    var propertyTypeId: Long? = 0,
    val price : Double? = null,
    var sold: Boolean = false,
    val address: String,
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val electric: Int? = 0,
    val water: Int? = 0,
    val countryValue: String? = "",
    var cityId: Long? = null,
    val postalCode: String? = "",
    var communeId: Long? = null,
    var quartierId: Long? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    var isAvailable: Boolean = true,
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now()
)

fun SalleFuneraire.toEntity() = SalleFuneraireEntity(
    id = this.id,
    userId = this.userId,
    title = this.title,
    description = this.description,
    capacityPeople = this.capacityPeople,
    price = this.price,
    electric = this.electric,
    water = this.water,
    deviseId = this.deviseId,
    postalCode = this.postalCode,
    longitude = this.longitude,
    latitude = this.latitude,
    communeValue = this.communeValue,
    quartierValue = this.quartierValue,
    cityValue = this.cityValue,
    address = this.address,
    communeId = this.communeId,
    quartierId = this.quartierId,
    cityId = this.cityId,
    sold = this.sold,
    countryValue = this.countryValue,
    transactionType = this.transactionType,
    propertyTypeId = this.propertyTypeId,
)
class SalleFuneraireDTO(
    @NotNull
    val userId : Long? = 0,
    @NotNull
    val deviseId : Long? = 0,
    @NotNull
    val title: String,
    val description: String? = "",
    val capacityPeople: String? = "",
    @NotNull
    val price : Double? = null,
    @NotNull
    val address: String,
    @NotNull
    val transactionType: String = "",
    @NotNull
    val propertyTypeId: Long? = 0,
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    var cityId: Long? = 0,
    val electric: Int? = 0,
    val water: Int? = 0,
    val postalCode: String? = "",
    var communeId: Long? = 0,
    var quartierId: Long? = 0,
    val latitude: Double? = null,
    val longitude: Double? = null,
    var isAvailable: Boolean = true,
)

fun SalleFuneraireDTO.toDomain() = SalleFuneraire(
    userId = this.userId,
    title = this.title,
    description = this.description,
    capacityPeople = this.capacityPeople,
    price = this.price,
    deviseId = this.deviseId,
    postalCode = this.postalCode,
    longitude = this.longitude,
    latitude = this.latitude,
    communeValue = this.communeValue,
    quartierValue = this.quartierValue,
    electric = this.electric,
    water = this.water,
    cityValue = this.cityValue,
    address = this.address,
    communeId = this.communeId,
    quartierId = this.quartierId,
    cityId = this.cityId,
    countryValue = this.countryValue,
    transactionType = this.transactionType,
    propertyTypeId = this.propertyTypeId,
)
data class SalleFuneraireDTOMaster(
    val funeraire: SalleFuneraire,
    val devise: Devise?,
    val postBy: String,
    val images: List<SalleFuneraireImage?>,
    val feature: List<Feature>,
    val address: AddressDTO,
    val geoZone: GeoDTO,
    val localAddress: LocalAddressDTO,
    val typeProperty: PropertyType,
    val image: String,
)

data class FuneraireDTO(
    var id: Long? = null,
    val userId : Long? = null,
    val deviseId : Long? = null,
    val title: String,
    val description: String? = "",
    val capacityPeople: String? = "",
    val transactionType: String = "",
    val propertyTypeId: Long? = 0,
    val price : Double? = null,
    val electric: Int? = 0,
    val water: Int? = 0,
    var isAvailable: Boolean = true,
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now()
)
data class SalleFuneraireRequest(
    @NotNull
    val funeraire :SalleFuneraireDTO ,
    @NotNull
    val images : List<ImageRequest>,
    val features : List<FeatureRequest> = emptyList(),
)

fun SalleFuneraire.toDTO() = FuneraireDTO(
    id = this.id,
    userId = this.userId,
    title = this.title,
    description = this.description,
    capacityPeople = this.capacityPeople,
    price = this.price,
    deviseId = this.deviseId,
    electric = this.electric,
    water = this.water,
    transactionType = this.transactionType,
    propertyTypeId = this.propertyTypeId,
    isAvailable = this.isAvailable,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)