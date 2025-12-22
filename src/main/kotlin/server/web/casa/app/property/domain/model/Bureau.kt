package server.web.casa.app.property.domain.model

import server.web.casa.app.property.infrastructure.persistence.entity.BureauEntity
import java.time.LocalDateTime

class Bureau(
    var id: Long? = null,
    val userId : Long? = null,
    val deviseId : Long? = null,
    val title: String,
    val description: String? = "",
    val price: Double? = null,
    val roomMeet: Int? = 0,
    val numberPiece: Int? = 0,
    val isEquip: Boolean = false,
    val address: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

fun Bureau.toEntity() = BureauEntity(
    id = this.id,
    userId = this.userId,
    deviseId = this.deviseId,
    title = this.title,
    description = this.description,
    price = this.price,
    roomMeet = this.numberPiece,
    isEquip = this.isEquip,
    address = this.address,
    createdAt = this.createdAt
)