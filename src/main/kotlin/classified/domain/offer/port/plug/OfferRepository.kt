package classified.domain.offer.port.plug

import classified.domain.model.AdId
import classified.domain.model.Offer
import classified.domain.model.OfferDetails
import classified.domain.model.OfferId
import classified.domain.offer.port.socket.OfferHubError
import dev.forkhandles.result4k.Result

interface OfferRepository {
    fun insertOffer(offer: OfferDetails): Result<OfferId, OfferHubError>
    fun offersFor(adId: AdId): Result<List<Offer>, OfferHubError>
    fun offer(offerId: OfferId): Result<Offer, OfferHubError>
    fun updateOffer(updated: Offer): Result<Unit, OfferHubError>
}