package server.web.casa.app.reservation.domain.model

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import server.web.casa.app.property.domain.model.Terrain
import server.web.casa.app.property.infrastructure.persistence.entity.TerrainImageEntity
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationTerrainEntity
import server.web.casa.app.user.domain.model.UserDto
import java.time.LocalDate
import javax.annotation.Nullable

data class ReservationTerrain(
    val reservationId: Long? = null,
    val userId: Long,
    val terrainId: Long,
    val message: String? = "",
    val status: String = ReservationStatus.PENDING.name,
    val type: String = ReservationType.STANDARD.name,
    val isActive: Boolean = true,
    val reservationHeure: String? = "",
    val cancellationReason: String? = "",
    val startDate: LocalDate,
    val endDate: LocalDate,
    val createdAt: LocalDate = LocalDate.now(),
)

data class ReservationTerrainDTO(
    val reservation: ReservationTerrainEntity,
    val terrain: Terrain,
    val user: UserDto,
    val imageTerrain: List<TerrainImageEntity>
)

data class ReservationTerrainRequest(
    @NotNull
    val userId: Long,
    @NotNull
    val terrainId: Long,
    @NotNull
    val startDate: LocalDate,
    @Nullable
    val message : String,
    @NotNull
    val endDate: LocalDate,
    @Nullable
    val type : ReservationType,
    @NotNull
    @field:Pattern(
        regexp = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$",
        message = "Format invalide, attendu HH:mm:ss"
    )
    val reservationHeure :String
)