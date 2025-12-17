package server.web.casa.app.property.domain.model

data class Feature(
    val featureId : Long? = null,
    val name : String = "",
)

data class FeatureRequest(
    val featureId : Long,
)
