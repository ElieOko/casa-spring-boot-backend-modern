package server.web.casa.app.actor.application.command

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import server.web.casa.app.actor.infrastructure.persistence.entity.TypeCardEntity
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
//            log.info(normalizeAndValidatePhoneNumberUniversal("00243827824163"))
//            log.info("*************")
//            isEmailValid("elieoko100@gmail.com")
//            log.info(normalizeAndValidatePhoneNumberUniversal("+243837824163"))
//            log.info("*************")
//            log.info(normalizeAndValidatePhoneNumberUniversal("+2438278241630"))
//            log.info("*************")
//            log.info(normalizeAndValidatePhoneNumberUniversal("+24382782416e"))
//            log.info("*************")
//            log.info(normalizeAndValidatePhoneNumberUniversal("+242827824163"))
//            log.info("*************")
//            log.info(normalizeAndValidatePhoneNumberUniversal("+242065208881"))
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