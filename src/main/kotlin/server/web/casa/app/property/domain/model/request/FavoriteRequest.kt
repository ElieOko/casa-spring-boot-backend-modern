package server.web.casa.app.property.domain.model.request
import jakarta.validation.constraints.*
data class FavoriteRequest(
    @NotNull
    val userId : Long,
    @NotNull
    val propertyId : Long,
)
