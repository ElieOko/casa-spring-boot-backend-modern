package server.web.casa.app.ecosystem.domain.request

data class MaconServiceRequest(
    val service : TaskRequest,
    val realisation : List<ImageRequest>
)
