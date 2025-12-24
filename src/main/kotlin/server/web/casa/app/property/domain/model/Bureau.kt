package server.web.casa.app.property.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import server.web.casa.app.ecosystem.domain.request.ImageRequest
import server.web.casa.app.payment.domain.model.Devise
import server.web.casa.app.property.infrastructure.persistence.entity.BureauEntity
import java.time.LocalDateTime

class Bureau(
    var id: Long? = null,
    val userId : Long? = null,
    val deviseId : Long? = null,
    val title: String,
    val description: String? = "",
    val price: Double? = null,
    val electric: Int? = 0,
    val water: Int? = 0,
    val roomMeet: Int? = 0,
    val numberPiece: Int? = 0,
    val isEquip: Boolean = false,
    val address: String,
    val postalCode: String? = "",
    var communeId: Long? = 0,
    var quartierId: Long? = 0,
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    var cityId: Long? = 0,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

class BureauRequest(
    @NotNull
    val userId : Long? = null,
    val deviseId : Long? = null,
    @NotNull
    val title: String,
    val description: String? = "",
    @NotNull
    val price: Double? = null,
    val roomMeet: Int? = 0,
    val electric: Int? = 0,
    val water: Int? = 0,
    val numberPiece: Int? = 0,
    val isEquip: Boolean = false,
    val postalCode: String? = "",
    var communeId: Long? = 0,
    var quartierId: Long? = 0,
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    var cityId: Long? = 0,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @NotNull
    val address: String,
)

data class BureauDTOMaster(
    val bureau: Bureau,
    val devise: Devise?,
    val images : List<BureauImage?>
)

data class BureauDto(
   @NotNull
   val bureau: BureauRequest,
   @NotNull
   val images : List<ImageRequest>
)

fun Bureau.toEntity() = BureauEntity(
    id = this.id,
    userId = this.userId,
    deviseId = this.deviseId,
    title = this.title,
    description = this.description,
    price = this.price,
    water = this.water,
    electric = this.electric,
    roomMeet = this.roomMeet,
    numberPiece = this.numberPiece,
    isEquip = this.isEquip,
    address = this.address,
    latitude = this.latitude,
    longitude = this.longitude,
    communeValue = this.communeValue,
    quartierValue = this.quartierValue,
    countryValue = this.countryValue,
    cityValue = this.cityValue,
    communeId = this.communeId,
    quartierId = this.quartierId,
    cityId = this.cityId,
    postalCode = this.postalCode,
    createdAt = this.createdAt
)

fun BureauRequest.toDomain() = Bureau(
    userId = this.userId,
    deviseId = this.deviseId,
    title = this.title,
    water = this.water,
    numberPiece = this.numberPiece,
    electric = this.electric,
    description = this.description,
    price = this.price,
    roomMeet = this.roomMeet,
    isEquip = this.isEquip,
    address = this.address,
    latitude = this.latitude,
    longitude = this.longitude,
    communeValue = this.communeValue,
    quartierValue = this.quartierValue,
    countryValue = this.countryValue,
    cityValue = this.cityValue,
    communeId = this.communeId,
    quartierId = this.quartierId,
    cityId = this.cityId,
    postalCode = this.postalCode,
)