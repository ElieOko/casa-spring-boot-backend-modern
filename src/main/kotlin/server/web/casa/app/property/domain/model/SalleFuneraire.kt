package server.web.casa.app.property.domain.model

import jakarta.validation.constraints.NotNull
import server.web.casa.app.ecosystem.domain.request.ImageRequest
import server.web.casa.app.payment.domain.model.Devise
import server.web.casa.app.property.infrastructure.persistence.entity.SalleFuneraireEntity
import java.time.LocalDate

class SalleFuneraire(
    var id: Long? = null,
    val userId : Long? = null,
    val deviseId : Long? = null,
    val title: String,
    val description: String? = "",
    val capacityPeople: String? = "",
    val price : Double? = null,
    val address: String,
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val electric: Int? = 0,
    val water: Int? = 0,
    val countryValue: String? = "",
    val cityId: Long? = null,
    val postalCode: String? = "",
    val communeId: Long? = null,
    val quartierId: Long? = null,
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
    countryValue = this.countryValue,
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
)
data class SalleFuneraireDTOMaster(
    val funeraire: SalleFuneraire,
    val devise: Devise?,
    val postBy : String,
    val images : List<SalleFuneraireImage?>,
    val feature: List<Feature>
)

data class SalleFuneraireRequest(
    @NotNull
    val funeraire :SalleFuneraireDTO ,
    @NotNull
    val images : List<ImageRequest>,
    val features : List<FeatureRequest> = emptyList(),
)