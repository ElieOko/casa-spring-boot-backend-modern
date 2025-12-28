package server.web.casa.app.pub.application

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.threeten.bp.LocalTime
import server.web.casa.app.pub.infrastructure.persistance.entity.PublicityEntity
import server.web.casa.app.pub.infrastructure.persistance.repository.PublicityRepository
import server.web.casa.utils.Mode
import server.web.casa.utils.base64ToMultipartFile
import server.web.casa.utils.gcs.GcsService
import java.time.LocalDate

@Service
@Profile(Mode.DEV)
class PublicityService(
    private  val repo: PublicityRepository,
    private val gcsService: GcsService
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val subdirectory = "pub/"
    suspend fun createPub(pub: PublicityEntity): PublicityEntity {
        val file = base64ToMultipartFile(pub.imagePath!!, "pub")
        log.info("file ****taille:${file.size}")
        log.info("file ****name:${file.name}")
        val imageUrl = gcsService.uploadFile(file,subdirectory)
        log.info("public url local ${imageUrl}")
        pub.imagePath = imageUrl
        return repo.save(pub)
    }
    suspend fun findAllPub() = repo.findAll().map{ it }.toList()

    suspend fun findId(id: Long) = repo.findById(id)

    suspend fun updateIsActive(id: Long, state: Boolean): Boolean{
        val pub = repo.findById(id)
        pub?.isActive = state
        repo.save(pub!!)
        return true
    }

    suspend fun findByUser(userId: Long): List<PublicityEntity?> {
        return repo.findByUserId(userId)?.map{ it }?.toList() ?: emptyList()
    }
    suspend fun deleteById(id: Long): Boolean {
        val pub = repo.deleteById(id)
        return true
    }
    suspend fun findByCreated(date: LocalDate): List<PublicityEntity?>  {
        return repo.findByCreated(date)?.map{ it }?.toList() ?: emptyList()
    }
}