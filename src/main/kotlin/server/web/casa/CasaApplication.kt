package server.web.casa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import server.web.casa.utils.Mode

@SpringBootApplication
@Profile(Mode.DEV)
class CasaApplication

fun main(args: Array<String>) {
	runApplication<CasaApplication>(*args)
}

@Controller
@Profile(Mode.DEV)
class HomeController {
    @GetMapping("/")
    fun home():String {
        return  "index"
    }
}