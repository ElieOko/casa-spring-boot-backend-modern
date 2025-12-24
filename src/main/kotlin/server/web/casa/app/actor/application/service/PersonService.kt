package server.web.casa.app.actor.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.actor.domain.model.Person
import server.web.casa.app.actor.domain.model.request.PersonRequest
import server.web.casa.app.actor.domain.model.toEntity
import server.web.casa.app.actor.infrastructure.persistence.entity.toDomain
import server.web.casa.app.actor.infrastructure.persistence.repository.PersonRepository
import server.web.casa.app.user.domain.model.ImageUserRequest
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import server.web.casa.utils.storage.FileSystemStorageService

@Service
class PersonService(
    private val repository: PersonRepository,
    private val gcsService: GcsService,
    private val storageService: FileSystemStorageService,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    suspend fun create(model: Person): Person {
       return repository.save(model.toEntity()).toDomain()
    }
    suspend fun findAllPerson(): Flow<Person> {
        return repository.findAll().map { it.toDomain() }
    }
    suspend fun findByIdPerson(id : Long): Person? = coroutineScope  {
        repository.findById(id)?.toDomain() ?: throw ResponseStatusException(HttpStatusCode.valueOf(404), "ID $id not found.")
    }
    suspend fun findByIdPersonUser(id : Long): Person? {
        return repository.findAll().filter { it.userId == id }.firstOrNull()?.toDomain()
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
    suspend fun changeFile(imageUser: ImageUserRequest, userId: Long) = coroutineScope {
        val person = repository.findByUser(userId)?: throw Exception()
        val file = base64ToMultipartFile(imageUser.image, "profile")
        log.info("file ****taille:${file.size}")
        log.info("file ****name:${file.name}")
        val imageUri = gcsService.uploadFile(file,"profil/")
        log.info("public url local ")
        person.images =imageUri
        val result = repository.save(person)
        result.toDomain()
    }
    suspend fun update(person : PersonRequest, id : Long): Person = coroutineScope {
        val data = repository.findById(id)?:  throw ResponseStatusException(HttpStatusCode.valueOf(404), "Information invalid.")
        if ((person.cardBack != null && person.cardFront != null) && (person.cardBack.isNotEmpty() && person.cardFront.isNotEmpty())) {
            val file = base64ToMultipartFile(person.cardBack, "profile")
            val file2 = base64ToMultipartFile(person.cardFront, "profile")
            val imageUri = gcsService.uploadFile(file,"card/")
            val imageUri2 = gcsService.uploadFile(file2,"card/")
            log.info("public url local ")
            data.cardFront = imageUri2
            data.cardBack = imageUri
            repository.save(data).toDomain()
        }
        data.address = person.address
        data.lastName = person.lastName
        data.firstName = person.firstName
        repository.save(data).toDomain()
    }
}