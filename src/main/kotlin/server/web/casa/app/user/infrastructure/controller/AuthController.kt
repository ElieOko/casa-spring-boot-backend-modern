package server.web.casa.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*
import server.web.casa.app.user.application.service.*
import server.web.casa.app.user.domain.model.*
import server.web.casa.app.user.domain.model.request.*
import server.web.casa.route.auth.AuthRoute
import server.web.casa.security.Auth
import server.web.casa.security.monitoring.*

const val ROUTE_REGISTER = AuthRoute.REGISTER
const val ROUTE_LOGIN = AuthRoute.LOGIN

@Tag(name = "Authentification", description = "Gestion des accès")
@RestController
@RequestMapping
@Profile("dev")
class AuthController(
    private val authService: AuthService,
    private val accountService: TypeAccountService,
    private val auth: Auth,
    private val sentry : SentryService
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    @Operation(summary = "Création utilisateur")
    @PostMapping(ROUTE_REGISTER)
    suspend fun register(request: HttpServletRequest,
        @Valid @RequestBody req : UserAuthRequest
    ): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val accountItems = req.account
            if (accountItems.isNotEmpty()){
              val account = accountItems.map { accountService.findByIdTypeAccount(it.typeAccount) }.first()
              val userSystem = req.toDomain()
              val data = authService.register(userSystem,accountItems)
              val response = mapOf("user" to data.first, "token" to data.second, "message" to "Votre compte principal ${account.name} a été créer avec succès")
              ResponseEntity.status(201).body(response)
            }
            val response = mapOf("message" to "Vous devez selectionner au moins un compte pour vous enregistrez")
            ResponseEntity.status(404).body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.register.count",
                    distributionName = "api.auth.register.latency"
                )
            )
        }
    }

    @Operation(summary = "Connexion utilisateur")
    @PostMapping(ROUTE_LOGIN)
    suspend fun login(
        request: HttpServletRequest,
      @Valid @RequestBody body: UserAuth
    ): ResponseEntity<Map<String, Any?>>  = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val data = authService.login(body.identifiant, body.password)
              try {
                  val response = mapOf("member" to data.second, "token" to data.first.accessToken, "refresh_token" to data.first.refreshToken, "message" to "Connexion réussie avec succès")
                  ResponseEntity.ok().body(response)
              }
              catch (e: AuthenticationException){
                  log.info(e.message)
                  val response = mapOf("message" to e.message)
                  ResponseEntity.status(401).body(response)
              }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.login.count",
                    distributionName = "api.auth.login.latency"
                )
            )
        }
    }

    @PostMapping("/api/{version}/protected/token/refresh")
    suspend fun refresh(request: HttpServletRequest, @RequestBody body: RefreshRequest): AuthService.TokenPair = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            authService.refresh(body.refreshToken)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.refresh.count",
                    distributionName = "api.auth.refresh.latency"
                )
            )
        }
    }
    @Operation(summary = "OTP activation send code")
    @PostMapping("/api/{version}/public/otp/generate")
    suspend fun generateKeyOTP(request: HttpServletRequest,
        @RequestBody @Valid user : IdentifiantRequest
    ): ResponseEntity<Map<String, String?>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val result = authService.generateOTP(user.identifier)
            val message = mapOf("message" to result.second, "status" to result.first, "phone" to result.third)

             ResponseEntity.ok(message)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.generatekeyotp.count",
                    distributionName = "api.auth.generatekeyotp.latency"
                )
            )
        }
    }

    @Operation(summary = "OTP activation send code")
    @PostMapping("/api/{version}/public/otp/verify")
    suspend fun verifyOTP(request: HttpServletRequest,
        @RequestBody @Valid user : VerifyRequest
    ): ResponseEntity<out Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val result = authService.verifyOTP(user)
            val message = mapOf("status" to result.second, "user" to result.first)
            ResponseEntity.ok(message)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.verifyotp.count",
                    distributionName = "api.auth.verifyotp.latency"
                )
            )
        }
    }
    @Operation(summary = "certification user")
    @PutMapping("/api/{version}/protected/certification/{userId}")
    suspend fun goCertification(
        request: HttpServletRequest,
        @PathVariable("userId") userId : Long,
        @RequestBody @Valid certification : CertificationState
    )  = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val data = authService.goCertification(userId,certification.state)
            ResponseEntity.ok(
                mapOf(
                    "message" to if (certification.state) "Certification successful" else "Certification failed",
                    "user" to data
                )
            )
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.certifcation.user.count",
                    distributionName = "api.certifcation.user.latency"
                )
            )
        }

    }
    @Operation(summary = "Reset password ")
    @PutMapping("/api/{version}/protected/reset/password")
    suspend fun resetPassword(request: HttpServletRequest,
        @RequestBody @Valid user : UserPassword
    ) : ResponseEntity<Map<String, String>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val session = auth.user()
            val new = user.newPassword
            authService.changePassword(session?.first?.userId?:user.userId,new)
            val message = mapOf("message" to "Mot de passe changé avec succès")
            ResponseEntity.ok(message)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.resetpassword.count",
                    distributionName = "api.auth.resetpassword.latency"
                )
            )
        }
    }

    @Operation(summary = "Change password utilisateur")
    @PutMapping("/api/{version}/protected/change/password")
    suspend fun updateUser(request: HttpServletRequest,
        @RequestBody @Valid user : UserPassword
    ) : ResponseEntity<Map<String, String>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val userConnect = auth.user()
            val new = user.newPassword
            authService.changePassword(userConnect?.first?.userId!!,new)
            val message = mapOf("message" to "Mot de passe changé avec succès")
            ResponseEntity.ok(message)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.updateuser.count",
                    distributionName = "api.auth.updateuser.latency"
                )
            )
        }
    }

    @Operation(summary = "Delete Account User")
    @DeleteMapping("/api/{version}/protected/users/delete/user")
    suspend fun lockAccount(request: HttpServletRequest,): ResponseEntity<Map<String, String>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val userId = auth.user()?.first?.userId
            val state = authService.lockedOrUnlocked(userId as Long)
            val message = mapOf("message" to if (state) "Votre compte a été supprimé avec succès" else "Cet utilisateur n'existe pas")
            ResponseEntity.ok(message)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.lockaccount.count",
                    distributionName = "api.auth.lockaccount.latency"
                )
            )
        }
    }
    @Operation(summary = "Recovery Account User")
    @PutMapping("/api/{version}/protected/recovery/user/{id}")
    suspend fun unlockAccount(request: HttpServletRequest,@PathVariable("id") id : Long): ResponseEntity<Map<String, String>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val session = auth.user()
            val state: Boolean? = session?.second?.find{ true }
            when (state) {
                true -> {
                    val state = authService.lockedOrUnlocked(id,false)
                    val message = mapOf("message" to if (state) "Votre compte a été restauré avec succès" else "Cet utilisateur n'existe pas")
                    ResponseEntity.ok(message)
                }
                false,null -> {
                    ResponseEntity.status(403).body(mapOf("message" to "Accès non autorisé"))
                }
            }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.unlockaccount.count",
                    distributionName = "api.auth.unlockaccount.latency"
                )
            )
        }
    }
}