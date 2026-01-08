package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.property.infrastructure.persistence.entity.SalleFestiveEntity

interface PropertyRepository : CoroutineCrudRepository<PropertyEntity, Long> {
//    @EntityGraph(attributePaths = [
//        "propertyImage",
//        "propertyImageRoom",
//        "propertyImageLivingRoom",
//        "propertyImageKitchen"])
    @Query("""
            SELECT * FROM properties
            WHERE transaction_type = :transactionType
            AND is_available = true
            AND price BETWEEN :minPrice AND :maxPrice
            AND ((:city IS NULL AND (:cityValue IS NULL OR city_value = :cityValue) ) OR city_id = :city)
            OR(rooms = :room)
            OR ((:commune IS NULL AND (:communeValue IS NULL OR commune_value = :communeValue)) OR commune_id = :commune)
            AND property_type_id = :typeMaison
        """)
    fun filter(
        @Param("transactionType") transactionType: String,
        @Param("minPrice") minPrice: Double,
        @Param("maxPrice") maxPrice: Double,
        @Param("city") city: Long?,
        @Param("cityValue") cityValue: String?,
        @Param("commune") commune: Long?,
        @Param("communeValue") communeValue: String?,
        @Param("typeMaison")  typeMaison: Long,
        @Param("room")  room: Int,
    ) : Flow<PropertyEntity>

    @Query("""
        SELECT * FROM properties
        WHERE is_available = true
    """)
    override fun findAll(): Flow<PropertyEntity>
}
