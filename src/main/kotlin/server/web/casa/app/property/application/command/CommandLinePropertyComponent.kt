package server.web.casa.app.property.application.command

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import server.web.casa.app.property.infrastructure.persistence.entity.*
import server.web.casa.app.property.infrastructure.persistence.repository.*
import server.web.casa.utils.Mode

@Component
@Order(6)
@Profile(Mode.DEV)
class CommandLinePropertyComponent(
   private val typePropertyRepository: PropertyTypeRepository,
   private val feature : FeatureRepository
) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(this::class.java)
    override fun run(vararg args: String) {
        try {
            createTypeProperty()
            createFeature()
        }
        catch (e : Exception){

        }
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