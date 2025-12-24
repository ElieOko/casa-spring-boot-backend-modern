package server.web.casa.app.property.domain.model

import jakarta.validation.constraints.NotNull
import server.web.casa.app.ecosystem.domain.request.ImageRequest
import server.web.casa.app.payment.domain.model.Devise
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
    val price : Double? = null,
    val address: String,
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    val cityId: Long? = null,
    val postalCode: String? = "",
    val communeId: Long? = null,
    val quartierId: Long? = null,
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
    cityValue = this.cityValue
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
    cityValue = this.cityValue
)
data class SalleFestiveDTOMaster(
    val festive: SalleFestive,
    val devise: Devise?,
    val postBy : String,
    val images : List<SalleFestiveImage?>
)

data class SalleFestiveRequest(
    @NotNull
    val festive :SalleFestiveDTO ,
    @NotNull
    val images : List<ImageRequest>
)