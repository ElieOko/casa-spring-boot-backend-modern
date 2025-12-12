package server.web.casa.app.address.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "communes")
class CommuneEntity(
    @Id
    val communeId   : Long = 0,
    val districtId  : Long? = null,
    val name        : String,
)
