package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.*
import server.web.casa.app.property.domain.model.SalleFuneraire
import java.time.LocalDate

@Table(name = "salle_funeraires")
class SalleFuneraireEntity(
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
    @Column("capacity_people")
    val capacityPeople: String? = "",
    @Column("price")
    val price : Double? = null,
    @Column("usage")
    val usage: String? = "conference, etc",
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
    @Column("electric")
    val electric: Int? = 0,
    @Column("water")
    val water: Int? = 0,
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

fun SalleFuneraireEntity.toDomain() = SalleFuneraire(
    id = this.id,
    userId = this.userId,
    title = this.title,
    description = this.description,
    capacityPeople = this.capacityPeople,
    price = this.price,
    deviseId = this.deviseId,
    postalCode = this.postalCode,
    longitude = this.longitude,
    water = this.water,
    electric = this.electric,
    latitude = this.latitude,
    communeValue = this.communeValue,
    quartierValue = this.quartierValue,
    cityValue = this.cityValue,
    address = this.address,
    communeId = this.communeId,
    quartierId = this.quartierId,
    cityId = this.cityId,
    countryValue = this.countryValue,
    updatedAt = this.updatedAt,
    createdAt = this.createdAt,
)