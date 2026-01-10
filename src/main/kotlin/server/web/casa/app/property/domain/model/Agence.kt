package server.web.casa.app.property.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import server.web.casa.app.property.infrastructure.persistence.entity.AgenceEntity

class Agence(
    val id: Long? = null,
    val userId: Long,
    val name: String,
    val description : String = "",
    val phone1 : String = "",
    val phone2 : String = "",
    val address : String = "",
    var logo : String? = "",
)
class AgenceDTO(
    @NotNull
    val userId: Long,
    @NotNull
    val name: String,
    val description : String = "",
    @NotNull
    val phone1 : String = "",
    val phone2 : String = "",
    @NotNull
    val address : String = "",
    @NotNull
    var logo : String? = "",
)

fun AgenceDTO.toDomain() = Agence(
    userId = this.userId,
    name = this.name,
    description = this.description,
    phone1 = this.phone1,
    phone2 = this.phone2,
    address = this.address,
    logo = this.logo
)
fun Agence.toEntity() = AgenceEntity(
    this.id,
    this.userId,
    this.name,
    this.description,
    this.phone1,
    this.phone2,
    this.address,
    this.logo
)