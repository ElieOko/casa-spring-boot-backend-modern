package server.web.casa.app.ecosystem.infrastructure.persistence.entity.salubrite

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import server.web.casa.app.ecosystem.domain.model.realisation.MenusierRealisation
import server.web.casa.app.ecosystem.domain.model.realisation.SalubriteRealisation
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.menusier.ServiceMenusierRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.menusier.toDomain

@Entity
@Table(name = "service_salubrite_realisations")
class ServiceSalubriteRealisationEntity (
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,
    @ManyToOne
    @JoinColumn("service_salubrite_id")
    @JsonBackReference
    var service : ServiceSalubriteEntity,
    @Column(name = "name")
    val name : String,
    @Column(name = "path_image")
    val path : String
)

fun ServiceSalubriteRealisationEntity.toDomain() = SalubriteRealisation(
    id = this.id,
    service = this.service.toDomain(),
    name = this.name,
    path = this.path
)