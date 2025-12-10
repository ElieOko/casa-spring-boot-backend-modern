package server.web.casa.app.ecosystem.infrastructure.persistence.entity.plombier

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
import server.web.casa.app.ecosystem.domain.model.realisation.PlombierRealisation
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.ajusteur.ServiceAjusteurEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.architect.ServiceArchitectEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.carreleur.ServiceCarreleurEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.frigoriste.ServiceFrigoristeEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.menusier.ServiceMenusierRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.menusier.toDomain

@Entity
@Table(name = "service_plombier_realisations")
class ServicePlombierRealisationEntity (
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,
    @ManyToOne
    @JoinColumn("service_plombier_id")
    @JsonBackReference
    var service : ServicePlombierEntity,
    @Column(name = "name")
    val name : String,
    @Column(name = "path_image")
    val path : String
)

fun ServicePlombierRealisationEntity.toDomain() = PlombierRealisation(
    id = this.id,
    service = this.service.toDomain(),
    name = this.name,
    path = this.path
)