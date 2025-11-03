package server.web.casa.app.property.domain.model.request
import jakarta.validation.constraints.*
data class FavoriteRequest(
    @NotNull
    val userId : Long,
    @Null
    val propertyId : Long,
    @Null
    val featureId : Long
)
