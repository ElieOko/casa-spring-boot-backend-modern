package server.web.casa.app.property.infrastructure.persistence.entity


import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("properties")
data class PropertyEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("title")
    val title: String,
    @Column("description")
    val description: String? = "",
    @Column("price")
    val price: Double,
    @Column("surface")
    val surface: Double? = null,
    @Column("rooms")
    val rooms: Int? = 0,
    @Column("bedrooms")
    val bedrooms: Int? = 0,
    @Column("kitchen")
    val kitchen: Int? = 0,
    @Column("living_room")
    val livingRoom: Int? = 0,
    @Column("bathroom")
    val bathroom: Int? = 0,
    @Column("electric")
    val electric: Int? = 0,
    @Column("water")
    val water: Int? = 0,
    @Column("floor")
    val floor: Int? = 0,
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
    @Column("sold")
    val sold: Boolean = false,
    @Column("transaction_type")
    val transactionType: String,
    @Column("guarantee")
    val guarantee: String = "",
    @Column("property_type_id")
    val propertyTypeId: Long,
    @Column("user_id")
    val user: Long? = null,
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

