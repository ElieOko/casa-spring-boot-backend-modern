package server.web.casa.app.property.domain.model

data class PropertyFeature(
    val propertyFeatureId : Long = 0,
    val name : String,
    val property : List<Property?> = emptyList()
)
