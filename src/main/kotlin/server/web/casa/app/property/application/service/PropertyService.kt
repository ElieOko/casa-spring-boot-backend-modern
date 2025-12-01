package server.web.casa.app.property.application.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.util.Streamable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
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
    suspend fun getAll(
        page : Int,
        size : Int,
        sortBy : String,
        sortOrder : String
    ) : Page<Property> {
        val sort = if (sortOrder.equals("desc",true)) Sort.by(sortBy).descending()  else Sort.by(sortBy).ascending()
        val pageable = PageRequest.of(page,size,sort)
        val page: Page<PropertyEntity> = repository.findAll(pageable = pageable)
        return page.map { mapper.toDomain(it) }
    }

    suspend fun getAllPropertyByUser(userId : Long) : List<Property> {
        val data = repository.findAll().filter { it.user?.userId ==  it.user?.userId}
        return data.map { mapper.toDomain(it) }
    }

    suspend fun findByIdProperty(id: Long): Pair<Property, List<Property>> {
        val property = repository.findById(id).orElseThrow {
                ResponseStatusException(HttpStatus.BAD_REQUEST,"Cette proprièté n'existe pas")
            }
        val data = mapper.toDomain(property)
        val similary = repository.findAll().filter {
            (it.cityValue == data.cityValue) || (it.communeValue == data.communeValue)  || (it.transactionType == data.transactionType) || (it.propertyType == data.propertyType)
        }.toList()
        val key = Pair(data,similary.map{mapper.toDomain(it)})
        return key
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
            transactionType = filterModel.transactionType,
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

