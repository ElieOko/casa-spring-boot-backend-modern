package server.web.casa.app.ecosystem.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.jetbrains.annotations.NotNull
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.PrestationEntity
import java.time.LocalDateTime

class Prestation(
    var id: Long? = null,
    val userId : Long,
    val serviceId : Long,
    val deviseId : Long? = null,
    val title : String = "",
    val description : String?="",
    val experience : String = "",
    val plageJourPrestation : String = "",
    val plageHeurePrestation : String = "",
    val minPrice: Double = 0.0,
    val maxPrice: Double = 0.0,
    val address: String? = null,
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    val cityId: Long? = null,
    val communeId: Long? = null,
    val quartierId: Long? = null,
    var isCertified: Boolean = true,
    val isActive: Boolean = true,
    val dateCreated: LocalDateTime = LocalDateTime.now(),
)

class PrestationDTO(
    @NotNull
    val userId : Long,
    @NotNull
    val serviceId : Long,
    val deviseId : Long? = null,
    @NotNull
    val title : String = "",
    val description : String?="",
    @NotNull
    val experience : String = "",
    val plageJourPrestation : String = "",
    val plageHeurePrestation : String = "",
    val minPrice: Double = 0.0,
    val maxPrice: Double = 0.0,
    val address: String? = "",
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    val cityId: Long? = null,
    val communeId: Long? = null,
    val quartierId: Long? = null,
)
fun PrestationDTO.toDomain()= Prestation(
    id = null,
    userId = this.userId,
    serviceId = this.serviceId,
    deviseId = this.deviseId,
    title = this.title,
    description = this.description,
    experience = this.experience,
    plageJourPrestation = this.plageJourPrestation,
    plageHeurePrestation = this.plageHeurePrestation,
    minPrice = this.minPrice,
    maxPrice = this.maxPrice,
    address = this.address,
    communeValue = this.communeValue,
    quartierValue = this.quartierValue,
    cityValue = this.cityValue,
    countryValue = this.countryValue,
    cityId = this.cityId,
    communeId = this.communeId,
    quartierId = this.quartierId,
    isCertified = false,
    isActive = true,
    dateCreated = LocalDateTime.now()
)
fun Prestation.toEntity() = PrestationEntity(
    id = this.id,
    userId = this.userId,
    serviceId = this.serviceId,
    deviseId = this.deviseId,
    title = this.title,
    description = this.description,
    experience = this.experience,
    plageJourPrestation = this.plageJourPrestation,
    plageHeurePrestation = this.plageHeurePrestation,
    minPrice = this.minPrice,
    maxPrice = this.maxPrice,
    address = this.address,
    communeValue = this.communeValue,
    quartierValue = this.quartierValue,
    cityValue = this.cityValue,
    countryValue = this.countryValue,
    cityId = this.cityId,
    communeId = this.communeId,
    quartierId = this.quartierId,
    isCertified = this.isCertified,
    isActive = this.isActive,
    dateCreated = this.dateCreated
)