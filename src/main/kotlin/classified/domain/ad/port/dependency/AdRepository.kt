package classified.domain.ad.port.dependency

import classified.domain.ad.port.service.AdHubError
import classified.domain.model.Ad
import classified.domain.model.AdDetails
import classified.domain.model.AdId
import dev.forkhandles.result4k.Result

interface AdRepository {
    fun insertAd(item: AdDetails): Result<AdId, AdHubError>
    fun findAdByName(adName: String): Result<Ad, AdHubError>
    fun ad(adId: AdId): Result<Ad, AdHubError>
    fun ads(): Result<List<Ad>, AdHubError>
}
