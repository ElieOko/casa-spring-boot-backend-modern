package server.web.casa.app.address.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.address.domain.model.District
import server.web.casa.app.address.infrastructure.persistence.mapper.*
import server.web.casa.app.address.infrastructure.persistence.repository.DistrictRepository

@Service
class DistrictService(
    private val repository: DistrictRepository
) {
    suspend fun saveDistrict(data: District): District? {
        val entity = data.toEntity()
        val result = repository.save(entity)
        return result.toDomain()

    }
    suspend fun findAllDistrict() = repository.findAll().map { it.toDomain() }.toList()

    suspend fun findByIdDistrict(id : Long) = repository.findById(id).orElse(null).toDomain()
}