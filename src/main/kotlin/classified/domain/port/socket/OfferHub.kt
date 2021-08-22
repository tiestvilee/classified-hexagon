package classified.domain.port.socket

import classified.domain.model.OfferDetails
import classified.domain.model.OfferId
import dev.forkhandles.result4k.Result

sealed class OfferHubError(message: String) : Exception(message)

interface OfferHub {
    fun createOffer(offer: OfferDetails): Result<OfferId, OfferHubError>
}