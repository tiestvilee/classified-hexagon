package classified.domain.ad.port.socket

import classified.domain.model.Ad
import classified.domain.model.AdDetails
import classified.domain.model.AdId
import dev.forkhandles.result4k.Result

sealed class AdHubError(message: String) : Exception(message) {
    class AdNotFound(message: String) : AdHubError(message)
    class RepositoryFailure(message: String) : AdHubError(message)
}

interface AdHub {
    fun createAd(item: AdDetails): Result<AdId, AdHubError>
    fun findAdByName(adName: String): Result<Ad, AdHubError>
    fun ad(adId: AdId): Result<Ad, AdHubError>
}