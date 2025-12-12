package server.web.casa.app.user.application.service

import server.web.casa.app.user.domain.model.User
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.user.domain.model.*
import server.web.casa.app.user.domain.model.request.UserRequestChange
import server.web.casa.app.user.infrastructure.persistence.mapper.*
import server.web.casa.utils.Mode
import kotlin.time.ExperimentalTime

@Service
@Profile(Mode.DEV)
class UserService(
    private val repository: UserRepository,
    private val service: TypeAccountService,
) {
    val name = "utilisateur"
    @OptIn(ExperimentalTime::class)
    suspend fun createUser(user: User) : UserDto? {
        val entityToSave = UserEntity(
            password = user.password,
            typeAccountUserId = user.accountId() ,
            email = user.email,
            phone = user.phone,
            username = user.username,
            city = user.city,
            country = user.country
        )
        val savedEntity = repository.save(entityToSave)
        return savedEntity.toDomain()
    }

    suspend fun findAllUser() : List<UserDto?> {
        val allEntityUser = repository.findAll()
        return allEntityUser.map {
            it.toDomain()
        }.toList()
    }

    suspend fun findIdUser(id : Long) : UserDto? {
        val userEntity = repository.findById(id).orElse(null)
        return userEntity.toDomain()
//        }?: throw EntityNotFoundException("Aucun $name avec cet identifiant $id")

    }
    fun findUsernameOrEmail(identifier : String): UserDto? {
        return  repository.findByPhoneOrEmail(identifier)?.toDomain()
    }

    fun findId(id : Long) : UserDto? {
        val userEntity = repository.findById(id).orElse(null)
        return userEntity.toDomain()
    }


    @OptIn(ExperimentalTime::class)
    suspend fun updateUser(
        id: Long,
        user: UserRequestChange
    ): UserDto ?{
      val userState =  repository.findById(id).orElse(null)
      if (userState.email == user.email) {
          userState.city = user.cityId
          val updatedUser = repository.save(userState)
          return updatedUser.toDomain()
      }
      else{
          val state = repository.findByPhoneOrEmail(user.email)
          if(state != null) {
              throw ResponseStatusException(HttpStatus.CONFLICT, "Cette adresse email est déjà utilisé.")
          }
          userState.email = user.email
          userState.city = user.city
          val updatedUser = repository.save(userState)
          return updatedUser.toDomain()
      }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun updateUsername(
        id: Long,
        username : String
    ): UserDto ?{
        val userState =  repository.findById(id).orElse(null)
        userState.username = username
        val updatedUser = repository.save(userState)
        return updatedUser.toDomain()
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