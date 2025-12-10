package server.web.casa.app.payment.infrastructure.persistence.entity

import jakarta.persistence.*
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.ajusteur.ServiceAjusteurEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.architect.ServiceArchitectEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.carreleur.ServiceCarreleurEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.electricien.ServiceElectricienEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.frigoriste.ServiceFrigoristeEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.macon.ServiceMaconEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.menusier.ServiceMenusierEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.peintre.ServicePeintreEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.salubrite.ServiceSalubriteEntity
import server.web.casa.app.payment.domain.model.Devise

@Entity
@Table(name = "devises")
class DeviseEntity(
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val deviseId : Long = 0,
    @OneToMany(mappedBy = "devise")
    val peintreService : List<ServicePeintreEntity> = emptyList(),
    @OneToMany(mappedBy = "devise")
    val menusierService : List<ServiceMenusierEntity> = emptyList(),
    @OneToMany(mappedBy = "devise")
    val architectService : List<ServiceArchitectEntity> = emptyList(),
    @OneToMany(mappedBy = "devise")
    val ajusteurService : List<ServiceAjusteurEntity> = emptyList(),
    @OneToMany(mappedBy = "devise")
    val frigoristeService : List<ServiceFrigoristeEntity> = emptyList(),
    @OneToMany(mappedBy = "devise")
    val carreleurService : List<ServiceCarreleurEntity> = emptyList(),
    @OneToMany(mappedBy = "devise")
    val electricienService : List<ServiceElectricienEntity> = emptyList(),
    @OneToMany(mappedBy = "devise")
    val salubriteService : List<ServiceSalubriteEntity> = emptyList(),
    @OneToMany(mappedBy = "devise")
    val maconService : List<ServiceMaconEntity> = emptyList(),
//    @OneToMany(mappedBy = "devise")
//    val majordomeService : List<ServiceMajordomeEntity> = emptyList(),
//    @OneToMany(mappedBy = "devise")
//    val demenagementService : List<ServiceDemenagementEntity> = emptyList(),
    @Column("name")
    val name : String,
    @Column("code")
    val code : String,
    @Column("tauxLocal", nullable = true)
    val tauxLocal : Double? = 22500.0,
)

fun DeviseEntity.toDomain() = Devise(
    deviseId = this.deviseId,
    name = this.name,
    code = this.code,
    tauxLocal = this.tauxLocal
)