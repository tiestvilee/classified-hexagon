package classified.offer.port.socket

import classified.domain.model.AdId
import classified.domain.model.Offer
import classified.domain.model.OfferDetails
import classified.domain.model.OfferId
import dev.forkhandles.result4k.Result

sealed class OfferHubError(message: String) : Exception(message) {
    class OfferNotFound(message: String) : OfferHubError(message)
}

interface OfferHub {
    fun createOffer(offer: OfferDetails): Result<OfferId, OfferHubError>
    fun offersFor(adId: AdId): Result<List<Offer>, OfferHubError>
    fun acceptOffer(offerId: OfferId): Result<Unit, OfferHubError>
    fun offer(offerId: OfferId): Result<Offer, OfferHubError>
    fun itemMailed(offerId: OfferId): Result<Unit, OfferHubError>
    fun itemReceived(offerId: OfferId): Result<Unit, OfferHubError>
}