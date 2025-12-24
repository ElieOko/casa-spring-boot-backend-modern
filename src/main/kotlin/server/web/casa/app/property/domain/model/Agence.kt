package server.web.casa.app.property.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import server.web.casa.app.property.infrastructure.persistence.entity.AgenceEntity

class Agence(
    @Null
    @JsonIgnore
    val id: Long? = null,
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