package server.web.casa.exception

import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import server.web.casa.utils.Mode
import java.time.LocalDateTime

@ControllerAdvice
@Profile(Mode.DEV)
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)
    @ExceptionHandler(Exception::class)
    fun handleGenericException(e : Exception): ResponseEntity<ErrorResponseDto>{
        logger.error("Handle exception", e)
        val errorDto = ErrorResponseDto(
            "Internal server error",
            e.message.toString(),
            LocalDateTime.now()
        )
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorDto)
    }

    @ExceptionHandler(value = [EntityNotFoundException::class])
    fun handleEntityNotFound(e: EntityNotFoundException): ResponseEntity<ErrorResponseDto>{
        logger.error("Handle entityNotFoundException", e);
        val errorDto = ErrorResponseDto(
            "Entity not found",
            e.message.toString(),
            LocalDateTime.now()
        )

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorDto)
    }

    @ExceptionHandler(value = [
        IllegalArgumentException::class,
        IllegalStateException::class,
        MethodArgumentNotValidException::class
    ])
    fun handleBadRequest(e : MethodArgumentNotValidException) : ResponseEntity<MutableMap<String, Any>> {
        logger.error("Handle handleBadRequest", e)
        val map = mutableMapOf<String, Any>()
        e.bindingResult.fieldErrors.forEach { error->
            map[error.field] = error.defaultMessage ?: "Validation"
        }
        val mapList = mutableMapOf<String, Any>()
        map.forEach { (p0, p1) ->
            mapList["message"] = p1
        }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(mapList)
    }

}