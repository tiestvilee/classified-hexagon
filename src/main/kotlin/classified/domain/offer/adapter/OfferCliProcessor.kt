package classified.domain.offer.adapter

import classified.domain.ad.adapter.toAdId
import classified.domain.model.*
import classified.domain.offer.port.socket.OfferHub
import classified.domain.payment.model.Money
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.orThrow
import java.math.BigDecimal

class OfferCliParserError(message: String) : Exception(message)

fun offerCliProcessor(
    offerHub: OfferHub,
    domain: String,
    command: String,
    params: List<String>
) = when (command) {
    "create" -> {
        val offerId = offerHub.createOffer(
            OfferDetails(
                AdId.parse(params[0]),
                Money(BigDecimal(params[1]))
            )
        ).orThrow()
        offerId.toCliString()
    }
    "accept" -> {
        offerHub.acceptOffer(params[0].toOfferId().orThrow())
        ""
    }
    "mailed" -> {
        offerHub.itemMailed(params[0].toOfferId().orThrow())
        ""
    }
    "received" -> {
        offerHub.itemReceived(params[0].toOfferId().orThrow())
        ""
    }
    "find" -> {
        when (params[0]) {
            "-id" -> {
                offerHub.offer(params[1].toOfferId().orThrow()).orThrow().toCliString()
            }
            "-ad" -> {
                offerHub.offersFor(params[1].toAdId().orThrow()).orThrow().toCliString()
            }
            else -> {
                throw OfferCliParserError("Don't understand qualifier ${params.get(0)} for command $command in domain $domain")
            }
        }
    }
    else -> {
        throw OfferCliParserError("Don't understand command $command in domain $domain")
    }
}

fun List<Offer>.toCliString() =
    joinToString("\n") { offer ->
        offer.toCliString()
    }

fun OfferId.toCliString() = value.toString()

fun String.toOfferId(): Result<OfferId, OfferCliParserError> {
    return Success(OfferId.parse(this))
}

fun Offer.toCliString() =
    "${id.value} $state ${details.adId.value} ${details.offer.amount}"

fun String.toOffer(): Result<Offer, OfferCliParserError> {
    val params = this.split(Regex("\\s"))
    return Success(
        Offer(
            params[0].toOfferId().orThrow(),
            OfferState.valueOf(params[1]),
            OfferDetails(
                params[2].toAdId().orThrow(),
                Money(BigDecimal(params[3]))
            )
        )
    )
}

fun String.toOffers(): Result<List<Offer>, OfferCliParserError> {
    return Success(this.split("\n").map {
        it.toOffer().orThrow()
    })
}