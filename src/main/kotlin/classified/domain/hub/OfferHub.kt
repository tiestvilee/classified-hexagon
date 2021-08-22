package classified.domain.hub

import classified.domain.model.OfferDetails
import classified.domain.model.OfferId
import classified.domain.port.plug.OfferRepository
import classified.domain.port.socket.OfferHubError
import dev.forkhandles.result4k.Result

class OfferHub(val repo: OfferRepository) : classified.domain.port.socket.OfferHub {
    override fun createOffer(offer: OfferDetails): Result<OfferId, OfferHubError> {
        return repo.insertOffer(offer)
    }
}