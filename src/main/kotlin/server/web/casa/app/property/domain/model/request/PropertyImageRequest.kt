package server.web.casa.app.property.domain.model.request

data class PropertyImageRequest(val image : String)

data class ImageRequest(val name : String)

data class ImageChangeRequest(
    val name : String,
    val old : String,
)

data class ImageChangeOtherRequest(
    val name : String
)
