package server.web.casa.app.ecosystem.infrastructure.persistence.entity.menusier

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "service_menusiers")
class ServiceMenusierEntity(
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val serviceMenusierId : Long,
    @Column("experience")
    val experience : String,
    @Column("description")
    val description : String,
    @Column(name = "address")
    val address : String,
    @Column(name = "communeValue", nullable = true)
    val communeValue: String? = "",
    @Column(name = "quartierValue", nullable = true)
    val quartierValue: String? = "",
    @Column(name = "cityValue", nullable = true)
    val cityValue: String? = "",
    @Column(name = "countryValue", nullable = true)
    val countryValue: String? = "",

)
//https://www.amazon.com.be/-/en/BenQ-RD320UA-3840x2160-Programming-Monitor/dp/B0DHRWJ88S/ref=sr_1_1?crid=38J8MYD3I892&dib=eyJ2IjoiMSJ9.lHCWM-IwPe0cSr58G6IyXd9TdUPMyPAoaDwLd98HyT3iEciozI8V2tDDKpjs36hStrC5zwZpOTi9wjqT7bc4YfICjeafgiU8A5t9JeFGA__wcUz3ubJkYqi7V2HSb_-FAOfFglQHaIJCgaSOJfw3lmlC9sJl0Ix1AK7Fr86rUULNhngIQ2WCEdkWjzh5oPfT6I0VGdVuaZYcWLeYwMKDnCSSGMKp81b-X3PZmlNMtx1uo4FFoNnKAiicH71ASo9PU0lUJO0lKjg85yHLH3WKNP5vozTbXSJjCn2NNFfD4cM.Xb4ydFK6Z6_rubMjoSVhAR9efIloTk4V-PRCS0xDEuQ&dib_tag=se&keywords=benq+RD320UA&qid=1765126081&sprefix=benq+monitor%2Caps%2C1917&sr=8-1