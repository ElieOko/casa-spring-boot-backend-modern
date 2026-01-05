package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.property.domain.model.Hotel
import java.time.LocalDate

@Table(name = "hotels")
class HotelEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("user_id")
    val userId: Long? = null,
    @Column("title")
    val title: String,
    @Column("description")
    val description: String? = "",
    @Column("address")
    val address: String,
    @Column("image")
    val image: String,
    @Column("commune_value")
    val communeValue: String? = "",
    @Column("quartier_value")
    val quartierValue: String? = "",
    @Column("city_value")
    val cityValue: String? = "",
    @Column("country_value")
    val countryValue: String? = "",
    @Column("city_id")
    val cityId: Long? = null,
    @Column("postal_code")
    val postalCode: String? = "",
    @Column("commune_id")
    val communeId: Long? = null,
    @Column("quartier_id")
    val quartierId: Long? = null,
    @Column("latitude")
    val latitude: Double? = null,
    @Column("longitude")
    val longitude: Double? = null,
    @Column("is_available")
    var isAvailable: Boolean = true,
    @Column("created_at")
    val createdAt: LocalDate = LocalDate.now(),
    @Column("updated_at")
    val updatedAt: LocalDate = LocalDate.now()
)

fun HotelEntity.toDomain() = Hotel(
    id = this.id,
    userId = this.userId,
    title = this.title,
    description = this.description,
    address = this.address,
    image = this.image,
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