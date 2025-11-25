package server.web.casa.app.notification.infrastructure.controller.stom

import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.*
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import server.web.casa.security.Auth
import java.security.Principal

@Controller
class NotificationController(
    private val auth: Auth
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional
    @MessageMapping("/sendMessage")
    @SendTo("/topic/properties")
    fun notificationProperties(principal: Principal): Map<String, String> {
        val user = auth.userStom(principal)
        log.info("**********user->${user?.phone}")
        Thread.sleep(1000)
        val response = mapOf(
            "properties" to "${user?.phone}"
        )
        log.info("*** ${response["properties"]}")
        return response
    }
}