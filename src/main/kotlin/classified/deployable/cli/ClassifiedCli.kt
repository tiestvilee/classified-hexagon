package classified.deployable.cli

import classified.domain.ad.adapter.adCliProcessor
import classified.domain.ad.port.socket.AdHub
import classified.domain.offer.adapter.offerCliProcessor
import classified.domain.offer.port.socket.OfferHub
import classified.domain.payment.port.socket.PaymentHub

class ClassifiedCliParserError(message: String) : Exception(message)

class ClassifiedCli(private val adHub: AdHub, private val offerHub: OfferHub, private val paymentHub: PaymentHub) {
    fun process(commandLine: String): String {
        val parts = commandLine.split(Regex("""\s"""))
        val domain = parts.first()
        val command = parts.drop(1).first()
        val params = parts.drop(2)
        return when (domain) {
            "ad" -> {
                adCliProcessor(adHub, domain, command, params)
            }
            "offer" -> {
                offerCliProcessor(offerHub, domain, command, params)
            }
            "payment" -> {
                when (command) {
                    "" -> {
                        return "todo"
                    }
                    else -> {
                        throw ClassifiedCliParserError("Don't understand command $command in domain $domain")
                    }
                }
            }
            else -> {
                throw ClassifiedCliParserError("Dont' understand domain $domain")
            }
        }
    }

}


