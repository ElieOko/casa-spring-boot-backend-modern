package server.web.casa.app.payment.application.command

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import server.web.casa.app.address.infrastructure.persistence.repository.CityRepository
import server.web.casa.app.address.infrastructure.persistence.repository.CommuneRepository
import server.web.casa.app.payment.infrastructure.persistence.entity.DeviseEntity
import server.web.casa.app.payment.infrastructure.persistence.repository.DeviseRepository
import server.web.casa.app.property.infrastructure.persistence.entity.*
import server.web.casa.app.property.infrastructure.persistence.repository.*
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import server.web.casa.utils.Mode

@Component
@Order(9)
@Profile(Mode.DEV)
class CommandLineDeviseComponent(
   private val repository: DeviseRepository,

) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(this::class.java)
    override fun run(vararg args: String) {
        try {
//            createTypeProperty()
//            createFeature()
//            createProperty()
//            createDevise()
//            runBlocking {
//                createDevise()
//            }

        }
        catch (e : Exception){

        }
    }
    suspend fun createDevise(){
       val test = repository.saveAll(
            listOf(
                DeviseEntity(
                    name = "Franc Congolais",
                    code = "CDF",
                    tauxLocal = 2200.0
                ),
                DeviseEntity(
                    name = "Dollar Americain",
                    code = "USD",
                    tauxLocal = 1.0
                ),
            )
        ).toList()
        log.info("save devise ${test.size}")
    }

}