package server.web.casa.app.ecosystem.domain.request

data class AjusteurServiceRequest(
    val service : TaskRequest,
    val realisation : List<ImageRequest>
)

