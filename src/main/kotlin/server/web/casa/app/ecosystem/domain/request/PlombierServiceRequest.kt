package server.web.casa.app.ecosystem.domain.request

data class PlombierServiceRequest(
    val service : TaskRequest,
    val realisation : List<ImageRequest>
)
