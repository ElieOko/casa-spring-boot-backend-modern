package server.web.casa.app.property.domain.model

import server.web.casa.app.property.infrastructure.persistence.entity.VacanceEntity
import java.time.LocalDateTime

data class Vacance(
    val id: Long? = null,
    val agenceId : Long? = null,
    val userId : Long? = null,
    val title: String,
    val description: String? = "",
    val note: String? = "",
    val dateVacance: String? = null,
    val heureVacance : String? = null,
    val price: Double? = null,
    val priceCombo: String? = "",
    val offre: String? = "",
    val capacity: Int? = 0,
    val address: String,
    val cityValue: String? = "",
    val countryValue: String? = "",
    var isAvailable: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

fun Vacance.toEntity() = VacanceEntity(
    id = this.id,
    agenceId = this.agenceId,
    userId = this.userId,
    title = this.title,
    description = this.description,
    note = this.note,
    dateVacance = this.dateVacance,
    heureVacance = this.heureVacance,
    price = this.price,
    priceCombo = this.priceCombo,
    capacity = this.capacity,
    address = this.address,
    cityValue = this.cityValue,
    countryValue = this.countryValue,
    isAvailable = this.isAvailable,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)
