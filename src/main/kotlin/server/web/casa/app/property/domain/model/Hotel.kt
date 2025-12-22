package server.web.casa.app.property.domain.model

import java.time.LocalDate

class Hotel(
    val id: Long? = null,
    val userId: Long? = null,
    val title: String,
    val description: String? = "",
    val address: String,
    val image: String,
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    val cityId: Long? = null,
    val postalCode: String? = "",
    val communeId: Long? = null,
    val quartierId: Long? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    var isAvailable: Boolean = true,
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now()
)