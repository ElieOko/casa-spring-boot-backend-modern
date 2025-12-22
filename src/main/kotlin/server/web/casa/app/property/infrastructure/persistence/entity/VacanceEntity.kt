package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.property.domain.model.Vacance
import java.time.LocalDateTime

@Table("vacances")
data class VacanceEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("agence_id")
    val agenceId : Long? = null,
    @Column("user_id")
    val userId : Long? = null,
    @Column("title")
    val title: String,
    @Column("description")
    val description: String? = "",
    @Column("note")
    val note: String? = "",
    @Column("date_vacance")
    val dateVacance: String? = null,
    @Column("heure_vacance")
    val heureVacance : String? = null,
    @Column("price")
    val price: Double? = null,
    @Column("price_combo")
    val priceCombo: String? = "",
    @Column("offre")
    val offre: String? = "",
    @Column("capacity")
    val capacity: Int? = 0,
    @Column("address")
    val address: String,
    @Column("city_value")
    val cityValue: String? = "",
    @Column("country_value")
    val countryValue: String? = "",
    @Column("is_available")
    var isAvailable: Boolean = true,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column("updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

fun VacanceEntity.toDomain() = Vacance(
    id = this.id,
    agenceId = this.agenceId,
    userId = this.userId,
    title = this.title,
    description = this.description,
    note = this.note,
    dateVacance = this.dateVacance,
    heureVacance = this.heureVacance,
    price = this.price,
    priceCombo = this.priceCombo,
    capacity = this.capacity,
    address = this.address,
    cityValue = this.cityValue,
    countryValue = this.countryValue,
    isAvailable = this.isAvailable,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

