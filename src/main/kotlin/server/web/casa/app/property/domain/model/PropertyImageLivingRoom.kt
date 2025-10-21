package server.web.casa.app.property.domain.model

data class PropertyImageLivingRoom(
    val propertyImageLivingRoomId : Long = 0,
    var property : Property? = null,
    var name : String = "",
    var path : String = ""
)
