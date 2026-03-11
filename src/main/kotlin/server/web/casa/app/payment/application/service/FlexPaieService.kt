package server.web.casa.app.payment.application.service

import org.springframework.beans.factory.annotation.*
import org.springframework.http.*
import org.springframework.stereotype.*
import org.springframework.web.reactive.function.client.*
import reactor.core.publisher.*
import server.web.casa.app.payment.domain.model.*

//@Service
//class FlexPaieService(
//   @Value("\${app.api.key}")
//   private val apiKeyFlex: String,
//   private val builder: WebClient.Builder
//) {
//    private val clientMobile: WebClient = builder.baseUrl("https://backend.flexpay.cd/api/rest/v1/paymentService").build()
//    private val clientCard: WebClient = builder.baseUrl("https://cardpayment.flexpay.cd/v1.1/pay").build()
//    private val clientCheck: WebClient = builder.baseUrl("https://apicheck.flexpaie.com/api/rest/v1/check").build()
//
//    suspend fun paymentMobileMoney(transaction: Transaction) = clientMobile
//        .post()
//        .header(HttpHeaders.AUTHORIZATION, apiKeyFlex)
//        .contentType(MediaType.APPLICATION_JSON)
//        .bodyValue(transaction)
//        .retrieve()
//        .onStatus(HttpStatusCode::is4xxClientError) {
//            it.bodyToMono<String>().map { msg -> RuntimeException("Client error: $msg") }
//        }
//        .onStatus(HttpStatusCode::is5xxServerError) {
//            it.bodyToMono<String>().map { msg -> RuntimeException("Problem Server error: $msg") }
//        }
//        .bodyToMono<ResponseTransaction>()
//
//    suspend fun checkStateTransaction(orderNumber: String): Mono<FlexCheck> = clientCheck
//        .get()
//        .uri("/${orderNumber}")
//        .header(HttpHeaders.AUTHORIZATION, apiKeyFlex)
//        .accept(MediaType.APPLICATION_JSON)
//        .retrieve()
//        .onStatus(HttpStatusCode::is4xxClientError) {
//            it.bodyToMono<String>().map { msg -> RuntimeException("Client error: $msg") }
//        }
//        .onStatus(HttpStatusCode::is5xxServerError) {
//            it.bodyToMono<String>().map { msg -> RuntimeException("Problem Server error: $msg") }
//        }
//        .bodyToMono<FlexCheck>()
//        .map { it }
//
//    suspend fun paymentCard(transaction : TransactionCard){
//        clientCard
//            .post()
//            .header(HttpHeaders.AUTHORIZATION, apiKeyFlex)
//            .contentType(MediaType.APPLICATION_JSON)
//            .bodyValue(transaction)
//            .retrieve()
//            .onStatus(HttpStatusCode::is4xxClientError) {
//                it.bodyToMono<String>().map { msg -> RuntimeException("Client error: $msg") }
//            }
//            .onStatus(HttpStatusCode::is5xxServerError) {
//                it.bodyToMono<String>().map { msg -> RuntimeException("Problem Server error: $msg") }
//            }
//            .bodyToMono<ResponseTransaction>()
//    }
//
//}