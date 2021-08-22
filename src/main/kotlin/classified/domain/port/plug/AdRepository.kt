package classified.domain.port.plug

import classified.domain.model.AdDetails
import classified.domain.model.AdId
import classified.domain.port.socket.AdHubError
import dev.forkhandles.result4k.Result

interface AdRepository {
    fun insertAd(item: AdDetails): Result<AdId, AdHubError>

}