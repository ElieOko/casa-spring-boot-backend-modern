package server.web.casa.app.property.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import server.web.casa.app.ecosystem.domain.request.ImageRequest
import server.web.casa.app.payment.domain.model.Devise
import server.web.casa.app.property.domain.model.dto.GeoDTO
import server.web.casa.app.property.domain.model.dto.Images
import server.web.casa.app.property.domain.model.dto.LocalAddressDTO
import server.web.casa.app.property.domain.model.dto.PropertyDTO
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
    var isAvailable: Boolean = true,
    var communeId: Long? = 0,
    var quartierId: Long? = 0,
    val transactionType: String = "",
    var propertyTypeId: Long? = 0,
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    var cityId: Long? = 0,
    val latitude: Double? = null,
    val longitude: Double? = null,
    var sold: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

class BureauRequest(
    @NotNull
    val userId : Long? = null,
    @NotNull
    val deviseId : Long? = null,
    @NotNull
    val title: String,
    val description: String? = "",
    @NotNull
    val price: Double? = null,
    @NotNull
    val transactionType: String,
    @NotNull
    val propertyTypeId: Long?,
    val roomMeet: Int? = 0,
    val electric: Int? = 0,
    val water: Int? = 0,
    val numberPiece: Int? = 0,
    val isEquip: Boolean = false,
    val postalCode: String? = "",
    var communeId: Long? = 0,
    var isAvailable: Boolean = true,
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
    val bureau: BureauDTO,
    val devise: Devise?,
    val postBy : String,
    val address :AddressDTO,
    val localAddress : LocalAddressDTO,
    val geoZone : GeoDTO,
    val images : List<BureauImage?>,
    val feature: List<Feature>,
    val typeProperty: PropertyType,
)

data class BureauDtoRequest(
   @NotNull
   val bureau: BureauRequest,
   @NotNull
   val images : List<ImageRequest>,
   val features : List<FeatureRequest> = emptyList(),
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
    isAvailable = this.isAvailable,
    latitude = this.latitude,
    longitude = this.longitude,
    communeValue = this.communeValue,
    quartierValue = this.quartierValue,
    countryValue = this.countryValue,
    cityValue = this.cityValue,
    communeId = this.communeId,
    quartierId = this.quartierId,
    cityId = this.cityId,
    transactionType = this.transactionType,
    propertyTypeId = this.propertyTypeId,
    postalCode = this.postalCode,
    createdAt = this.createdAt,
    sold = this.sold
)

fun BureauRequest.toDomain() = Bureau(
    userId = this.userId,
    deviseId = this.deviseId,
    title = this.title,
    water = this.water,
    transactionType = this.transactionType,
    propertyTypeId = this.propertyTypeId,
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
    isAvailable = this.isAvailable,
    quartierId = this.quartierId,
    cityId = this.cityId,
    postalCode = this.postalCode,
)

 data class BureauDTO(
     var id: Long? = null,
     val userId : Long? = null,
     val title: String,
     val description: String? = "",
     val price: Double? = null,
     val electric: Int? = 0,
     val water: Int? = 0,
     val transactionType: String = "",
     val propertyTypeId: Long? = 0,
     val roomMeet: Int? = 0,
     var isAvailable: Boolean = true,
     val numberPiece: Int? = 0,
     val isEquip: Boolean = false,
     val createdAt: LocalDateTime = LocalDateTime.now(),
 )
fun Bureau.toDT0() = BureauDTO(
    id = this.id,
    userId = this.userId,
    title = this.title,
    description = this.description,
    price = this.price,
    electric = this.electric,
    water = this.water,
    isAvailable = this.isAvailable,
    transactionType = this.transactionType,
    propertyTypeId = this.propertyTypeId,
    roomMeet = this.roomMeet,
    numberPiece = this.numberPiece,
    isEquip = this.isEquip,
    createdAt = this.createdAt,
)
