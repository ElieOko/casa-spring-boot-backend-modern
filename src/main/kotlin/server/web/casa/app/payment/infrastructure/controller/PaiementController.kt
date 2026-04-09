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
import org.slf4j.*
import server.web.casa.app.notification.application.service.*
import server.web.casa.app.notification.domain.model.request.*
import server.web.casa.app.notification.infrastructure.persistence.entity.*
import server.web.casa.app.notification.infrastructure.persistence.repository.*
import server.web.casa.app.payment.domain.model.*
import server.web.casa.app.user.application.service.UserService
import server.web.casa.app.user.infrastructure.persistence.repository.*
import server.web.casa.route.payment.*
import server.web.casa.security.*
import server.web.casa.utils.*
import server.web.casa.utils.MessageResponse.PAYMENT_CANCEL
import server.web.casa.utils.MessageResponse.PAYMENT_DECLINE
import server.web.casa.utils.MessageResponse.PAYMENT_SUCCESS
import server.web.casa.utils.scheduler.*
import kotlin.random.*

const val AMOUNT_SUBSCRIPTION = 2
@RestController
@RequestMapping("api")
@Profile("dev")
class PaiementController(
    private val service: FlexPaieService,
    private val sentry: SentryService,
    private val devise : DeviseService,
    private val payment : PaymentService,
    private val auth : Auth,
    private val task : ReservationScheduler,
    private val userRepository: UserRepository,
    private val notificationService: NotificationService,
    private val notification2 : NotificationCasaRepository,
    private val userService: UserService
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Operation(summary = "Abonnement CasaNayo user premium 5$")
    @PostMapping("/{version}/${PaymentScope.PROTECTED}/subscription",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun mobileMoney(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: TransactionRequest, @PathVariable version: String,
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        val userConnect = auth.user()
        val index = (Random.nextInt(1,1_000_000_000)).toLong()
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
                    if (result.code != null) {
                        when(result.code){
                            "0" -> {
                                payment.create(Paiement(
                                    userId = userConnect?.first?.userId!!,
                                    reference = reference,
                                    amount = "$amountCDF",
                                    devise = DeviseType.CDF.name,
                                    description = "Paiement abonnement",
                                    typePayment = TypePayment.MOBILE_MONEY.name,
                                    status = StatusPayment.PENDING.name))
                                task.scheduleOneShot(index, taskType = reference, type = "payment", minute = 2L)
                               }
                           }
                       }
                    result
                }
                2L->{
                    val reference = generateTransactionReference()
                    val transaction = Transaction(phone = request.phone, reference = reference)
                    val result = service.paymentMobileMoney(transaction)
                    if (result.code != null) {
                        when(result.code){
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
                                task.scheduleOneShot(index, taskType = reference, type = "payment", minute = 2L)
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

    @Operation(summary = "Abonnement CasaNayo user premium 5$")
    @PostMapping("/{version}/${PaymentScope.PROTECTED}/card",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun cardPayment(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: TransactionCardRequest, @PathVariable version: String,
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        val userConnect = auth.user()
        val index = (Random.nextInt(1,1_000_000_000)).toLong()
        try {
            when(request.deviseId){
                1L-> {
                    val test = devise.getById(request.deviseId)
                    val amountCDF = test?.tauxLocal?.times(AMOUNT_SUBSCRIPTION)
                    val reference = generateTransactionReference()
                    val transaction = TransactionCard(reference = reference, amount = "$amountCDF", currency = "CDF")
                    val result = service.paymentCard(transaction)
                    if (result.code != null){
                        when(result.code){
                            "0" -> {
                                payment.create(Paiement(
                                    userId = userConnect?.first?.userId!!,
                                    reference = reference,
                                    amount = "$amountCDF",
                                    devise = DeviseType.CDF.name,
                                    description = "Payment abonnement",
                                    typePayment = TypePayment.CARD.name,
                                    status = StatusPayment.PENDING.name))
                                task.scheduleOneShot(index, taskType = reference, type = "payment", minute = 15L)
                            }
                        }
                    }
                    result
                }
                2L->{
                    val reference = generateTransactionReference()
                    val transaction = TransactionCard(reference = reference, amount = AMOUNT_SUBSCRIPTION.toString())
                    val result = service.paymentCard(transaction)
                    if (result.code != null) {
                        when(result.code){
                            "0" -> {
                                payment.create(Paiement(
                                        userId = userConnect?.first?.userId!!,
                                        reference = reference,
                                        amount = transaction.amount,
                                        devise = DeviseType.USD.name,
                                        description = "Paiement abonnement",
                                        typePayment = TypePayment.CARD.name,
                                        status = StatusPayment.PENDING.name))
                                task.scheduleOneShot(index, taskType = reference, type = "payment", minute = 15L)
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
        @PathVariable version: String,
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            if (!request.code.isNullOrEmpty() && !request.reference.isNullOrEmpty()){
                when(request.code){
                    "0" -> {
                        val state = payment.update(request.reference,request.code)
                        val user = userRepository.findById(state.userId)
                        if (user != null){
                            val note = notification2.save(NotificationCasaEntity(
                                    id = null,
                                    userId = user.userId,
                                    title = "CasaNayo Abonnement",
                                    message = PAYMENT_SUCCESS,
                                    tag = TagType.SUBSCRIPTION.toString(),
                                ))
                            val notify = note.toDomain()
                            user.isPremium = true
                            userRepository.save(user)
                            log.info("**********Abonnement Casa: paiement reussie")
                            notify.user = userService.findIdUser(user.userId!!)
                            notificationService.sendNotificationToUser(user.userId.toString(),notify)
                        }
                    }
                    else-> {
                        log.info("***********callback ${request.code}")
//                        payment.update(request.reference,request.code)
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
        @PathVariable("userId") userId : Long, @PathVariable version: String,
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
    suspend fun getAllPaiementAll(request: HttpServletRequest, @PathVariable version: String) = coroutineScope {
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

    @Operation(summary = "Abonnement CasaNayo Callback Card Payment callback")
    @PostMapping("/{version}/${PaymentScope.PUBLIC}/card/callback",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun cardPaymentCallback(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: TransactionCallBack, @PathVariable version: String, ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            if (!request.code.isNullOrEmpty() && !request.reference.isNullOrEmpty()){
                when(request.code){
                    "0" -> {
                        val state = payment.update(request.reference,request.code)
                        val user = userRepository.findById(state.userId)
                        log.info("***********callback ${request.code} | ${request.reference}")
//                        if (user != null){
//                            val note = notification2.save(NotificationCasaEntity(
//                                id = null,
//                                userId = user.userId,
//                                title = "CasaNayo Abonnement",
//                                message = PAYMENT_SUCCESS,
//                                tag = TagType.SUBSCRIPTION.toString(),
//                            ))
//                            val notify = note.toDomain()
//                            user.isPremium = true
//                            userRepository.save(user)
//                            log.info("**********Abonnement Casa: paiement reussie")
//                            notify.user = userService.findIdUser(user.userId!!)
//                            notificationService.sendNotificationToUser(user.userId.toString(),notify)
//                        }
                    }
                    else-> {
                        log.info("***********callback ${request.code}")
//                        payment.update(request.reference,request.code)
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

    @Operation(summary = "Abonnement CasaNayo Callback Card Payment approve")
    @PostMapping("/{version}/${PaymentScope.PUBLIC}/card/callback/approve",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun cardPaymentCallbackApprove(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: TransactionCallBack, @PathVariable version: String, ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            if (!request.code.isNullOrEmpty() && !request.reference.isNullOrEmpty()){
                when(request.code){
                    "0" -> {
                        val state = payment.update(request.reference,request.code)
                        val user = userRepository.findById(state.userId)
                        if (user != null){
                            val note = notification2.save(NotificationCasaEntity(
                                id = null,
                                userId = user.userId,
                                title = "CasaNayo Abonnement",
                                message = PAYMENT_SUCCESS,
                                tag = TagType.SUBSCRIPTION.toString(),
                            ))
                            val notify = note.toDomain()
                            user.isPremium = true
                            userRepository.save(user)
                            log.info("**********Abonnement Casa: paiement reussie")
                            notify.user = userService.findIdUser(user.userId!!)
                            notificationService.sendNotificationToUser(user.userId.toString(),notify)
                        }
                    }
                    else-> {
                        log.info("***********callback ${request.code}")
//                        payment.update(request.reference,request.code)
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

    @Operation(summary = "Abonnement CasaNayo Callback Card Payment cancel")
    @PostMapping("/{version}/${PaymentScope.PUBLIC}/card/callback/cancel",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun cardPaymentCallbackCancel(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: TransactionCallBack, @PathVariable version: String,
    ) = coroutineScope{
        val startNanos = System.nanoTime()
        try {
            if (!request.code.isNullOrEmpty() && !request.reference.isNullOrEmpty()){
                when(request.code){
                    "1" -> {
                        val state = payment.update(request.reference,request.code)
                        val user = userRepository.findById(state.userId)
                        if (user != null){
                            val note = notification2.save(NotificationCasaEntity(
                                id = null,
                                userId = user.userId,
                                title = "CasaNayo Abonnement",
                                message = PAYMENT_CANCEL,
                                tag = TagType.SUBSCRIPTION.toString(),
                            ))
                            val notify = note.toDomain()
                            userRepository.save(user)
                            log.info("**********Abonnement Casa: paiement annulé")
                            notify.user = userService.findIdUser(user.userId!!)
                            notificationService.sendNotificationToUser(user.userId.toString(),notify)
                        }
                    }
                    else-> {
                        log.info("***********callback ${request.code}")
//                        payment.update(request.reference,request.code)
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

    @Operation(summary = "Abonnement CasaNayo Callback Card Payment decline")
    @PostMapping("/{version}/${PaymentScope.PUBLIC}/card/callback/decline",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun cardPaymentCallbackDecline(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: TransactionCallBack, @PathVariable version: String,
    ) = coroutineScope{
        val startNanos = System.nanoTime()
        try {
            if (!request.code.isNullOrEmpty() && !request.reference.isNullOrEmpty()){
                when(request.code){
                    "1" -> {
                        val state = payment.update(request.reference,request.code)
                        val user = userRepository.findById(state.userId)
                        if (user != null){
                            val note = notification2.save(NotificationCasaEntity(
                                id = null,
                                userId = user.userId,
                                title = "CasaNayo Abonnement",
                                message = PAYMENT_DECLINE,
                                tag = TagType.SUBSCRIPTION.toString(),
                            ))
                            val notify = note.toDomain()
                            user.isPremium = true
                            userRepository.save(user)
                            log.info("**********Abonnement Casa: paiement non approuvé")
                            notify.user = userService.findIdUser(user.userId!!)
                            notificationService.sendNotificationToUser(user.userId.toString(),notify)
                        }
                    }
                    else-> {
                        log.info("***********callback ${request.code}")
//                        payment.update(request.reference,request.code)
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
                    countName = "api.payment.cardPaymentCallbackDecline.count",
                    distributionName = "api.payment.cardPaymentCallbackDecline.latency"
                )
            )
        }
    }
}