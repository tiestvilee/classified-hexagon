package classified.domain.adapter

import classified.domain.model.Offer
import classified.domain.model.OfferDetails
import classified.domain.model.OfferId
import classified.domain.port.plug.OfferRepository
import classified.domain.port.socket.OfferHubError
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success

class InMemoryOfferRepository : OfferRepository {
    val offers = mutableMapOf<OfferId, Offer>()
    override fun insertOffer(item: OfferDetails): Result<OfferId, OfferHubError> {
        val offerId = OfferId.random()
        offers[offerId] = Offer(offerId, item)
        return Success(offerId)
    }
}