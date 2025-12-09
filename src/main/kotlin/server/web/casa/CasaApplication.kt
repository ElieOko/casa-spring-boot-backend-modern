package server.web.casa

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import server.web.casa.utils.Mode
import server.web.casa.utils.storage.StorageProperties


@SpringBootApplication
@EnableConfigurationProperties(StorageProperties::class)
@Profile(Mode.DEV)
class CasaApplication

fun main(args: Array<String>) {

	runApplication<CasaApplication>(*args)
//    val redis = RedisStorage()
//    redis.storeRedisData("user1","ElieOko")
//    redis.storeRedisData("user2","Hd&Fils")
//    redis.getRedisData("user1")
//    redis.delete("user1")
//    redis.getRedisData("user2")
//    redis.getRedisData("user1")
}

@Controller
@Profile(Mode.DEV)
class HomeController {
    @GetMapping("/")
    fun home():String {
        return  "index"
    }
}