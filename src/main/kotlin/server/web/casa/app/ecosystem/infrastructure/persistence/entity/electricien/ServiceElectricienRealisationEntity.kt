package server.web.casa.app.ecosystem.infrastructure.persistence.entity.electricien

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import server.web.casa.app.ecosystem.domain.model.realisation.AjusteurRealisation
import server.web.casa.app.ecosystem.domain.model.realisation.ElectricienRealisation
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.ajusteur.ServiceAjusteurRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.ajusteur.toDomain

@Entity
@Table(name = "service_peintre_realisations")
class ServiceElectricienRealisationEntity (
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,
    @ManyToOne
    @JoinColumn("service_electricien_id")
    @JsonBackReference
    var service : ServiceElectricienEntity,
    @Column(name = "name")
    val name : String,
    @Column(name = "path_image")
    val path : String
)

fun ServiceElectricienRealisationEntity.toDomain() = ElectricienRealisation(
    id = this.id,
    service = this.service.toDomain(),
    name = this.name,
    path = this.path
)