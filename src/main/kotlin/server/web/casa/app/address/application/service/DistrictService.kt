package server.web.casa.app.address.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.address.domain.model.District
import server.web.casa.app.address.infrastructure.persistence.entity.DistrictEntity
import server.web.casa.app.address.infrastructure.persistence.mapper.DistrictMapper
import server.web.casa.app.address.infrastructure.persistence.repository.DistrictRepository

@Service
class DistrictService(
    private val repository: DistrictRepository,
    private val mapper : DistrictMapper
) {
    suspend fun saveDistrict(data: District): District? {
        val entity: DistrictEntity? = mapper.toEntity(data)
        if ( entity != null){
            val result = repository.save(entity)
            return mapper.toDomain(result)
        }
        return entity
    }

    suspend fun findAllDistrict() : List<District?>{
        return repository.findAll().map { mapper.toDomain(it) }.toList()
    }

    suspend fun findByIdDistrict(id : Long) : District? {
      return repository.findById(id).let{  mapper.toDomain(it.orElse(null)) }
    }
}