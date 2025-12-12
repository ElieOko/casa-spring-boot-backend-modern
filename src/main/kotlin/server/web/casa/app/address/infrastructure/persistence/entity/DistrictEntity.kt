package server.web.casa.app.address.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "districts")
class DistrictEntity(
    @Id
    val districtId  : Long = 0,
    val cityId      : Long?,
    val name        : String,
)
