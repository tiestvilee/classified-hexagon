package classified.deployable.cli

import classified.domain.ad.adapter.dependency.FileAdRepository
import classified.domain.ad.adapter.service.CliAdHubServer
import classified.domain.ad.port.service.AdHub
import classified.domain.offer.adapter.dependency.InMemoryOfferRepository
import classified.domain.offer.adapter.service.offerCliProcessor
import classified.domain.offer.port.service.OfferHub
import classified.domain.payment.adapter.dependency.InMemoryPaymentRepository
import classified.domain.payment.adapter.dependency.PaymentProviderFake
import classified.domain.payment.adapter.service.paymentCliProcessor
import classified.domain.payment.port.service.PaymentHub
import java.io.File

class ClassifiedCliParserError(message: String) : Exception(message)

class ClassifiedCli(adHub: AdHub, private val offerHub: OfferHub, private val paymentHub: PaymentHub) {

    private val adCliProcessor = CliAdHubServer(adHub)

    fun process(commandLine: String): String {
        val parts = commandLine.split(Regex("""\s"""))
        val domain = parts.first()
        val command = parts.drop(1).first()
        val params = parts.drop(2)
        return when (domain) {
            "ad" -> {
                adCliProcessor.process(command, params)
            }
            "offer" -> {
                offerCliProcessor(offerHub, domain, command, params)
            }
            "payment" -> {
                paymentCliProcessor(command, params, domain, paymentHub)
            }
            else -> {
                throw ClassifiedCliParserError("Dont' understand domain $domain")
            }
        }
    }
}

fun main(args: Array<String>) {
    println(
        ClassifiedCli(
            classified.domain.ad.hub.AdHub(FileAdRepository(File("./ads.db"))),
            classified.domain.offer.hub.OfferHub(InMemoryOfferRepository()),
            classified.domain.payment.hub.PaymentHub(InMemoryPaymentRepository(), PaymentProviderFake())
        )
            .process(args.joinToString(" "))
    )
}


