package server.web.casa.app.actor.application.service

import kotlinx.coroutines.flow.*
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.actor.domain.model.Person
import server.web.casa.app.actor.domain.model.toEntity
import server.web.casa.app.actor.infrastructure.persistence.entity.PersonEntity
import server.web.casa.app.actor.infrastructure.persistence.entity.toDomain
import server.web.casa.app.actor.infrastructure.persistence.repository.PersonRepository

@Service
class PersonService(
    private val repository: PersonRepository
) {
    suspend fun create(model: Person): Person {
       return repository.save(model.toEntity()).toDomain()
    }
    suspend fun findAllPerson(): Flow<Person> {
        return repository.findAll().map { it.toDomain() }
    }
    suspend fun findByIdPerson(id : Long): Person? {
        return repository.findById(id)?.toDomain()
    }

    suspend fun findByIdPersonUser(id : Long): Person {
        return repository.findAll().filter { it.userId == id }.first().toDomain()
    }
    suspend fun update(id : Long,model: Person): Person {
       val data = repository.findById(id)
        if (data != null && model.id == data.id) {
           return repository.save(model.toEntity()).toDomain()
        }
        throw ResponseStatusException(HttpStatusCode.valueOf(403), "Invalid credentials.")
    }
    suspend fun findByIdUser(userId : Long): Person? {
       val profile: Person? = repository.findAll().filter { it.userId == userId }.firstOrNull()?.toDomain()
       return profile
    }
}