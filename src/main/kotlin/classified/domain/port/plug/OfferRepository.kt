package classified.domain.port.plug

import classified.domain.model.OfferDetails
import classified.domain.model.OfferId
import classified.domain.port.socket.OfferHubError
import dev.forkhandles.result4k.Result

interface OfferRepository {
    fun insertOffer(offer: OfferDetails): Result<OfferId, OfferHubError>
}