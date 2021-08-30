package classified.domain.ad.adapter

import classified.domain.ad.port.socket.AdHub
import classified.domain.ad.port.socket.AdHubError
import classified.domain.model.Ad
import classified.domain.model.AdDetails
import classified.domain.model.AdId
import classified.domain.model.AdState
import classified.domain.payment.model.Money
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.mapFailure
import dev.forkhandles.result4k.orThrow
import java.math.BigDecimal

class CliAdParserError(message: String) : Exception(message)

class CliAdHubServer(private val adHub: AdHub) {
    fun process(
        command: String,
        params: List<String>
    ) = when (command) {
        "create" -> {
            val adId = adHub.createAd(
                AdDetails(
                    params.dropLast(1).joinToString(" ").drop(1).dropLast(1),
                    Money(BigDecimal(params.last()))
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
                    throw CliAdParserError("Don't understand qualifier ${params.get(0)} for command $command in domain Ad")
                }
            }
        }
        else -> {
            throw CliAdParserError("Don't understand command $command in domain Ad")
        }
    }

    fun process() = { input: String ->
        val parts = input.split(" ")
        process(parts[0], parts.drop(1))
    }
}

class CliAdHubClient(val cliProcessor: (String) -> String) : AdHub {
    override fun createAd(item: AdDetails): Result<AdId, AdHubError> {
        return cliProcessor("create '${item.name}' ${item.askingPrice.amount}").toAdId()
            .mapFailure { AdHubError.RepositoryFailure(it.message!!) }
    }

    override fun findAdByName(adName: String): Result<Ad, AdHubError> {
        return cliProcessor("find -name $adName").toAd()
            .mapFailure { AdHubError.RepositoryFailure(it.message!!) }
    }

    override fun ad(adId: AdId): Result<Ad, AdHubError> {
        return cliProcessor("find -id ${adId.value}").toAd()
            .mapFailure { AdHubError.RepositoryFailure(it.message!!) }
    }

}

fun AdId.toCliString(): String = value.toString()

fun String.toAdId(): Result<AdId, CliAdParserError> = Success(AdId.parse(this))

fun Ad.toCliString() =
    "${id.value} $state ${details.name} ${details.askingPrice.amount}"

fun String.toAd(): Result<Ad, CliAdParserError> {
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
