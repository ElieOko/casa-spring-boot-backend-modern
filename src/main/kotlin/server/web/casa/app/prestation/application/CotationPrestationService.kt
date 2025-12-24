package server.web.casa.app.prestation.application

import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.prestation.infrastructure.persistance.entity.CotationPrestationEntity
import server.web.casa.app.prestation.infrastructure.persistance.repository.CotationPrestationRepository

@Service
class CotationPrestationService(
    private val p: CotationPrestationRepository
) {
    suspend fun create(c: CotationPrestationEntity): CotationPrestationEntity{
        val entity = p.save(c)
        return entity
    }
    suspend fun findAll(): List<CotationPrestationEntity>{
        val entity = p.findAll()
        return entity.toList()
    }

    suspend fun findById(id: Long): CotationPrestationEntity?{
        val entity = p.findById(id)
        return entity
    }

    suspend fun deleteById(id: Long): Boolean {
        val entity = p.deleteById(id)
        return true
    }
}