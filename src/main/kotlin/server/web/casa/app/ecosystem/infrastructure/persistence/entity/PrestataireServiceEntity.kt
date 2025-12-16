package server.web.casa.app.ecosystem.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.threeten.bp.LocalDateTime
import server.web.casa.app.address.infrastructure.persistence.mapper.toDomain
import server.web.casa.app.payment.infrastructure.persistence.entity.toDomain
import server.web.casa.app.user.infrastructure.persistence.mapper.toDomain
//
//@Table(name = "PrestataireServices")
//class PrestataireServiceEntity(
//    @Id
//    val id : Long = 0,
//    val userId : Long?,
//    val deviseId : Long?,
//    val cityId : Long?,
//    val communeId : Long?,
//    val quartierId : Long?,
//    val experience : String,
//    val description : String?="",
//    val address : String?="",
//    val communeValue: String? = "",
//    val quartierValue: String? = "",
//    val cityValue: String? = "",
//    val countryValue: String? = "",
//    val minPrice: Double = 0.0,
//    val maxPrice: Double = 0.0,
//    var isCertified: Boolean = true,
//    val isActive: Boolean = true,
//    val dataCreated: LocalDateTime = LocalDateTime.now(),
//    )
//
//fun PrestataireServiceEntity.toDomain(): AjusteurTask = AjusteurTask(
//    id = this.id,
//    user = this.user!!.toDomain(),
//    devise = this.devise!!.toDomain(),
//    experience = this.experience,
//    description = this.description,
//    address = this.address,
//    communeValue = this.communeValue,
//    quartierValue = this.quartierValue,
//    cityValue = this.cityValue,
//    countryValue = this.countryValue,
//    minPrice = this.minPrice,
//    maxPrice = this.maxPrice,
//    isCertified = this.isCertified,
//    isActive = this.isActive,
//    dataCreated = this.dataCreated,
//    city = this.city?.toDomain(),
//    quartier = this.quartier?.toDomain(),
//    commune = this.commune?.toDomain()
//)
//https://www.amazon.com.be/-/en/BenQ-RD320UA-3840x2160-Programming-Monitor/dp/B0DHRWJ88S/ref=sr_1_1?crid=38J8MYD3I892&dib=eyJ2IjoiMSJ9.lHCWM-IwPe0cSr58G6IyXd9TdUPMyPAoaDwLd98HyT3iEciozI8V2tDDKpjs36hStrC5zwZpOTi9wjqT7bc4YfICjeafgiU8A5t9JeFGA__wcUz3ubJkYqi7V2HSb_-FAOfFglQHaIJCgaSOJfw3lmlC9sJl0Ix1AK7Fr86rUULNhngIQ2WCEdkWjzh5oPfT6I0VGdVuaZYcWLeYwMKDnCSSGMKp81b-X3PZmlNMtx1uo4FFoNnKAiicH71ASo9PU0lUJO0lKjg85yHLH3WKNP5vozTbXSJjCn2NNFfD4cM.Xb4ydFK6Z6_rubMjoSVhAR9efIloTk4V-PRCS0xDEuQ&dib_tag=se&keywords=benq+RD320UA&qid=1765126081&sprefix=benq+monitor%2Caps%2C1917&sr=8-1