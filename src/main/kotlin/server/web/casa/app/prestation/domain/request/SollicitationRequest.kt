package server.web.casa.app.prestation.domain.request

import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class SollicitationRequest(
    @NotNull
    val userId: Long,
    @NotNull
    val prestationId: Long,
    @NotNull
    val deviseId: Long,
    @NotNull
    val budget: Double,
    @NotNull
    val description: String,
    @NotNull
    val startDate: LocalDate,
    @NotNull
    val endDate: LocalDate,
)