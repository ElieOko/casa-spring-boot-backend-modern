package server.web.casa.app.actor.application.command

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import server.web.casa.app.actor.infrastructure.persistence.entity.TypeCardEntity
import server.web.casa.app.actor.infrastructure.persistence.repository.TypeCardRepository
import server.web.casa.utils.Mode

@Component
@Order(2)
@Profile(Mode.DEV)
class CommandLineActorComponent(
   private val typeCardRepository: TypeCardRepository
) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(this::class.java)
    override fun run(vararg args: String) {
        try {
//            createCard()
        }
        catch (e : Exception){

        }

    }
//
    fun createCard(){
        typeCardRepository.saveAll(
            listOf(
                TypeCardEntity(name = "Carte d'électeur", typeCardId = 0),
                TypeCardEntity(name = "Visa", typeCardId = 0),
                TypeCardEntity(name = "Autres", typeCardId = 0)
            )
        )
        log.info("save card")
    }
}