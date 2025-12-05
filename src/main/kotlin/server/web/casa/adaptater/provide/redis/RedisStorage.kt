package server.web.casa.adaptater.provide.redis

//import org.slf4j.LoggerFactory
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
//import org.springframework.data.redis.core.RedisTemplate
//import org.springframework.data.redis.serializer.StringRedisSerializer
//import server.web.casa.CasaApplication
//import java.util.concurrent.TimeUnit
//
//class RedisStorage {
//   private val log = LoggerFactory.getLogger(CasaApplication::class.java)
//
//    fun getRedisData(key : String = "foo") : String?{
//        val connectionFactory = LettuceConnectionFactory()
//        val template = RedisTemplate<String?, String?>()
//        connectionFactory.afterPropertiesSet()
//        template.connectionFactory = connectionFactory
//        template.defaultSerializer = StringRedisSerializer.UTF_8
//        template.afterPropertiesSet()
//        log.info("Value at $key:" + template.opsForValue().get(key))
//        template.opsForValue().get(key)
////        connectionFactory.destroy()
//        return template.opsForValue().get(key)
//    }
//    fun delete(key: String){
//        val connectionFactory = LettuceConnectionFactory()
//        val template = RedisTemplate<String?, String?>()
//        connectionFactory.afterPropertiesSet()
//        template.connectionFactory = connectionFactory
//        template.defaultSerializer = StringRedisSerializer.UTF_8
//        template.afterPropertiesSet()
//        template.delete(key)
//        connectionFactory.destroy()
//    }
//    fun storeRedisData(key: String, value : String, time : Long = 5){
//        val connectionFactory = LettuceConnectionFactory()
//        val template = RedisTemplate<String?, String?>()
//        connectionFactory.afterPropertiesSet()
//        template.connectionFactory = connectionFactory
//        template.defaultSerializer = StringRedisSerializer.UTF_8
//        template.afterPropertiesSet()
//        template.opsForValue().set(key, value,time, TimeUnit.MINUTES)
//        connectionFactory.destroy()
//    }
//}