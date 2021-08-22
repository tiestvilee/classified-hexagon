package classified.domain.adapter

import classified.domain.model.Ad
import classified.domain.model.AdDetails
import classified.domain.model.AdId
import classified.domain.model.AdState
import classified.domain.port.plug.AdRepository
import classified.domain.port.socket.AdHubError
import classified.domain.port.socket.AdHubError.AdNotFound
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success

class InMemoryAdRepository : AdRepository {
    val ads = mutableMapOf<AdId, Ad>()
    override fun insertAd(item: AdDetails): Result<AdId, AdHubError> {
        val adId = AdId.random()
        ads[adId] = Ad(adId, AdState.Available, item)
        return Success(adId)
    }

    override fun findAdByName(adName: String): Result<Ad, AdHubError> {
        return ads.values.find { it.details.name == adName }?.let {
            Success(it)
        } ?: Failure(AdNotFound("with name $adName"))
    }

    override fun ad(adId: AdId): Result<Ad, AdHubError> {
        return ads[adId]?.let {
            Success(it)
        } ?: Failure(AdNotFound("with id $adId"))
    }
}