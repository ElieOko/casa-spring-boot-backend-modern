package server.web.casa.app.property.domain.model.dto

import server.web.casa.app.payment.domain.model.Devise
import server.web.casa.app.property.domain.model.AddressDTO
import server.web.casa.app.property.domain.model.PropertyType
import server.web.casa.app.property.domain.model.Terrain
import server.web.casa.app.property.domain.model.TerrainImage
import java.time.LocalDateTime

data class TerrainDTO(
    val id: Long? = null,
    val userId: Long,
    val title: String,
    val description : String = "",
    val price: Double? = null,
    val surface : String? = "",
    val transactionType: String = "",
    var isAvailable: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)


data class TerrainMasterDTO(
    val terrain : TerrainDTO,
    val devise: Devise?,
    val postBy : String,
    val address :AddressDTO,
    val localAddress : LocalAddressDTO,
    val geoZone : GeoDTO,
    val images : List<TerrainImage>? = null ,
    val typeProperty: PropertyType,
)


fun Terrain.toDTO() = TerrainDTO(
    id = this.id,
    userId = this.userId,
    title = this.title,
    description = this.description,
    price = this.price,
    surface = this.surface,
    transactionType = this.transactionType,
    isAvailable = this.isAvailable,
    createdAt = this.createdAt,
)