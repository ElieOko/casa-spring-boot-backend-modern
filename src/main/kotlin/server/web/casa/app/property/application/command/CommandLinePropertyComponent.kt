package server.web.casa.app.property.application.command

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import server.web.casa.app.address.application.service.CommuneService
import server.web.casa.app.address.infrastructure.persistence.repository.CityRepository
import server.web.casa.app.address.infrastructure.persistence.repository.CommuneRepository
import server.web.casa.app.property.infrastructure.persistence.entity.*
import server.web.casa.app.property.infrastructure.persistence.repository.*
import server.web.casa.app.user.application.service.AccountService
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import server.web.casa.utils.Mode

@Component
@Order(6)
@Profile(Mode.DEV)
class CommandLinePropertyComponent(
    private val typePropertyRepository: PropertyTypeRepository,
    private val feature: FeatureRepository,
    private val communeService: CommuneService,
    private val userRepository: UserRepository,
    private val propertyRepository: PropertyRepository,
    private val communeRepository: CommuneRepository,
    private val cityRepository: CityRepository


) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(this::class.java)
    override fun run(vararg args: String) {
        try {

            runBlocking {
//                createTypeProperty()
//                createFeature()
              val test = communeService.findByIdCommune(1)
                log.info("Found commune ${test?.name}")
            }
//            createProperty()
        }
        catch (e : Exception){

        }
    }
//    fun createProperty() {
//        log.info("property*********************")
//        val city = cityRepository.findById(1).orElse(null)
//        log.info("in side 0*****")
//        val user = userRepository.findById(1).orElse(null)
//        log.info("in side 1*****")
//        val propertyType = typePropertyRepository.findById(1).orElse(null)
//        log.info("in side 2*****")
//        val commune = communeRepository.findById(1).orElse(null)
//        log.info("in side*****")
//            val property = PropertyEntity(
//                title = "Maison de rêve",
//                description = "",
//                price = 5000.0,
//                surface = 20.0,
//                rooms = 1,
//                bedrooms = 1,
//                kitchen = 1,
//                livingRoom = 1,
//                bathroom = 1,
//                floor = 1,
//                address = "Gazo",
//                city = city!!,
//                postalCode = "",
//                commune = commune,
//                features = emptyList(),
//                quartier = "",
//                sold = true,
//                transactionType = "",
//                propertyType = propertyType,
//                user = user,
//                latitude = 0.0,
//                longitude = 0.0,
//                propertyImage = mutableSetOf(),
//                propertyImageRoom = mutableSetOf(),
//                propertyImageLivingRoom =mutableSetOf(),
//                propertyImageKitchen = mutableSetOf(),
//                reservation = emptyList()
//            )
//            val result = propertyRepository.save(property)
//            log.info("***property success******")
//    }
   suspend fun createTypeProperty(){
      val data =  typePropertyRepository.saveAll(
            listOf(
                PropertyTypeEntity(
                    name = "Studio",
                    description = "",
                ),
                PropertyTypeEntity(
                    name = "Apparts",
                    description = "",
                ),
                PropertyTypeEntity(
                    name = "Maisons",
                    description = "",
                ),
                PropertyTypeEntity(
                    name = "Bureau",
                    description = "",
                ),
                PropertyTypeEntity(
                    name = "Hotels",
                    description = "",
                ),
                PropertyTypeEntity(
                    name = "Vacances",
                    description = "",
                ),
                PropertyTypeEntity(
                    name = "Espace funéraire",
                    description = "",
                ),
                PropertyTypeEntity(
                    name = "Salle de fête",
                    description = "",
                )
            )
        ).toList()
        log.info("save type property ${data.size}")
    }
//
suspend fun createFeature(){
      val data = feature.saveAll(
            listOf(
                FeatureEntity(name = "Wifi"),
                FeatureEntity(name = "Climatisation"),
                FeatureEntity(name = "Chauffage"),
                FeatureEntity(name = "Cuisine équipé"),
                FeatureEntity(name = "Lave-linge"),
                FeatureEntity(name = "Parking"),
                FeatureEntity(name = "Ascenceur"),
                FeatureEntity(name = "Piscine"),
                FeatureEntity(name = "Jardin"),
                FeatureEntity(name = "Terrasse"),
                FeatureEntity(name = "Gym"),
                FeatureEntity(name = "Sécurité"),
                FeatureEntity(name = "Animaux acceptés"),
            )
        ).toList()
        log.info("save features ${data.size}")
    }
}