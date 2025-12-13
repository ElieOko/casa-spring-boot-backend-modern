package server.web.casa.app.pub.infrastructure.persistance.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import java.time.LocalDate

@Table(name = "publicities")
class PublicityEntity(
    @Id
    val publicityId: Long = 0,
    val user            : UserEntity?,
    val image           : String? = null,
    val title           : String,
    val description     : String,
    var isActive        : Boolean = true,
    val createdAt       : LocalDate = LocalDate.now(),
)
