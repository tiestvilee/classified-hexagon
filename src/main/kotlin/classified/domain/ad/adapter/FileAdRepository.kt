package classified.domain.ad.adapter

import classified.domain.ad.port.plug.AdRepository
import classified.domain.ad.port.socket.AdHubError
import classified.domain.model.Ad
import classified.domain.model.AdDetails
import classified.domain.model.AdId
import classified.domain.model.AdState
import classified.domain.payment.model.Money
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.map
import java.io.File
import java.math.BigDecimal

class FileAdRepository(val theFile: File) : AdRepository {
    private val ads = InMemoryAdRepository()

    init {
        if (theFile.exists()) {
            deserialiseAds(theFile.readText()).map { ad ->
                ads.insertAd(ad)
            }
        }
    }

    override fun insertAd(item: AdDetails): Result<AdId, AdHubError> {
        return ads.insertAd(item).also { flush() }
    }

    override fun findAdByName(adName: String): Result<Ad, AdHubError> {
        return ads.findAdByName(adName)
    }

    override fun ad(adId: AdId): Result<Ad, AdHubError> {
        return ads.ad(adId)
    }

    override fun ads(): Result<List<Ad>, AdHubError> {
        return ads.ads()
    }

    private fun flush() {
        theFile.bufferedWriter().use { writer ->
            ads.ads().map { list ->
                writer.write(serialiseAds(list))
            }
        }
    }

}

private const val recordSeparator = "\u001E\n"
private const val fieldSeparator = "\u001F\t"

private fun serialiseAds(list: List<Ad>) = list.joinToString(recordSeparator) { serialiseAd(it) }

fun serialiseAd(ad: Ad): String {
    return ad.id.toString() + fieldSeparator +
            ad.state + fieldSeparator +
            ad.details.name + fieldSeparator +
            ad.details.askingPrice.amount.toPlainString()
}

fun deserialiseAds(db: String): List<Ad> {
    return db.split(recordSeparator).flatMap { record ->
        val fields = record.split(fieldSeparator)
        if (fields.size == 4) {
            listOf(
                Ad(
                    AdId.parse(fields[0]),
                    AdState.valueOf(fields[1]),
                    AdDetails(
                        fields[2],
                        Money(BigDecimal(fields[3]))
                    )
                )
            )
        } else {
            emptyList()
        }
    }
}
