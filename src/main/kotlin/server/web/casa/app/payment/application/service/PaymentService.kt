package server.web.casa.app.payment.application.service

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.springframework.http.*
import org.springframework.stereotype.*
import org.springframework.web.server.*
import server.web.casa.app.payment.domain.model.*
import server.web.casa.app.payment.infrastructure.persistence.entity.*
import server.web.casa.app.payment.infrastructure.persistence.repository.*
import server.web.casa.app.user.application.service.*

@Service
class PaymentService(
   private val repository: PaiementRepository,
   private val user : UserService
){
    suspend fun create(model : Paiement) = coroutineScope {
        repository.save(model.toEntity()).toDomain()
    }

    private suspend fun logPayment(userId : Long) =  coroutineScope{
        repository.findByUser(userId)
    }

   suspend fun update(reference: String, code : String) = coroutineScope {
       when(code){
           "0" -> {
               val data = referencePayment(reference)
               data?.status = StatusPayment.SUCCESS.name
               repository.save(data!!)
           }
           else -> {
               val data = referencePayment(reference)
               data?.status = StatusPayment.CANCELLED.name
               repository.save(data!!)
           }
       }

   }

    private suspend fun referencePayment(reference : String) = coroutineScope {
        val state = repository.findByReference(reference).toList()
        if (state.isNotEmpty()) state[0] else throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette reference n'existe pas !!.")
    }

    suspend fun showDetail(id : Long) = coroutineScope {
        val data = repository.findById(id) ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "ID Is Not Found."
        )
        data.toDomain()
    }

    suspend fun showAll() = coroutineScope {
        val items = mutableListOf<PaymentDTO>()
        repository.findAll().collect { items.add(owner(it.userId)) }
        items
    }

    suspend fun owner(userId: Long) = coroutineScope {
        val items = mutableListOf<Paiement>()
        val userDto = user.findIdUser(userId)
        logPayment(userId).collect { items.add(it!!.toDomain()) }
        PaymentDTO(payment = items, user = userDto)
    }
}