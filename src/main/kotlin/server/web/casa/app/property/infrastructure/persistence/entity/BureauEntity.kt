package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.property.domain.model.AddressDTO
import server.web.casa.app.property.domain.model.Bureau
import server.web.casa.app.property.domain.model.dto.GeoDTO
import java.time.LocalDateTime

@Table(name = "bureau")
class BureauEntity(
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
    @Column("price")
    val price: Double? = null,
    @Column("room_meet")
    val roomMeet: Int? = 0,
    @Column("number_piece")
    val numberPiece: Int? = 0,
    @Column("is_equip")
    val isEquip: Boolean = false,
    @Column("electric")
    val electric: Int? = 0,
    @Column("water")
    val water: Int? = 0,
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

fun BureauEntity.toDomain() = Bureau(
    id = this.id,
    userId = this.userId,
    deviseId = this.deviseId,
    title = this.title,
    description = this.description,
    transactionType = this.transactionType,
    propertyTypeId = this.propertyTypeId,
    price = this.price,
    roomMeet = this.roomMeet,
    numberPiece = this.numberPiece,
    isEquip = this.isEquip,
    address = this.address,
    isAvailable = this.isAvailable,
    createdAt = this.createdAt
)

fun BureauEntity.toAddressDTO() = AddressDTO(
    address = this.address,
    communeValue = this.communeValue,
    quartierValue = this.quartierValue,
    cityValue = this.cityValue,
    countryValue = this.countryValue,
    postalCode = this.postalCode,
)

fun BureauEntity.toGeo() = GeoDTO(this.latitude, this.longitude)