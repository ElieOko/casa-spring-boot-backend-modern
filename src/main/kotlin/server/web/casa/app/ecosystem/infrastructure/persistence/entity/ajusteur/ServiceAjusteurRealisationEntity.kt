package server.web.casa.app.ecosystem.infrastructure.persistence.entity.ajusteur

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
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.architect.ServiceArchitectEntity

@Entity
@Table(name = "service_ajusteur_realisations")
class ServiceAjusteurRealisationEntity (
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,
    @ManyToOne
    @JoinColumn("service_ajusteur_id")
    @JsonBackReference
    var service : ServiceAjusteurEntity,
    @Column(name = "name")
    val name : String,
    @Column(name = "path_image")
    val path : String
)

fun ServiceAjusteurRealisationEntity.toDomain() = AjusteurRealisation(
    id = this.id,
    service = this.service.toDomain(),
    name = this.name,
    path = this.path
)