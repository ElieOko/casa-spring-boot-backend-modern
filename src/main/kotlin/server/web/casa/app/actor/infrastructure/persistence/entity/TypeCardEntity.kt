package server.web.casa.app.actor.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "typecards")
data class TypeCardEntity(
    @Id
    val typeCardId : Long? = 0,
    val name : String?,
)
