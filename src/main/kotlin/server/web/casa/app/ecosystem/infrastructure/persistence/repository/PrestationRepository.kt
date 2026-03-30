package server.web.casa.app.ecosystem.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import server.web.casa.app.ecosystem.infrastructure.persistence.entity.PrestationEntity

interface PrestationRepository : CoroutineCrudRepository<PrestationEntity, Long>{

    @Query("SELECT * FROM prestations WHERE user_id = :userId AND service_id = :serviceId")
    suspend fun findByUserAndService(userId: Long, serviceId : Long) : PrestationEntity?

    @Query("SELECT * FROM prestations WHERE user_id = :userId")
    suspend fun findByUser(userId: Long) : Flow<PrestationEntity>

    @Query("SELECT COUNT(*) FROM prestations WHERE user_id = :userId")
    suspend fun countByUserId(userId: Long) : Long

    @Query("SELECT  * FROM prestations WHERE is_active = true ")
    suspend fun findAllFilter() : Flow<PrestationEntity>

    @Query("SELECT * FROM prestations WHERE is_active = true and id = :id")
    suspend fun findByIdPrestation(id : Long) :Flow<PrestationEntity>

    @Query("SELECT * FROM prestations WHERE is_active = true and user_id = :userId")
    suspend fun findAllFindByUser(userId: Long) : Flow<PrestationEntity>

    @Modifying
    @Query(
        """ UPDATE prestations
    SET is_active = :state
    WHERE user_id = :userId"""
    )
    suspend fun setUpdateIsAvailable(userId: Long, state: Boolean = false): Int

    @Modifying
    @Query(
        """ UPDATE prestations
    SET 
        serviceId = :serviceId,
        deviseId = :deviseId,
        title = :title,
        description = :description,
        profile = :profile,
        cvFile = :cvFile,
        experience = :experience,
        plageJourPrestation = :plageJourPrestation,
        plageHeurePrestation = :plageHeurePrestation,
        minPrice = :minPrice,
        maxPrice = :maxPrice,
        address = :address,
        communeValue = :communeValue,
        quartierValue = :quartierValue,
        cityValue = :cityValue,
        countryValue = :countryValue,
        cityId = :cityId,
        communeId = :communeId,
        quartierId = :quartierId,
        isCertified = :isCertified,
        isActive = :isActive
    WHERE id = :id
    """
    )
    suspend fun updatePrestation(
        @Param("id") id: Long, @Param("isActive") isActive: Boolean, @Param("serviceId") serviceId: Long,
        @Param("deviseId") deviseId: Long, @Param("title") title: String, @Param("description") description: String?,
        @Param("profile") profile: String?, @Param("cvFile") cvFile: String?, @Param("experience") experience: String?,
        @Param("plageJourPrestation") plageJourPrestation: String?, @Param("plageHeurePrestation") plageHeurePrestation: String?, @Param("minPrice") minPrice: Double,
        @Param("maxPrice") maxPrice: Double, @Param("address") address: String?, @Param("communeValue") communeValue: String?,
        @Param("quartierValue") quartierValue: String?, @Param("cityValue") cityValue: String?, @Param("countryValue") countryValue: String?,
        @Param("cityId") cityId: Long?, @Param("quartierId") quartierId: Long?, @Param("isCertified") isCertified: Boolean,
        @Param("communeId") communeId: Long?
        ): Int
}
