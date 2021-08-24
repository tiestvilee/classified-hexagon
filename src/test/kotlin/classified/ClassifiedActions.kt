package classified

import classified.ad.adapter.InMemoryAdRepository
import classified.domain.adapter.InMemoryOfferRepository
import classified.domain.adapter.InMemoryPaymentRepository
import classified.domain.model.*
import classified.domain.port.socket.AdHub
import classified.domain.port.socket.OfferHub
import classified.domain.port.socket.PaymentHub
import com.ubertob.pesticide.core.*
import dev.forkhandles.result4k.orThrow

class ClassifiedActions : DomainActions<DdtProtocol> {
    override val protocol: DdtProtocol = DomainOnly

    override fun prepare(): DomainSetUp = Ready
    fun usersExistInTheSystem(vararg users: ClassifiedUser) {
        // do something?
    }

    private val adHub: AdHub = classified.ad.hub.AdHub(
        InMemoryAdRepository()
    )

    private val offerHub: OfferHub = classified.domain.hub.OfferHub(
        InMemoryOfferRepository()
    )

    private val paymentHub: PaymentHub = classified.domain.hub.PaymentHub(
        InMemoryPaymentRepository()
    )

    fun createAd(item: AdDetails): AdId {
        return adHub.createAd(item).orThrow()
    }

    fun findOffersFor(adId: AdId): List<Offer> {
        return offerHub.offersFor(adId).orThrow()
    }

    fun acceptOffer(offerId: OfferId) {
        offerHub.acceptOffer(offerId).orThrow()
    }

    fun itemMailed(offerId: OfferId) {
        offerHub.itemMailed(offerId).orThrow()
    }

    fun findItem(itemName: String): AdId {
        return adHub.findAdByName(itemName).orThrow().id
    }

    fun createOffer(offer: OfferDetails): OfferId {
        return offerHub.createOffer(offer).orThrow()
    }

    fun createPayment(offerId: OfferId, address: Address, cardDetails: CardDetails): PaymentId {
        val offer = offerHub.offer(offerId).orThrow()
        return paymentHub.createPayment(offerId, address, cardDetails, offer.details.offer).orThrow()
    }

    fun itemReceived(offerId: OfferId) {
        offerHub.itemReceived(offerId)
    }

    fun stateOf(paymentId: PaymentId): PaymentState {
        return paymentHub.payment(paymentId).orThrow().state
    }

    fun stateOf(offerId: OfferId): OfferState {
        return offerHub.offer(offerId).orThrow().state
    }

    fun stateOf(adId: AdId): AdState {
        return adHub.ad(adId).orThrow().state
    }

    fun paymentSettled(paymentId: PaymentId) {
        paymentHub.settle(paymentId)
    }
}