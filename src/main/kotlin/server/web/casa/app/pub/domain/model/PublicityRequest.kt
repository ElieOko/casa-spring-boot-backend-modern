package server.web.casa.app.pub.domain.model

import jakarta.validation.Path
import jakarta.validation.constraints.NotNull

class PublicityRequest(
    @NotNull
    val userId: Long,
    @NotNull
    val imagePath: String,
    @NotNull
    val title: String,
    @NotNull
    val description: String
)