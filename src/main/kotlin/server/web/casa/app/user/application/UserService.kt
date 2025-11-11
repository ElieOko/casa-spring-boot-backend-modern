package server.web.casa.app.user.application

import server.web.casa.app.user.domain.model.User
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.address.infrastructure.persistence.mapper.CityMapper
import server.web.casa.app.user.domain.model.TypeAccount
import server.web.casa.app.user.domain.model.request.UserRequestChange
import server.web.casa.app.user.infrastructure.persistence.mapper.TypeAccountMapper
import server.web.casa.utils.Mode
import kotlin.time.ExperimentalTime

@Service
@Profile(Mode.DEV)
class UserService(
    private val repository: UserRepository,
    private val service: TypeAccountService,
    private val mapper: UserMapper,
    private val mapperAccount: TypeAccountMapper,
    private val mapperCity: CityMapper,
) {
    val name = "utilisateur"
    @OptIn(ExperimentalTime::class)
    suspend fun createUser(user: User) : User? {

        val entityToSave = UserEntity(
            password = user.password,
            typeAccount = mapperAccount.toEntity(user.typeAccount) ,
            email = user.email,
            phone = user.phone,
            username = user.username,
            city = mapperCity.toEntity(user.city)
        )
        val savedEntity = repository.save(entityToSave)
        return mapper.toDomain(savedEntity)
    }

    suspend fun findAllUser() : List<User?> {
        val allEntityUser = repository.findAll()
        return allEntityUser.map {
            mapper.toDomain(it)
        }.toList()
    }

    suspend fun findIdUser(id : Long) : User?{
        val userEntity = repository.findById(id).orElse(null)
        return mapper.toDomain(userEntity)
//        }?: throw EntityNotFoundException("Aucun $name avec cet identifiant $id")

    }
    fun findUsernameOrEmail(identifier : String): User? {
        return mapper.toDomain( repository.findByPhoneOrEmail(identifier))
    }

    fun findId(id : Long) : User?{
        val userEntity = repository.findById(id).orElse(null)
        return mapper.toDomain(userEntity)
//        }?: throw EntityNotFoundException("Aucun $name avec cet identifiant $id")

    }


    @OptIn(ExperimentalTime::class)
    suspend fun updateUser(
        id: Long,
        user: UserRequestChange
    ): User ?{
      val userState =  repository.findById(id).orElse(null)
      if (userState.email == user.email) {
          userState.city = mapperCity.toEntity(user.city)
          val updatedUser = repository.save(userState)
          return mapper.toDomain(updatedUser)
      }
      else{
          val state = repository.findByPhoneOrEmail(user.email)
          if(state != null) {
              throw ResponseStatusException(HttpStatus.CONFLICT, "Cette adresse email est déjà utilisé.")
          }
          userState.email = user.email
          userState.city = mapperCity.toEntity(user.city)
          val updatedUser = repository.save(userState)
          return mapper.toDomain(updatedUser)
      }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun updateUsername(
        id: Long,
        username : String
    ): User ?{
        val userState =  repository.findById(id).orElse(null)
        userState.username = username
        val updatedUser = repository.save(userState)
        return mapper.toDomain(updatedUser)
    }

    suspend fun deleteUser(id : Long) : Boolean{
        if (!repository.existsById(id)){
            EntityNotFoundException("Aucun $name avec cet identifiant $id")
        }
        repository.deleteById(id)
        return true
    }
     fun account(id: Long): TypeAccount? {
        return service.findByIdType(id)
    }
}