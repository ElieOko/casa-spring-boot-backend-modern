package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.property.domain.model.Bureau
import java.time.LocalDateTime

@Table(name = "bureau")
class BureauEntity(
    @Id
    @Column("id")
    var id: Long? = null,
    @Column("user_id")
    val userId : Long? = null,
    @Column("devise_id")
    val deviseId : Long? = null,
    @Column("title")
    val title: String,
    @Column("description")
    val description: String? = "",
    @Column("price")
    val price: Double? = null,
    @Column("room_meet")
    val roomMeet: Int? = 0,
    @Column("number_piece")
    val numberPiece: Int? = 0,
    @Column("is_equip")
    val isEquip: Boolean = false,
    @Column("address")
    val address: String,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

fun BureauEntity.toDomain() = Bureau(
    id = this.id,
    userId = this.userId,
    deviseId = this.deviseId,
    title = this.title,
    description = this.description,
    price = this.price,
    roomMeet = this.roomMeet,
    numberPiece = this.numberPiece,
    isEquip = this.isEquip,
    address = this.address,
    createdAt = this.createdAt
)