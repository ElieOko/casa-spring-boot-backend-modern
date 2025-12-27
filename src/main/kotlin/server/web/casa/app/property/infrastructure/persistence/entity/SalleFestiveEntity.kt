package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.*
import server.web.casa.app.property.domain.model.AddressDTO
import server.web.casa.app.property.domain.model.SalleFestive
import server.web.casa.app.property.domain.model.dto.GeoDTO
import java.time.LocalDate

@Table(name = "salle_festives")
class SalleFestiveEntity(
    @Id
    @Column("id")
    var id: Long? = null,
    @Column("user_id")
    val userId : Long? = null,
    @Column("devise_id")
    val deviseId : Long? = null,
    @Column("title")
    val title: String,
    @Column("description")
    val description: String? = "",
    @Column("usage")
    val usage: String? = "conference, mariage, etc",
    @Column("capacity_people")
    val capacityPeople: Long? = null,
    @Column("electric")
    val electric: Int? = 0,
    @Column("water")
    val water: Int? = 0,
    @Column("price")
    val price : Double? = null,
    @Column("address")
    val address: String,
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
    @Column("transaction_type")
    val transactionType: String = "",
    @Column("property_type_id")
    val propertyTypeId: Long? = null,
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
    @Column("is_decore")
    var isDecore: Boolean = false,
    @Column("piste_danse")
    var pisteDanse: Boolean = false,
    @Column("created_at")
    val createdAt: LocalDate = LocalDate.now(),
    @Column("updated_at")
    val updatedAt: LocalDate = LocalDate.now()
)

fun SalleFestiveEntity.toDomain()= SalleFestive(
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
    water= this.water,
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
    cityValue = this.cityValue,
    transactionType = this.transactionType,
    propertyTypeId = this.propertyTypeId,
)

fun SalleFestiveEntity.toAddressDTO() = AddressDTO(
    address = this.address,
    communeValue = this.communeValue,
    quartierValue = this.quartierValue,
    cityValue = this.cityValue,
    countryValue = this.countryValue,
    postalCode = this.postalCode,
)

fun SalleFestiveEntity.toGeo() = GeoDTO(this.latitude, this.longitude)