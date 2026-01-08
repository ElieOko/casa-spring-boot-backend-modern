package server.web.casa.app.property.domain.model.request

import jakarta.validation.constraints.NotNull
import server.web.casa.app.property.domain.model.Vacance

data class VacanceRequest(
    var agenceId : Long? = null,
    @NotNull
    val userId : Long? = null,
    @NotNull
    val title: String,
    val description: String? = "",
    val note: String? = "",
    val dateVacance: String? = null,
    val heureVacance : String? = null,
    @NotNull
    val price: Double? = null,
    val priceCombo: String? = "",
    val offre: String? = "",
    val capacity: Int? = 0,
    val address: String,
    val cityValue: String? = "",
    val countryValue: String? = "",
    var isAvailable: Boolean = true,
    val images : List<ImageRequest?> = emptyList(),
)

fun VacanceRequest.toDomain() = Vacance(
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
)