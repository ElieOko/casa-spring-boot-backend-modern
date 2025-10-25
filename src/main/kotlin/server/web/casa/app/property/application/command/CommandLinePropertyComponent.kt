package server.web.casa.app.property.application.command

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import server.web.casa.app.address.application.service.CityService
import server.web.casa.app.address.application.service.CommuneService
import server.web.casa.app.address.infrastructure.persistence.repository.CityRepository
import server.web.casa.app.address.infrastructure.persistence.repository.CommuneRepository
import server.web.casa.app.property.application.service.FeatureService
import server.web.casa.app.property.application.service.PropertyImageKitchenService
import server.web.casa.app.property.application.service.PropertyImageLivingRoomService
import server.web.casa.app.property.application.service.PropertyImageRoomService
import server.web.casa.app.property.application.service.PropertyImageService
import server.web.casa.app.property.application.service.PropertyService
import server.web.casa.app.property.application.service.PropertyTypeService
import server.web.casa.app.property.domain.model.Feature
import server.web.casa.app.property.domain.model.Property
import server.web.casa.app.property.domain.model.PropertyImage
import server.web.casa.app.property.domain.model.PropertyImageKitchen
import server.web.casa.app.property.domain.model.PropertyImageLivingRoom
import server.web.casa.app.property.domain.model.PropertyImageRoom
import server.web.casa.app.property.domain.model.request.PropertyImageRequest
import server.web.casa.app.property.domain.model.request.PropertyRequest
import server.web.casa.app.property.infrastructure.persistence.entity.*
import server.web.casa.app.property.infrastructure.persistence.repository.*
import server.web.casa.app.user.application.UserService
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import server.web.casa.utils.Mode

@Component
@Order(6)
@Profile(Mode.DEV)
class CommandLinePropertyComponent(
   private val typePropertyRepository: PropertyTypeRepository,
  private val feature: FeatureRepository,
   private val userRepository: UserRepository,
   private val propertyRepository: PropertyRepository,
   private val communeRepository: CommuneRepository,
   private val cityRepository: CityRepository


) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(this::class.java)
    override fun run(vararg args: String) {
        try {
//            createTypeProperty()
//            createFeature()
//            createProperty()
        }
        catch (e : Exception){

        }
    }
    fun createProperty() {
        log.info("property*********************")
        val city = cityRepository.findById(1).orElse(null)
        log.info("in side 0*****")
        val user = userRepository.findById(1).orElse(null)
        log.info("in side 1*****")
        val propertyType = typePropertyRepository.findById(1).orElse(null)
        log.info("in side 2*****")
        val commune = communeRepository.findById(1).orElse(null)
        log.info("in side*****")
            val property = PropertyEntity(
                title = "Maison de rêve",
                description = "",
                price = 5000.0,
                surface = 20.0,
                rooms = 1,
                bedrooms = 1,
                kitchen = 1,
                livingRoom = 1,
                bathroom = 1,
                floor = 1,
                address = "Gazo",
                city = city!!,
                postalCode = "",
                commune = commune,
                features = emptyList(),
                quartier = "",
                sold = true,
                transactionType = "",
                propertyType = propertyType,
                user = user,
                latitude = 0.0,
                longitude = 0.0,
                propertyImage = mutableSetOf(),
                propertyImageRoom = mutableSetOf(),
                propertyImageLivingRoom =mutableSetOf(),
                propertyImageKitchen = mutableSetOf(),
                reservation = emptyList()
            )
            val result = propertyRepository.save(property)
            log.info("***property success******")
    }
    fun createTypeProperty(){
        typePropertyRepository.saveAll(
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
                )
            )
        )
        log.info("save type property")
    }

    fun createFeature(){
        feature.saveAll(
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
        )
        log.info("save features")
    }
}