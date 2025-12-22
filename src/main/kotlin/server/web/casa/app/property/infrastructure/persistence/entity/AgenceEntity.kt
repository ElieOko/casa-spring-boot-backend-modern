package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.property.domain.model.Agence

@Table(name = "agencies")
class AgenceEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("user_id")
    val userId: Long,
    @Column("name")
    val name: String,
    @Column("description")
    val description : String = "",
    @Column("phone_1")
    val phone1 : String = "",
    @Column("phone_2")
    val phone2 : String = "",
    @Column("address")
    val address : String = "",
    @Column("logo")
    val logo : String = "",
)

fun AgenceEntity.toDomain() = Agence(
    id = this.id,
    name = this.name,
    description = this.description,
    phone1 = this.phone1,
    phone2 = this.phone2,
    address = this.address,
    logo = this.logo,
    userId = this.userId
)