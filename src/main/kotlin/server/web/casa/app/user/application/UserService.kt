package server.web.casa.app.user.application

import server.web.casa.app.user.domain.model.UserAuth
import server.web.casa.app.user.domain.model.User
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import server.web.casa.app.user.infrastructure.persistence.mapper.UserMapper
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.EntityResponse
import server.web.casa.app.address.infrastructure.persistence.mapper.CityMapper
import server.web.casa.app.user.infrastructure.persistence.mapper.TypeAccountMapper
import server.web.casa.utils.Mode
import kotlin.time.ExperimentalTime

@Service
@Profile(Mode.DEV)
class UserService(
    private val repository: UserRepository,
    private val mapper: UserMapper,
    private val mapperAccount: TypeAccountMapper,
    private val mapperCity: CityMapper,
) {
    val name = "utilisateur"
    @OptIn(ExperimentalTime::class)
    fun createUser(user: User) : User? {

        val entityToSave = UserEntity(
            password = user.password,
            typeAccount = mapperAccount.toEntity(user.typeAccount) ,
            email = user.email,
            phone = user.phone,
            city = mapperCity.toEntity(user.city)
        )
        val savedEntity = repository.save(entityToSave)
        return mapper.toDomain(savedEntity)
    }

    fun findAllUser() : List<User?> {
        val allEntityUser = repository.findAll()
        return allEntityUser.stream().map {
            mapper.toDomain(it)
        }.toList()
    }

    fun findIdUser(id : Long) : User?{
        val userEntity = repository.findById(id).orElseThrow{->
            EntityNotFoundException("Aucun $name avec cet identifiant $id")
        }
        return mapper.toDomain(userEntity)
    }

    @OptIn(ExperimentalTime::class)
    fun updateUser(
        id : Long,
        user : User
    ): User ?{
        val userEntity = repository.findById(id).orElseThrow{->
            EntityNotFoundException("Aucun $name avec cet identifiant $id")
        }
        val entityToUpdate = UserEntity(
            userId = user.userId,
            password = user.password,
            typeAccount = mapperAccount.toEntity(user.typeAccount),
            email = user.email,
            phone = user.phone,
            city = mapperCity.toEntity(user.city)
        )
        val updatedUser = repository.save(entityToUpdate)

        return mapper.toDomain(updatedUser)
    }

    fun deleteUser(id : Long) : Boolean{
        if (!repository.existsById(id)){
            EntityNotFoundException("Aucun $name avec cet identifiant $id")
        }
        repository.deleteById(id)
        return true
    }
}