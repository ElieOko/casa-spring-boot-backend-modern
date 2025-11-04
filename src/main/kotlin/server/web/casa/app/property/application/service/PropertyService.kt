package server.web.casa.app.property.application.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import server.web.casa.app.property.domain.model.Property
import server.web.casa.app.property.domain.model.filter.PropertyFilter
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.property.infrastructure.persistence.mapper.PropertyMapper
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyRepository

@Service
class PropertyService(
    private val repository: PropertyRepository,
    private val mapper : PropertyMapper
) {

    suspend fun create(p : Property): Property {
        val data = mapper.toEntity(p)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }
    suspend fun getAll() : List<Property> = repository.findAll().stream().map {
        mapper.toDomain(it!!)
    }.toList()

    suspend fun findByIdProperty(id : Long) : Property? {
        repository.findById(id).let {
            return mapper.toDomain(it.orElse(null))
        }
    }
    suspend fun filterProduct(
        filterModel : PropertyFilter,
        page : Int,
        size : Int,
        sortBy : String,
        sortOrder : String
    ): Page<Property>{
        val sort = if (sortOrder.equals("desc",true)) Sort.by(sortBy).descending()  else Sort.by(sortBy).ascending()
        val pageable = PageRequest.of(page,size,sort)
        val data = repository.filterProperty(
            sold = filterModel.sold,
            minPrice = filterModel.minPrice,
            maxPrice = filterModel.maxPrice,
            city = filterModel.city,
            commune = filterModel.commune,
            typeMaison = filterModel.typeMaison,
            room = filterModel.room,
            pageable = pageable
        )
        return data.map { mapper.toDomain(it) }
    }
}

