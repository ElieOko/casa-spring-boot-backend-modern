package server.web.casa.utils

import java.security.MessageDigest
import java.util.UUID

fun generateTransactionReference(): String {
    val input = UUID.randomUUID().toString() + System.nanoTime()
    val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}