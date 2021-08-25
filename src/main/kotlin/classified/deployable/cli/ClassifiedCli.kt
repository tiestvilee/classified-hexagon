package classified.deployable.cli

import classified.domain.ad.adapter.adCliProcessor
import classified.domain.ad.port.socket.AdHub
import classified.domain.model.AdId
import classified.domain.model.OfferDetails
import classified.domain.offer.port.socket.OfferHub
import classified.domain.payment.model.Money
import classified.domain.payment.port.socket.PaymentHub
import dev.forkhandles.result4k.orThrow
import java.math.BigDecimal

class AdCliParserError(message: String) : Exception(message)

class ClassifiedCli(private val adHub: AdHub, private val offerHub: OfferHub, private val paymentHub: PaymentHub) {
    fun process(command: String): String {
        val parts = command.split(Regex("""\s"""))
        val domain = parts.first()
        val command = parts.drop(1).first()
        val params = parts.drop(2)
        return when (domain) {
            "ad" -> {
                adCliProcessor(adHub, domain, command, params)
            }
            "offer" -> {
                when (command) {
                    "create" -> {
                        val offerId = offerHub.createOffer(
                            OfferDetails(
                                AdId.parse(params[0]),
                                Money(BigDecimal(params[1]))
                            )
                        ).orThrow()
                        offerId.value.toString()
                    }
                    else -> {
                        throw AdCliParserError("Don't understand command $command in domain $domain")
                    }
                }
            }
            "payment" -> {
                when (command) {
                    "" -> {
                        return "todo"
                    }
                    else -> {
                        throw AdCliParserError("Don't understand command $command in domain $domain")
                    }
                }
            }
            else -> {
                throw AdCliParserError("Dont' understand domain $domain")
            }
        }
    }
}

