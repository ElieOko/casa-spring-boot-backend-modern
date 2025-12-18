package server.web.casa.app.address.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore

data class City(
    var cityId : Long?,
    @JsonIgnore
    val country : Long,
    val name : String,
)
