package server.web.casa.security.otp

import org.springframework.stereotype.Service
import java.security.SecureRandom


//@Service
//class OtpService {
//    fun generateOtpForUser(userId: String?): String {
//        val otp = generateOTP(6) // code à 6 chiffres
//        val redis = RedisStorage()
//
//        redis.storeRedisData("OTP:$userId",otp,System.currentTimeMillis() + 5 * 60 * 1000)
//        return otp
//    }
//
//    fun validateOtp(userId: String?, otp: String?): Boolean {
//        val key = "OTP:$userId"
//        val redis = RedisStorage()
//        val stored: String? = redis.getRedisData(key)
//        if (stored != null && stored == otp) {
//            redis.delete(key)
//            return true
//        }
//        return false
//    }
//
//    private fun generateOTP(length: Int): String {
//        val random: SecureRandom = SecureRandom()
//        val sb = StringBuilder()
//        for (i in 0..<length) {
//            sb.append(random.nextInt(10))
//        }
//        return sb.toString()
//    }
//}