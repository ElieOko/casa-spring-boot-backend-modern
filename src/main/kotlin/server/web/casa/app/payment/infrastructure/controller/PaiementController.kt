package server.web.casa.app.payment.infrastructure.controller

import io.swagger.v3.oas.annotations.*
import kotlinx.coroutines.*
import org.springframework.context.annotation.*
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.payment.application.service.*
import server.web.casa.security.monitoring.*
import jakarta.servlet.http.*
import jakarta.validation.*
import kotlinx.coroutines.reactive.collect
import org.slf4j.*
import server.web.casa.app.payment.domain.model.*
import server.web.casa.app.user.infrastructure.persistence.repository.*
import server.web.casa.route.payment.*
import server.web.casa.security.*
import server.web.casa.utils.*

const val AMOUNT_SUBSCRIPTION = 5
@RestController
@RequestMapping("api")
@Profile("dev")
class PaiementController(
    private val service: FlexPaieService,
    private val sentry: SentryService,
    private val devise : DeviseService,
    private val payment : PaymentService,
    private val auth : Auth,
    private val userRepository: UserRepository
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Operation(summary = "Abonnement CasaNayo user premium 5$")
    @PostMapping("/{version}/${PaymentScope.PROTECTED}/subscription",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun mobileMoney(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: TransactionRequest,
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        val userConnect = auth.user()
        try {
            when(request.deviseId){
                1L-> {
                    val test = devise.getById(request.deviseId)
                    val amountCDF = test?.tauxLocal?.times(AMOUNT_SUBSCRIPTION)
                    val reference = generateTransactionReference()
                    val transaction = Transaction(
                        phone = request.phone,
                        reference = reference,
                        amount = "$amountCDF"
                    )
                    val result = service.paymentMobileMoney(transaction)
                    result.collect {
                       if (it.code != null) {
                           when(it.code){
                               "0" -> {
                                 payment.create(Paiement(
                                     userId = userConnect?.first?.userId!!,
                                     reference = reference,
                                     amount = "$amountCDF",
                                     devise = DeviseType.CDF.name,
                                     description = "Paiement abonnement",
                                     typePayment = TypePayment.MOBILE_MONEY.name,
                                     status = StatusPayment.PENDING.name)
                                 )
                               }
                           }
                       }
                    }
                    result
                }
                2L->{
                    val reference = generateTransactionReference()
                    val transaction = Transaction(phone = request.phone, reference = reference)
                    val result = service.paymentMobileMoney(transaction)
                    result.collect {
                        if (it.code != null) {
                            when(it.code){
                                "0" -> {
                                    payment.create(Paiement(
                                        userId = userConnect?.first?.userId!!,
                                        reference = reference,
                                        amount = transaction.amount,
                                        devise = DeviseType.USD.name,
                                        description = "Paiement abonnement",
                                        typePayment = TypePayment.MOBILE_MONEY.name,
                                        status = StatusPayment.PENDING.name)
                                    )
                                }
                            }
                        }
                    }
                    result
                }
                else -> ResponseEntity.status(404).body(mapOf("message" to "Devise not found"))
            }

        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.payment.mobileMoney.count",
                    distributionName = "api.payment.mobileMoney.latency"
                )
            )
        }
    }

    @Operation(summary = "Abonnement CasaNayo Callback mobile")
    @PostMapping("/{version}/${PaymentScope.PUBLIC}/mobile/callback",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun mobileMoneyCallback(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: TransactionCallBack,
    ) = coroutineScope{
        val startNanos = System.nanoTime()
        try {
            if (!request.code.isNullOrEmpty() && !request.reference.isNullOrEmpty()){
                when(request.code){
                    "0" -> {
                        val state = payment.update(request.reference,request.code)
                        val user = userRepository.findById(state.userId)
                        if (user != null){
                            user.isPremium = true
                            userRepository.save(user)
                        }
                    }
                    else-> {
                        payment.update(request.reference,request.code)
                    }
                }
            }
            log.info("callback, ${request.reference}|${request.orderNumber}")
            ResponseEntity.status(201).body(mapOf("task" to "tache executer avec succes"))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.payment.mobileMoneyCallback.count",
                    distributionName = "api.payment.mobileMoneyCallback.latency"
                )
            )
        }
    }

    @Operation(summary = "Get Payment Log by User")
    @GetMapping("/{version}/${PaymentScope.PROTECTED}/owner/{userId}",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPaiementByUser(
        request: HttpServletRequest,
        @PathVariable("userId") userId : Long,
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        val userConnect = auth.user()
        val isAdmin = userConnect?.second?.filter { true }
        try {
            if (isAdmin!=null){
                if (isAdmin.isNotEmpty()){
                    val data = payment.owner(userId)
                    return@coroutineScope ResponseEntity.status(201).body(mapOf("log" to data ))
                }
                else{
                    if (userConnect.first?.userId == userId){
                        val data = payment.owner(userId)
                        return@coroutineScope ResponseEntity.status(201).body(mapOf("log" to data ))
                    }
                    else return@coroutineScope ResponseEntity.status(404).body(mapOf("message" to "Ce type ne correspond pas à celui de mobile money"))
                }
            }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.paiment.getAllPaiementByUser.count",
                    distributionName = "api.paiment.getAllPaiementByUser.latency"
                )
            )
        }
    }

    @Operation(summary = "Get Payment All")
    @GetMapping("/{version}/${PaymentScope.PROTECTED}/users",
        produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllPaiementAll(request: HttpServletRequest) = coroutineScope {
        val startNanos = System.nanoTime()
        val userConnect = auth.user()
        val isAdmin = userConnect?.second?.filter { true }
        try {
            if (isAdmin!=null){
                if (isAdmin.isNotEmpty()){
                    val data = payment.showAll()
                    return@coroutineScope ResponseEntity.status(201).body(mapOf("payments" to data))
                }
                else{
                     return@coroutineScope ResponseEntity.status(403).body(mapOf("message" to "Accès non autorisé."))
                }
            }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.paiment.getAllPaiementAll.count",
                    distributionName = "api.paiment.getAllPaiementAll.latency"
                )
            )
        }
    }

}