package server.web.casa.app.actor.application.command

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
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
//            runBlocking {
//                createCard()
//            }
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
suspend fun createCard(){
      val test = typeCardRepository.saveAll(
            listOf(
                TypeCardEntity(name = "Carte d'électeur", typeCardId = null),
                TypeCardEntity(name = "Visa", typeCardId = null),
                TypeCardEntity(name = "Permis", typeCardId = null),
                TypeCardEntity(name = "Permis", typeCardId = null)
            )
        ).toList()
        log.info("save card ${test.size}")
    }
}