package server.web.casa.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.*
import org.springframework.messaging.simp.config.*
import org.springframework.messaging.simp.stomp.*
import org.springframework.messaging.support.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.socket.config.annotation.*
import server.web.casa.security.JwtService
import kotlin.jvm.java

private lateinit var heartbeatValue: LongArray
@Configuration
@EnableWebSocketMessageBroker
class NotificationConfig(
    private val jwtService: JwtService
): WebSocketMessageBrokerConfigurer {
    private val log = LoggerFactory.getLogger(this::class.java)
//    private lateinit var messageBrokerTaskScheduler: TaskScheduler
//
//    @Autowired
//    fun setMessageBrokerTaskScheduler(@Lazy taskScheduler: TaskScheduler) {
//        this.messageBrokerTaskScheduler = taskScheduler
//    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(object : ChannelInterceptor {
            override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
                val accessor = MessageHeaderAccessor.getAccessor(message,
                    StompHeaderAccessor::class.java)
                if (StompCommand.CONNECT == accessor!!.command) {
                    val authHeader = accessor.getFirstNativeHeader("Authorization")
                    log.info("$authHeader")
                    if (!authHeader.isNullOrEmpty() && authHeader.startsWith("Bearer ")) {
                        if(jwtService.validateAccessToken(authHeader)) {
                            val userId = jwtService.getUserIdFromToken(authHeader)
                            val auth = UsernamePasswordAuthenticationToken(userId, null, emptyList())
//                            SecurityContextHolder.getContext().authentication = auth
                            accessor.user = auth
                            log.info("${userId.toInt().toLong()}")
                        } else{
                            log.info("Authorization pas permis ce tokem n'est plus valide")
                        }
                    }
                    else {
                        throw IllegalArgumentException("Authorization header manquant ou invalide")
                    }
                }
                return message
            }
        })
    }

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic","/queue")
        config.setUserDestinationPrefix("/user") // pour les messages ciblés
        config.setApplicationDestinationPrefixes("/app")
//        config.
//            .setHeartbeatValue(longArrayOf(10000, 20000))
//            .setTaskScheduler(messageBrokerTaskScheduler)

    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            //réception ordonnée
            .setPreserveReceiveOrder(true)
            .addEndpoint("/websocket")
            .setAllowedOriginPatterns("*")
    }

    override fun configureWebSocketTransport(registry: WebSocketTransportRegistration) {
        registry.setMessageSizeLimit(4 * 8192)
        registry.setTimeToFirstMessage(30000)
    }
}