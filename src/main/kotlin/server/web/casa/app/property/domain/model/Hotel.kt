package server.web.casa.app.property.domain.model

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import org.springframework.data.relational.core.mapping.Column
import server.web.casa.app.property.infrastructure.persistence.entity.HotelEntity
import java.time.LocalDate

class Hotel(
    val id: Long? = null,
    val userId: Long? = null,
    val title: String,
    val description: String? = "",
    val address: String,
    var image: String,
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    val cityId: Long? = null,
    val postalCode: String? = "",
    val communeId: Long? = null,
    val quartierId: Long? = null,
    val latitude: Double? = null,
    val propertyTypeId: Long? = null,
    val longitude: Double? = null,
    var isAvailable: Boolean = true,
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now()
)
class HotelRequest(
    @NotNull
    val userId: Long? = null,
    @NotNull
    val title: String,
    val description: String? = "",
    @NotNull
    val address: String,
    @NotNull
    var image: String,
    @NotNull
    val propertyTypeId: Long? = null,
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    var cityId: Long? = null,
    val postalCode: String? = "",
    var communeId: Long? = null,
    var quartierId: Long? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    var isAvailable: Boolean = true,
)

fun HotelRequest.toDomain() = Hotel(
    userId = this.userId,
    title = this.title,
    description = this.description,
    address = this.address,
    image = this.image,
    propertyTypeId = this.propertyTypeId,
    communeId = this.communeId,
    quartierId = this.quartierId,
    cityId = this.cityId,
    countryValue = this.countryValue,
    communeValue = this.communeValue,
    latitude = this.latitude,
    longitude = this.longitude,
    quartierValue = this.quartierValue,
)
fun Hotel.toEntity() = HotelEntity(
    id = this.id,
    userId = this.userId,
    title = this.title,
    description = this.description,
    address = this.address,
    image = this.image,
    propertyTypeId = this.propertyTypeId,
    communeId = this.communeId,
    quartierId = this.quartierId,
    cityId = this.cityId,
    countryValue = this.countryValue,
    communeValue = this.communeValue,
    latitude = this.latitude,
    longitude = this.longitude,
    quartierValue = this.quartierValue,
    postalCode = this.postalCode,
    isAvailable = this.isAvailable,
)