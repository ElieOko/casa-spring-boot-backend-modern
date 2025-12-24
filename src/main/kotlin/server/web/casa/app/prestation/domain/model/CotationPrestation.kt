package server.web.casa.app.prestation.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class CotationPrestation(
    val id              : Long? = null,
    val userId          : Long,
    val sollicitationId : Long,
    val cote            : Float,
    val commentaire     : String,
    val isActive        : Boolean,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
