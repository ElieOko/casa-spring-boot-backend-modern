package server.web.casa.app.actor.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "typecards")
data class TypeCardEntity(
    @Id
    @Column("id")
    val typeCardId : Long? = null,
    @Column("name")
    val name : String?,
)
