package server.web.casa.app.address.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "quartiers")
 class QuartierEntity(
    @Id
    val quartierId : Long = 0,
    val commune : CommuneEntity? = null,
    val name : String,
)
