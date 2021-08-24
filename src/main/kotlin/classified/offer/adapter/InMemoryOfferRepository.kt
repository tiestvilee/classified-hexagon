package classified.offer.adapter

import classified.domain.model.AdId
import classified.domain.model.Offer
import classified.domain.model.OfferDetails
import classified.domain.model.OfferId
import classified.domain.model.OfferState.Offered
import classified.domain.port.socket.OfferHubError
import classified.domain.port.socket.OfferHubError.OfferNotFound
import classified.offer.port.plug.OfferRepository
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success

class InMemoryOfferRepository : OfferRepository {
    private val offers = mutableMapOf<OfferId, Offer>()
    override fun insertOffer(offer: OfferDetails): Result<OfferId, OfferHubError> {
        val offerId = OfferId.random()
        offers[offerId] = Offer(offerId, Offered, offer)
        return Success(offerId)
    }

    override fun offersFor(adId: AdId): Result<List<Offer>, OfferHubError> {
        return Success(offers.values.filter { it.details.adId == adId })
    }

    override fun offer(offerId: OfferId): Result<Offer, OfferHubError> {
        return offers[offerId]?.let { Success(it) } ?: Failure(OfferNotFound("Didn't find offer $offerId"))
    }

    override fun updateOffer(updated: Offer): Result<Unit, OfferHubError> {
        offers[updated.id] = updated
        return Success(Unit)
    }
}