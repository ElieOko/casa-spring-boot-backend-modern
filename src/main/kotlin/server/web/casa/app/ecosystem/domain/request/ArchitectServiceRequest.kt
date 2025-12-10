package server.web.casa.app.ecosystem.domain.request

data class ArchitectServiceRequest(
    val service : TaskRequest,
    val realisation : List<ImageRequest>
)

