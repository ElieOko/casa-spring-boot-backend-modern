package server.web.casa.adaptater.provide.vonage

//import org.slf4j.LoggerFactory
//import org.springframework.stereotype.Service
//
//@Service
//class VonageClientProvider{
//
////    private val log = LoggerFactory.getLogger(this::class.java)
////    fun sendMessage(body: String = "", to : String = "243827824163") : Boolean{
////        val client: Vonage = Vonage {
////            apiKey("");
////            apiSecret("")
////            applicationId("ce92972c-72fa-48f0-ab3f-b83db2a40f27")
////        }
////        val response =  client.sms.sendText(
////            "Vonage APIs",
////            to,
////            "Votre code de vérification est $body. Pour votre sécurité, ne le partagez pas.")
////
////        if (response.wasSuccessfullySent()){
////         return true
////        }
////        log.info("*******************")
////        log.info(response[0].errorText)
////        return false
////    }
//}