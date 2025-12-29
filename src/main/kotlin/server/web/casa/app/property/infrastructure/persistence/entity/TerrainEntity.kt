package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.*
import server.web.casa.app.property.domain.model.AddressDTO
import server.web.casa.app.property.domain.model.dto.GeoDTO
import java.time.LocalDateTime

@Table(name = "terrains")
class TerrainEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("user_id")
    val userId: Long,
    @Column("title")
    val title: String,
    @Column("description")
    val description : String = "",
    @Column("devise_id")
    val deviseId : Long? = null,
    @Column("price")
    val price: Double? = null,
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
    @Column("commune_id")
    val communeId: Long? = null,
    @Column("surface")
    val surface: String? = null,
    @Column("quartier_id")
    val quartierId: Long? = null,
    @Column("transaction_type")
    val transactionType: String = "",
    @Column("property_type_id")
    val propertyTypeId: Long? = null,
    @Column("is_available")
    var isAvailable: Boolean = true,
    @Column("latitude")
    val latitude: Double? = null,
    @Column("longitude")
    val longitude: Double? = null,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

fun TerrainEntity.toAddressDTO() = AddressDTO(
    address = this.address,
    communeValue = this.communeValue,
    quartierValue = this.quartierValue,
    cityValue = this.cityValue,
    countryValue = this.countryValue,
    postalCode = this.postalCode,
)

fun TerrainEntity.toGeo() = GeoDTO(this.latitude, this.longitude)