package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity

interface PropertyRepository : CoroutineCrudRepository<PropertyEntity, Long> {
//    @EntityGraph(attributePaths = [
//        "propertyImage",
//        "propertyImageRoom",
//        "propertyImageLivingRoom",
//        "propertyImageKitchen"])
override fun findAll(): Flow<PropertyEntity>

    @Query("""
        SELECT r FROM PropertyEntity r 
        WHERE r.transactionType = :transactionType 
        OR (r.price BETWEEN :minPrice AND :maxPrice)
        OR (r.commune.id = :commune)
        OR (r.city.id = :city)
        OR (r.propertyType.id = :typeMaison)
        OR (r.rooms = :room)
    """)
    fun filterProperty(
        @Param("transactionType") transactionType: String,
        @Param("minPrice") minPrice: Double,
        @Param("maxPrice") maxPrice: Double,
        @Param("city") city: Long,
        @Param("commune") commune: Long,
        @Param("typeMaison")  typeMaison: Long,
        @Param("room")  room: Int,
    ) : Flow<PropertyEntity>
}
