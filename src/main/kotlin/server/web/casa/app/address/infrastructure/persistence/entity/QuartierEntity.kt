package server.web.casa.app.address.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "quartiers")
 class QuartierEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("commune_id")
    val communeId: Long? = null,
    @Column("name")
    val name: String
)
