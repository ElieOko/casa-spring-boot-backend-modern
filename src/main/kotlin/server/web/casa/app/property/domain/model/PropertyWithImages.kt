package server.web.casa.app.property.domain.model

import server.web.casa.app.property.domain.model.dto.Images
import server.web.casa.app.property.infrastructure.persistence.entity.*

data class PropertyWithImages(
    val property: Property,
    val images: Images,
)

data class Image(
    val main : List<PropertyImageEntity?>,
    val room : List<PropertyImageRoomEntity?>,
    val living : List<PropertyImageLivingRoomEntity?>,
    val kitchen: List<PropertyImageKitchenEntity?>,
)
data class AddressDTO(
    var address : String,
    var communeValue: String? = "",
    var quartierValue: String? = "",
    var cityValue: String? = "",
    var countryValue: String? = "",
    var postalCode: String?,
)
