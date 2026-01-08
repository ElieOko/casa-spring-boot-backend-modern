package server.web.casa.utils

data class ApiResponseWithMessage<T>(
    val message: String,
    val data: T
)

data class ApiResponse<T>(
    val data: T
)
