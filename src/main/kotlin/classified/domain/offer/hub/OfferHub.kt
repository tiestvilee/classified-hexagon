package classified.domain.offer.hub

import classified.domain.model.*
import classified.domain.offer.port.dependency.OfferRepository
import classified.domain.offer.port.service.OfferHub
import classified.domain.offer.port.service.OfferHubError
import dev.forkhandles.result4k.*

class OfferHub(private val repo: OfferRepository) : OfferHub {
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

    override fun offer(offerId: OfferId): Result<Offer, OfferHubError> {
        return repo.offer(offerId)
    }

    override fun itemMailed(offerId: OfferId): Result<Unit, OfferHubError> {
        return repo.offer(offerId).flatMap {
            repo.updateOffer(it.copy(state = OfferState.OutForDelivery))
        }.map {
            Success(Unit)
        }.mapFailure {
            Failure(it)
        }.get()
    }

    override fun itemReceived(offerId: OfferId): Result<Unit, OfferHubError> {
        return repo.offer(offerId).flatMap {
            repo.updateOffer(it.copy(state = OfferState.ItemReceived))
        }.map {
            Success(Unit)
        }.mapFailure {
            Failure(it)
        }.get()
    }
}