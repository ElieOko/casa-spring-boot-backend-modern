package server.web.casa.app.actor.infrastructure.persistence.entity


import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "persons")
class PersonEntity(
    @Id
    val id : Long? = 0,
    val firstName : String,
    val lastName : String,
    val fullName : String,
    val address : String? = "",
    val images : String? = null,
    val cardFront : String?,
    val cardBack : String? = null,
    val numberCard : String? = null,
    val userId : Long?,
    val parrainId : Long? = null,
    val typeCard : Long? = null,
)