package server.web.casa.app.property.domain.model

import java.time.LocalDateTime

data class SalleFuneraireImage(
    val id: Long? = null,
    val salleFuneraireId : Long,
    val name: String,
    val path: String?,
    var isAvailable: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
