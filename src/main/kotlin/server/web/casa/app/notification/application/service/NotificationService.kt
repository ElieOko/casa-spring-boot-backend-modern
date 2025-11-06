package server.web.casa.app.notification.application.service

import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val messagingTemplate: SimpMessagingTemplate
) {
    /**
     * Envoie une notification à tous les clients abonnés au topic "/topic/notifications".
     *
     * @param message le texte ou objet de la notification
     */
    fun sendNotification(message: String) {
        messagingTemplate.convertAndSend("/topic/notifications", message)
    }

    /**
     * Variante : envoyer à un utilisateur spécifique.
     *
     * @param username l'identifiant de l'utilisateur
     * @param message  le contenu de la notification
     */
    fun sendNotificationToUser(username: String, message: String) {
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", message)
    }

}