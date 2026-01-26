package server.web.casa.app.property.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.*

interface BureauRepository : CoroutineCrudRepository<BureauEntity, Long>{
    @Query("""
        SELECT * FROM bureau
        WHERE transaction_type = :transactionType
        AND price BETWEEN :minPrice AND :maxPrice
        AND ((:city IS NULL AND (:cityValue IS NULL OR city_value = :cityValue) ) OR city_id = :city)
        OR ((:commune IS NULL AND (:communeValue IS NULL OR commune_value = :communeValue)) OR commune_id = :commune)
        AND property_type_id = :typeMaison
        AND is_available = true
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
    ) : Flow<BureauEntity>

    @Query("""
        SELECT * FROM bureau
        WHERE is_available = true
    """)
    override fun findAll():Flow<BureauEntity>

    @Query("""
        SELECT * FROM bureau
        WHERE user_id = :userId
    """)
    fun findAllByUser(userId: Long):Flow<BureauEntity>

    @Modifying
    @Query(
        """ UPDATE salle_festives
    SET is_available = :state
    WHERE user_id = :userId"""
    )
    suspend fun setUpdateIsAvailable(userId: Long, state: Boolean = false): Int
}