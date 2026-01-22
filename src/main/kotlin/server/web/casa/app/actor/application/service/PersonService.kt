package server.web.casa.app.actor.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.slf4j.LoggerFactory
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.actor.domain.model.Person
import server.web.casa.app.actor.domain.model.request.PersonRequest2
import server.web.casa.app.actor.domain.model.toEntity
import server.web.casa.app.actor.infrastructure.persistence.entity.toDomain
import server.web.casa.app.actor.infrastructure.persistence.repository.*
import server.web.casa.app.user.domain.model.ImageUserRequest
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import server.web.casa.utils.normalizeAndValidatePhoneNumberUniversal

@Service
class PersonService(
    private val repository: PersonRepository,
    private val gcsService: GcsService,
    private val cardRepository: TypeCardRepository,
    private val repositoryUser: UserRepository
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
    suspend fun findByIdPersonUser(id : Long): Person? = coroutineScope {
        repository.findByUser(id)?.toDomain()
    }
    suspend fun update(id : Long,model: Person): Person {
       val data = repository.findById(id)
        if (data != null && model.id == data.id) {
           return repository.save(model.toEntity()).toDomain()
        }
        throw ResponseStatusException(HttpStatusCode.valueOf(403), "Invalid credentials.")
    }
    suspend fun findByIdUser(userId : Long): Person?  = coroutineScope {
       repository.findByUser(userId)?.toDomain()
    }
    suspend fun changeFile(imageUser: ImageUserRequest, userId: Long) = coroutineScope {
        val person = repository.findByUser(userId)?: throw Exception()
        val file = base64ToMultipartFile(imageUser.image, "profile")
        log.info("file ****taille:${file.size}")
        log.info("file ****name:${file.name}")
        val imageUri = gcsService.uploadFile(file,"profil/")
//        log.info("public url local ")
        person.images =imageUri
        val result = repository.save(person)
        result.toDomain()
    }
    suspend fun update(person : PersonRequest2, id : Long) = coroutineScope {
        val data = repository.findById(id)?:  throw ResponseStatusException(HttpStatusCode.valueOf(404), "Information invalid au niveau de l'identifiant du membre.")
        val user  = repositoryUser.findById(data.userId!!)?: throw ResponseStatusException(HttpStatusCode.valueOf(404), "Information invalid au niveau de l'utilisateur.")
        val email = person.user.email
        val phone = person.user.phone?.ifEmpty { "" }
        person.user.city.ifEmpty { user.city }
        person.user.country.ifEmpty { user.country }
        if(email?.isNotEmpty() == true) {
            val identifier = repositoryUser.findByPhoneOrEmail(email)
            if (identifier == null){
                user.email = email
            }
        }

        if (phone?.isNotEmpty() == true){
            val phone2 =  normalizeAndValidatePhoneNumberUniversal(phone) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Ce numero n'est pas valide.")
            val identifier = repositoryUser.findByPhoneOrEmail(phone)
            if (identifier == null){
                user.phone = phone2
            }
        }

       user.country = person.user.country.ifEmpty { user.country }
       user.city = person.user.city.ifEmpty { user.city }
        if (person.typeCardId == null) throw ResponseStatusException(HttpStatusCode.valueOf(404), "La carte n'a pas été envoyée")
        cardRepository.findById(person.typeCardId)?: throw ResponseStatusException(HttpStatusCode.valueOf(404), "Carte invalide")
        if (person.typeCardId != 2L && person.cardBack?.isNotEmpty() == true) {
            if ((person.cardBack != null && person.cardFront != null) && (person.cardBack.isNotEmpty() && person.cardFront.isNotEmpty())) {
                val file = base64ToMultipartFile(person.cardBack, "profile")
                val file2 = base64ToMultipartFile(person.cardFront, "profile")
                val imageUri = gcsService.uploadFile(file,"card/")
                val imageUri2 = gcsService.uploadFile(file2,"card/")
                user.isCertified = true
                log.info("public url local ")
                data.cardFront = imageUri2
                data.cardBack = imageUri
                repository.save(data).toDomain()
            }
        }
        if (person.cardFront?.isNotEmpty() == true && person.cardBack?.isEmpty() == true) {
            person.cardFront.ifEmpty { throw ResponseStatusException(HttpStatusCode.valueOf(404), "Précisez votre carte") }
            val file2 = base64ToMultipartFile(person.cardFront, "profile")
            val imageUri2 = gcsService.uploadFile(file2,"card/")
            user.isCertified = true
            data.cardFront = imageUri2
            repository.save(data).toDomain()
        }
       data.address = person.address
       data.lastName = person.lastName.ifEmpty { data.lastName }
       data.firstName = person.firstName.ifEmpty { data.firstName }
       val userState = repositoryUser.save(user)
       val member = repository.save(data).toDomain()
       Pair(userState,member)
    }
}