package server.web.casa.app.user.application.commande

import org.hibernate.exception.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import server.web.casa.app.user.infrastructure.persistence.entity.TypeAccountEntity
import server.web.casa.app.user.infrastructure.persistence.repository.TypeAccountRepository

@Component
@Order(4)
@Profile("dev")
class CommandLineUserComponent(
    @Value("\${spring.application.version}")  private val version: String,
    val typeAccountRepository: TypeAccountRepository
) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun run(vararg args: String) {
        log.info("commande executor **User")
        log.info(this::class.simpleName)
        log.info(version)
        try {
            createTypeAccount()
            //getAllTypeAccount()
        }
        catch (e : ConstraintViolationException){
            log.info(e.message)
        }

    }
    fun createTypeAccount(){
        val store: List<TypeAccountEntity?> = typeAccountRepository.saveAll<TypeAccountEntity>(
            listOf(
                TypeAccountEntity(name = "admin"),
                TypeAccountEntity(name = "commissionnaire"),
                TypeAccountEntity(name = "bailleur"),
                TypeAccountEntity(name = "locataire"),
            )
        )
        log.info("Enregistrement réussi avec succès")
        log.info("@".repeat(12))
        store.forEach { typeAccountRepository->
            log.info("${typeAccountRepository?.name} ${typeAccountRepository?.typeAccountId}")
//            log.info()
        }
//        log.info("Enregistrement réussi avec succès")
    }

    fun getAllTypeAccount(){
        typeAccountRepository.findAll().forEach { accountEntity ->
            log.info("${accountEntity.name} | ${accountEntity.typeAccountId}")
        }
    }
}