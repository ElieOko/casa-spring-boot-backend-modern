package server.web.casa.app.property.infrastructure.persistence.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity

interface PropertyRepository : JpaRepository<PropertyEntity, Long> {
    @EntityGraph(attributePaths = [
        "propertyImage",
        "propertyImageRoom",
        "propertyImageLivingRoom",
        "propertyImageKitchen"])
    override fun findAll(): List<PropertyEntity?>

    @Query("""
        SELECT r FROM PropertyEntity r 
        WHERE r.sold = :sold 
        OR (r.price BETWEEN :minPrice AND :maxPrice)
        OR (r.commune.id = :commune)
        OR (r.city.id = :city)
        OR (r.propertyType.id = :typeMaison)
        OR (r.rooms = :room)
    """)
    fun filterProperty(
        @Param("sold") sold: Boolean,
        @Param("minPrice") minPrice: Double,
        @Param("maxPrice") maxPrice: Double,
        @Param("city") city: Long,
        @Param("commune") commune: Long,
        @Param("typeMaison")  typeMaison: Long,
        @Param("room")  room: Int,
        pageable: Pageable
    ) : Page<PropertyEntity>
}
