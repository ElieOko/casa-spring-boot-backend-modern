package server.web.casa.app.property.domain.model

import java.time.LocalDateTime

data class SalleFestiveImage(
    val id: Long? = null,
    val salleFestiveId : Long,
    val name: String,
    val path: String?,
    var isAvailable: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
