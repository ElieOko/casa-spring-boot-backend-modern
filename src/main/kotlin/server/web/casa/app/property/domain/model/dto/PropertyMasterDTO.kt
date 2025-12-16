package server.web.casa.app.property.domain.model.dto

import server.web.casa.app.property.domain.model.AddressDTO
import server.web.casa.app.property.domain.model.Feature
import server.web.casa.app.property.domain.model.PropertyType

class PropertyMasterDTO(
    val property: PropertyDTO,
    val images: Images,
    val postBy : String,
    val address :AddressDTO,
    val geoZone : GeoDTO,
    val typeProperty: PropertyType,
    val features :List<Feature>
)
