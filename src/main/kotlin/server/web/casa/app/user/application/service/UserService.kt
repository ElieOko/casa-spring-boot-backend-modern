package server.web.casa.app.user.application.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import server.web.casa.app.user.domain.model.User
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
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
            email = user.email,
            phone = user.phone,
            username = user.username,
            city = user.city,
            country = user.country
        )
        val savedEntity = repository.save(entityToSave)
        return savedEntity.toDomain()
    }

    suspend fun findAllUser() : Flow<UserDto> {
        val allEntityUser = repository.findAll()
        return allEntityUser.map { it.toDomain() }
    }

    suspend fun findIdUser(id : Long) : UserDto? {
        val userEntity = repository.findById(id)
        return userEntity?.toDomain()
//        }?: throw EntityNotFoundException("Aucun $name avec cet identifiant $id")

    }
    suspend fun findUsernameOrEmail(identifier : String): UserDto? {
        return  repository.findByPhoneOrEmail(identifier)?.toDomain()
    }

    suspend fun findId(id : Long) : UserDto? {
        val userEntity = repository.findById(id)
        return userEntity?.toDomain()
    }


    @OptIn(ExperimentalTime::class)
    suspend fun updateUser(
        id: Long,
        user: UserRequestChange
    ): UserDto ?{
      val userState =  repository.findById(id)
      if (userState?.email == user.email) {
          userState.city = user.city
          val updatedUser = repository.save(userState)
          return updatedUser.toDomain()
      }
      else{
          val state = repository.findByPhoneOrEmail(user.email)
          if(state != null) {
              throw ResponseStatusException(HttpStatus.CONFLICT, "Cette adresse email est déjà utilisé.")
          }
          userState?.email = user.email
          userState?.city = user.city
          val updatedUser = repository.save(userState!!)
          return updatedUser.toDomain()
      }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun updateUsername(
        id: Long,
        username : String
    ): UserDto ?{
        val userState =  repository.findById(id)
        if (userState != null) {
            userState.username = username
            val updatedUser = repository.save(userState)
            return updatedUser.toDomain()
        }
        return null
    }

    suspend fun deleteUser(id : Long) : Boolean{
        if (!repository.existsById(id)){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun $name avec cet identifiant $id")
            //NotFoundException("Aucun $name avec cet identifiant $id")
        }
        repository.deleteById(id)
        return true
    }

}