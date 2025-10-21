package server.web.casa.app.property.domain.model

data class PropertyImageKitchen(
    val propertyImageKitchenId : Long = 0,
    var property : Property? = null,
    var name : String = "",
    var path : String = ""
)
