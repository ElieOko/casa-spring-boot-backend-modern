package server.web.casa.app.ecosystem.domain.model.task


import org.threeten.bp.LocalDateTime
import server.web.casa.app.address.domain.model.City
import server.web.casa.app.address.domain.model.Commune
import server.web.casa.app.address.domain.model.Quartier
import server.web.casa.app.address.infrastructure.persistence.mapper.toEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.ServiceAjusteurEntity
import server.web.casa.app.payment.domain.model.Devise
import server.web.casa.app.payment.domain.model.toEntity
import server.web.casa.app.user.domain.model.UserDto
import server.web.casa.app.user.infrastructure.persistence.mapper.toEntityToDto

data class AjusteurTask(
    val id : Long,
    val user : UserDto,
    val devise : Devise,
    val experience : String,
    val description : String?="",
    val address : String?="",
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    val minPrice: Double = 0.0,
    val maxPrice: Double = 0.0,
    val isCertified: Boolean = false,
    val isActive: Boolean = false,
    var city: City?,
    var quartier: Quartier?,
    var commune: Commune?,
    val dataCreated: LocalDateTime = LocalDateTime.now(),
)

data class AjusteurTaskRequest(
    val userId : Long,
    val deviseId : Long,
    val experience : String,
    val description : String?="",
    val address : String?="",
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    val minPrice: Double = 0.0,
    val maxPrice: Double = 0.0,
)
fun AjusteurTask.toEntity() = ServiceAjusteurEntity(
    id = this.id,
    user = this.user.toEntityToDto(),
    devise = this.devise.toEntity(),
    experience = this.experience,
    description = this.description,
    address = this.address,
    communeValue = this.communeValue,
    quartierValue = this.quartierValue,
    cityValue = this.cityValue,
    countryValue = this.countryValue,
    minPrice = this.minPrice,
    maxPrice = this.maxPrice,
    isCertified = this.isCertified,
    isActive = this.isActive,
    dataCreated = this.dataCreated,
    city = this.city?.toEntity(),
    commune = this.commune?.toEntity(),
    quartier = this.quartier?.toEntity()
)