package server.web.casa.app.property.domain.model.filter

data class PropertyFilter(
    val sold : Boolean,
    val minPrice : Double = 0.0,
    val maxPrice : Double = 0.0,
    val city : Long,
    val commune : Long,
    val typeMaison : Long,
    val room : Int = 1
)
