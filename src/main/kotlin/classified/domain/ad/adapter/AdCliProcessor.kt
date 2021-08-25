package classified.domain.ad.adapter

import classified.deployable.cli.AdCliParserError
import classified.domain.ad.port.socket.AdHub
import classified.domain.model.Ad
import classified.domain.model.AdDetails
import classified.domain.model.AdId
import classified.domain.model.AdState
import classified.domain.payment.model.Money
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.orThrow
import java.math.BigDecimal

fun adCliProcessor(
    adHub: AdHub,
    domain: String,
    command: String,
    params: List<String>
) = when (command) {
    "create" -> {
        val adId = adHub.createAd(
            AdDetails(
                params.dropLast(1).joinToString(" ").drop(1).dropLast(1),
                Money(BigDecimal(params.last().drop(1))) // drop the '$' sign
            )
        ).orThrow()
        adId.toCliString()
    }
    "find" -> {
        when (params[0]) {
            "-id" -> {
                val ad = adHub.ad(AdId.parse(params[1])).orThrow()
                ad.toCliString()
            }
            "-name" -> {
                val ad = adHub.findAdByName(params.drop(1).joinToString(" ")).orThrow()
                ad.toCliString()
            }
            else -> {
                throw AdCliParserError("Don't understand qualifier ${params.get(0)} for command $command in domain $domain")
            }
        }
    }
    else -> {
        throw AdCliParserError("Don't understand command $command in domain $domain")
    }
}

fun AdId.toCliString(): String = value.toString()

fun String.toAdId(): Result<AdId, AdCliParserError> = Success(AdId.parse(this))

fun Ad.toCliString() =
    "${id.value} $state ${details.name} ${details.askingPrice.amount}"

fun String.toAd(): Result<Ad, AdCliParserError> {
    val parts = this.split(Regex("\\s"))
    return Success(
        Ad(
            parts[0].toAdId().orThrow(),
            AdState.valueOf(parts[1]),
            AdDetails(
                parts.drop(2).dropLast(1).joinToString(" "),
                Money(BigDecimal(parts.last()))
            )
        )
    )
}
