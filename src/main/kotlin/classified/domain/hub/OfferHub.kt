package classified.domain.hub

import classified.domain.model.*
import classified.domain.port.plug.OfferRepository
import classified.domain.port.socket.OfferHubError
import dev.forkhandles.result4k.*

class OfferHub(val repo: OfferRepository) : classified.domain.port.socket.OfferHub {
    override fun createOffer(offer: OfferDetails): Result<OfferId, OfferHubError> {
        return repo.insertOffer(offer)
    }

    override fun offersFor(adId: AdId): Result<List<Offer>, OfferHubError> {
        return repo.offersFor(adId)
    }

    override fun acceptOffer(offerId: OfferId): Result<Unit, OfferHubError> {
        return repo.offer(offerId).flatMap {
            repo.updateOffer(it.copy(state = OfferState.Accepted))
        }.map {
            Success(Unit)
        }.mapFailure {
            Failure(it)
        }.get()
    }
}