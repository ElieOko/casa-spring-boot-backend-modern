package server.web.casa.app.actor.application.command

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import server.web.casa.app.actor.infrastructure.persistence.entity.TypeCardEntity
import server.web.casa.app.actor.infrastructure.persistence.repository.TypeCardRepository

@Component
@Order(2)
class CommandLineActorComponent(
   private val typeCardRepository: TypeCardRepository
) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(this::class.java)
    override fun run(vararg args: String) {
        createCard()
    }

    fun createCard(){
        typeCardRepository.saveAll(
            listOf(
                TypeCardEntity(name = "Carte d'électeur", typeCardId = 0),
                TypeCardEntity(name = "Visa", typeCardId = 0)
            )
        )
        log.info("save card")
    }
}