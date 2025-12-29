package server.web.casa.app.property.domain.model

import java.time.LocalDateTime

class Terrain(
    val id: Long? = null,
    val userId: Long,
    val title: String,
    val description : String = "",
    val deviseId : Long? = null,
    val price: Double? = null,
    val surface : String? = "",
    val address: String,
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    val cityId: Long? = null,
    val postalCode: String? = "",
    val communeId: Long? = null,
    val quartierId: Long? = null,
    val transactionType: String = "",
    val propertyTypeId: Long? = null,
    var isAvailable: Boolean = true,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)