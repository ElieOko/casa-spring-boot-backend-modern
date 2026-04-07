package server.web.casa.app.property.infrastructure.persistence.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("hotel_images")
data class HotelImageEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("hotel_id")
    var hotelId: Long? = null,
    @Column("name")
    var name: String,
    @Column("path")
    var path: String?
)
