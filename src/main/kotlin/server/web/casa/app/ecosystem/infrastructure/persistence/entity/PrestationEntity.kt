package server.web.casa.app.ecosystem.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.ecosystem.domain.model.Prestation
import java.time.LocalDateTime

@Table(name = "prestations")
class PrestationEntity(
    @Id
    @Column("id")
    var id: Long? = null,
    @Column("user_id")
    val userId : Long,
    @Column("service_id")
    val serviceId : Long,
    @Column("devise_id")
    val deviseId : Long?,
    @Column("title")
    val title : String = "",
    @Column("description")
    val description : String?="",
    @Column("profile")
    val profile : String?="",
    @Column("experience")
    val experience : String = "",
    @Column("plage_jour_prestation")
    val plageJourPrestation : String = "",
    @Column("plage_heure_prestation")
    val plageHeurePrestation : String = "",
    @Column("min_price")
    val minPrice: Double = 0.0,
    @Column("max_price")
    val maxPrice: Double = 0.0,
    @Column("address")
    val address: String? = null,
    @Column("commune_value")
    val communeValue: String? = "",
    @Column("quartier_value")
    val quartierValue: String? = "",
    @Column("city_value")
    val cityValue: String? = "",
    @Column("country_value")
    val countryValue: String? = "",
    @Column("city_id")
    val cityId: Long? = null,
    @Column("commune_id")
    val communeId: Long? = null,
    @Column("quartier_id")
    val quartierId: Long? = null,
    @Column("is_certified")
    var isCertified: Boolean = true,
    @Column("is_active")
    val isActive: Boolean = true,
    @Column("date_created")
    val dateCreated: LocalDateTime = LocalDateTime.now(),
    )

fun PrestationEntity.toDomain() = Prestation(
    id = this.id,
    userId = this.userId,
    serviceId = this.serviceId,
    deviseId = this.deviseId,
    profile = this.profile,
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