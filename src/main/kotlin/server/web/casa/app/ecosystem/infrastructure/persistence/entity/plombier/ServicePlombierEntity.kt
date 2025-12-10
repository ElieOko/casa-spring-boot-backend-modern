package server.web.casa.app.ecosystem.infrastructure.persistence.entity.plombier

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import org.threeten.bp.LocalDateTime
import server.web.casa.app.address.infrastructure.persistence.entity.CityEntity
import server.web.casa.app.address.infrastructure.persistence.entity.CommuneEntity
import server.web.casa.app.address.infrastructure.persistence.entity.QuartierEntity
import server.web.casa.app.address.infrastructure.persistence.mapper.toDomain
import server.web.casa.app.ecosystem.domain.model.task.ElectricienTask
import server.web.casa.app.ecosystem.domain.model.task.PlombierTask
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.ajusteur.ServiceAjusteurRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.architect.ServiceArchitectRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.carreleur.ServiceCarreleurRealisationEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.electricien.ServiceElectricienEntity
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.frigoriste.ServiceFrigoristeRealisationEntity
import server.web.casa.app.payment.infrastructure.persistence.entity.DeviseEntity
import server.web.casa.app.payment.infrastructure.persistence.entity.toDomain
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import server.web.casa.app.user.infrastructure.persistence.mapper.toDomain

@Entity
@Table(name = "service_plombiers")
class ServicePlombierEntity(
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,
    @ManyToOne
    @JoinColumn("city_id", nullable = true)
    val city : CityEntity?,
    @ManyToOne
    @JoinColumn("commune_id", nullable = true)
    val commune : CommuneEntity?,
    @ManyToOne
    @JoinColumn("quartier_id", nullable = true)
    val quartier : QuartierEntity?,
    @ManyToOne
    @JoinColumn("user_id")
    val user : UserEntity?,
    @ManyToOne
    @JoinColumn("devise_id")
    val devise : DeviseEntity?,
    @OneToMany(mappedBy = "service")
    @JsonManagedReference
    val realisation : MutableSet<ServicePlombierRealisationEntity> = mutableSetOf(),
//    @OneToMany(mappedBy = "servicePeintre")
//    @JsonManagedReference
//    val serviceMenusierBackground : MutableSet<ServiceMenusierBackgroundEntity> = mutableSetOf(),
    @Column("experience")
    val experience : String,
    @Column("description", nullable = true)
    val description : String?="",
    @Column(name = "address", nullable = true)
    val address : String?="",
    @Column(name = "communeValue", nullable = true)
    val communeValue: String? = "",
    @Column(name = "quartierValue", nullable = true)
    val quartierValue: String? = "",
    @Column(name = "cityValue", nullable = true)
    val cityValue: String? = "",
    @Column(name = "countryValue", nullable = true)
    val countryValue: String? = "",
    @Column(name = "minPrice", nullable = true)
    val minPrice: Double = 0.0,
    @Column(name = "maxPrice", nullable = true)
    val maxPrice: Double = 0.0,
    @Column(name = "isCertified")
    var isCertified: Boolean = true,
    @Column(name = "isActive")
    val isActive: Boolean = true,
    @Column(name = "dataCreated")
    val dataCreated: LocalDateTime = LocalDateTime.now(),
    )

fun ServicePlombierEntity.toDomain() = PlombierTask(
    id = this.id,
    user = this.user!!.toDomain(),
    devise = this.devise!!.toDomain(),
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
    city = this.city?.toDomain(),
    quartier = this.quartier?.toDomain(),
    commune = this.commune?.toDomain()
)
//https://www.amazon.com.be/-/en/BenQ-RD320UA-3840x2160-Programming-Monitor/dp/B0DHRWJ88S/ref=sr_1_1?crid=38J8MYD3I892&dib=eyJ2IjoiMSJ9.lHCWM-IwPe0cSr58G6IyXd9TdUPMyPAoaDwLd98HyT3iEciozI8V2tDDKpjs36hStrC5zwZpOTi9wjqT7bc4YfICjeafgiU8A5t9JeFGA__wcUz3ubJkYqi7V2HSb_-FAOfFglQHaIJCgaSOJfw3lmlC9sJl0Ix1AK7Fr86rUULNhngIQ2WCEdkWjzh5oPfT6I0VGdVuaZYcWLeYwMKDnCSSGMKp81b-X3PZmlNMtx1uo4FFoNnKAiicH71ASo9PU0lUJO0lKjg85yHLH3WKNP5vozTbXSJjCn2NNFfD4cM.Xb4ydFK6Z6_rubMjoSVhAR9efIloTk4V-PRCS0xDEuQ&dib_tag=se&keywords=benq+RD320UA&qid=1765126081&sprefix=benq+monitor%2Caps%2C1917&sr=8-1