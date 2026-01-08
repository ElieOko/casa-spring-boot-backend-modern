package server.web.casa.app.property.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore

data class PropertyType(
    var propertyTypeId : Long = 0,
    val name : String,
    @JsonIgnore
    val description : String? = ""
)
