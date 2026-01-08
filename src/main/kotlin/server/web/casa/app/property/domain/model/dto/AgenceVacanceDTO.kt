package server.web.casa.app.property.domain.model.dto

import server.web.casa.app.property.domain.model.Agence
import server.web.casa.app.property.domain.model.Vacance
import server.web.casa.app.property.domain.model.VacanceImage

data class VacanceDTO(
    val vacance: Vacance,
    val image: List<VacanceImage?>,
    )

data class VacanceAgence(
    val agence : Agence?,
    val site : List<VacanceDTO>
    )
