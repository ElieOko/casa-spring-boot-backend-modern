package server.web.casa.app.property.domain.model

data class PropertyImage(
    val propertyImageId : Long = 0,
    var propertyId : Long?=null,
    var name : String = "",
    var path : String = ""
)
