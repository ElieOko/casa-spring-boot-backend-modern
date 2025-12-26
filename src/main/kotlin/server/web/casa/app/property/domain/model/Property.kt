package server.web.casa.app.property.domain.model

import java.time.LocalDate

data class Property(
    val propertyId: Long? = null,
    val deviseId : Long,
    var title: String,
    var description: String? = "",
    var price: Double,
    var surface: Double? = null,
    var rooms: Int? = 0,
    val bedrooms: Int? = 0,
    var electric: Int? = 1,
    var water: Int? = 1,
    var kitchen: Int? = 0,
    val livingRoom: Int? = 0,
    var guarantee: String = "",
    var bathroom: Int? = 0,
    var floor: Int? = 0,
    var address: String,
    var city: Long?,
    var quartier: Long?,
    var postalCode: String? = "",
    var commune: Long?,
    var communeValue: String? = "",
    var quartierValue: String? = "",
    var cityValue: String? = "",
    var countryValue: String? = "",
    var sold: Boolean,
    var transactionType: String,
    var propertyTypeId: Long,
    val user: Long?,
    var latitude: Double? = null,
    var longitude: Double? = null,
    val isAvailable: Boolean = true,
    val createdAt: LocalDate = LocalDate.now(),
    var updatedAt: LocalDate = LocalDate.now(),
)

data class PropertyDataSource(
    val property : Property,
    val feature: List<Feature>
)