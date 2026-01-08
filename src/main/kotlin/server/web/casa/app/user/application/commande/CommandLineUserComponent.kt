package server.web.casa.app.user.application.commande

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
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
import server.web.casa.app.user.infrastructure.persistence.entity.AccountEntity
import server.web.casa.app.user.infrastructure.persistence.entity.TypeAccountEntity
import server.web.casa.app.user.infrastructure.persistence.repository.AccountRepository
import server.web.casa.app.user.infrastructure.persistence.repository.TypeAccountRepository

@Component
@Order(4)
@Profile("dev")
class CommandLineUserComponent(
    @Value("\${spring.application.version}")  private val version: String,
    val typeAccountRepository: TypeAccountRepository,
    val accountRepository: AccountRepository,
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
                runBlocking {
//                    createAccount()
//                    createTypeAccountAll()
                }
//            createTypeAccount()
//            createUser()
//            getAllTypeAccount()
        }
        catch (e : Exception){
            log.info(e.message)
        }

    }
    suspend fun createTypeAccountAll(){
      val data = typeAccountRepository.saveAll<TypeAccountEntity>(
          listOf(
          TypeAccountEntity(name = "Client"),
          TypeAccountEntity(name = "Proprietaire"),
          TypeAccountEntity(name = "Prestataire de service"))
        ).toList()
        log.info("save type account all ${data.size}")
    }
    suspend fun createAccount(){
            val store = accountRepository.saveAll<AccountEntity>(
            listOf(
                AccountEntity(name = "commissionnaire", typeAccountId = 2),//2
                AccountEntity(name = "bailleur", typeAccountId = 2),//3
                AccountEntity(name = "gerant", typeAccountId = 2),//3
                AccountEntity(name = "locataire", typeAccountId = 1),//4
                AccountEntity(name = "majordome", typeAccountId = 3),//5
                AccountEntity(name = "demenagement",typeAccountId = 3),//6
                AccountEntity(name = "electricien",typeAccountId = 3),//7
                AccountEntity(name = "salubrité",typeAccountId = 3),//8
                AccountEntity(name = "peintre",typeAccountId = 3),//9
                AccountEntity(name = "carrelleur",typeAccountId = 3),//10
                AccountEntity(name = "menusier",typeAccountId = 3),//11
                AccountEntity(name = "frigoriste",typeAccountId = 3),//12
                AccountEntity(name = "ajusteur",typeAccountId = 3),//13
                AccountEntity(name = "architect",typeAccountId = 3),//14
                AccountEntity(name = "chauffeur",typeAccountId = 3),//15
                AccountEntity(name = "plombier",typeAccountId = 3),//16
                AccountEntity(name = "maçon",typeAccountId = 3),//18
            )
        ).toList()
        log.info("Enregistrement réussi avec succès ${store.size}")
//        log.info("Enregistrement réussi avec succès")
    }
//
//    fun getAllTypeAccount(){
//        typeAccountRepository.findAll().forEach { accountEntity ->
//            log.info("${accountEntity.name} | ${accountEntity.typeAccountId}")
//        }
//    }
//
//suspend fun createUser(){
//       val account = typeAccountService.findByIdTypeAccount(1)
////       val city = cityService.findByIdCity(1)
//       val userSystem = User(
//            password = "1234",
//            typeAccount = account,
//            email = "elieoko100@gmail.com",
//            phone = "0827824163",
//            city = "Kinshasa",
//           country = "CD"
//           )
//        val data = authService.register(userSystem, accountItems)
//        log.info("Enregistrement réussi avec succès***${data.first?.phone}")
//    }
}