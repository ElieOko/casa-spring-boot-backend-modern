package server.web.casa.app.user.application.commande

import org.hibernate.exception.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import server.web.casa.app.address.application.service.CityService
import server.web.casa.app.user.application.service.AuthService
import server.web.casa.app.user.application.service.TypeAccountService
import server.web.casa.app.user.domain.model.User
import server.web.casa.app.user.infrastructure.persistence.entity.TypeAccountEntity
import server.web.casa.app.user.infrastructure.persistence.repository.TypeAccountRepository

@Component
@Order(4)
@Profile("dev")
class CommandLineUserComponent(
    @Value("\${spring.application.version}")  private val version: String,
    val typeAccountRepository: TypeAccountRepository,
    private val authService: AuthService,
    private val cityService: CityService,
    private val typeAccountService: TypeAccountService
) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun run(vararg args: String) {
        log.info("commande executor **User")
        log.info(this::class.simpleName)
        log.info(version)
        try {
//            createTypeAccount()
//            createUser()
//            getAllTypeAccount()
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
//
//    fun getAllTypeAccount(){
//        typeAccountRepository.findAll().forEach { accountEntity ->
//            log.info("${accountEntity.name} | ${accountEntity.typeAccountId}")
//        }
//    }
//
suspend fun createUser(){
       val account = typeAccountService.findByIdTypeAccount(1)
//       val city = cityService.findByIdCity(1)
       val userSystem = User(
            password = "1234",
            typeAccount = account,
            email = "elieoko100@gmail.com",
            phone = "0827824163",
            city = "Kinshasa",
           country = "CD"
           )
        val data = authService.register(userSystem)
        log.info("Enregistrement réussi avec succès***${data.first?.phone}")
    }
}