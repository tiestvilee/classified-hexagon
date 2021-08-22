package classified.domain.port.socket

import classified.domain.model.AdDetails
import classified.domain.model.AdId
import dev.forkhandles.result4k.Result

sealed class AdHubError(message: String) : Exception(message) {

}

interface AdHub {
    fun createAd(item: AdDetails): Result<AdId, AdHubError>
}