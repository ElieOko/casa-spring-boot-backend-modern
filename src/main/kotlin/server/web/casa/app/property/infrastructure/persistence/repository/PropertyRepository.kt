package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity

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
       transactionType: String,
       minPrice: Double,
       maxPrice: Double,
       city: Long?,
       cityValue: String?,
       commune: Long?,
       communeValue: String?,
       typeMaison: Long,
       room: Int,
    ) : Flow<PropertyEntity>

    @Query("""
        SELECT * FROM properties
        WHERE is_available = true
    """)
    override fun findAll(): Flow<PropertyEntity>

    @Query("""
        SELECT * FROM properties
        WHERE user_id = :userId
    """)
    fun findAllByUser(userId: Long):Flow<PropertyEntity>

    @Modifying
    @Query(
        """ UPDATE properties
    SET is_available = :state
    WHERE user_id = :userId"""
    )
    suspend fun setUpdateIsAvailable(userId: Long, state: Boolean = false): Int

}
