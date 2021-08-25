package test.ddt

import classified.domain.ad.adapter.InMemoryAdRepository
import classified.domain.ad.port.socket.AdHub
import classified.domain.model.*
import classified.domain.offer.adapter.InMemoryOfferRepository
import classified.domain.offer.port.socket.OfferHub
import classified.domain.payment.adapter.InMemoryPaymentRepository
import classified.domain.payment.adapter.PaymentProviderFake
import classified.domain.payment.model.Address
import classified.domain.payment.model.CardDetails
import classified.domain.payment.model.PaymentId
import classified.domain.payment.model.PaymentState
import classified.domain.payment.port.socket.PaymentHub
import com.ubertob.pesticide.core.*
import dev.forkhandles.result4k.orThrow

class ClassifiedActions : DomainActions<DdtProtocol> {
    override val protocol: DdtProtocol = DomainOnly

    override fun prepare(): DomainSetUp = Ready
    fun usersExistInTheSystem(vararg users: ClassifiedUser) {
        // do something?
    }

    private val adHub: AdHub = classified.domain.ad.hub.AdHub(
        InMemoryAdRepository()
    )

    private val offerHub: OfferHub = classified.domain.offer.hub.OfferHub(
        InMemoryOfferRepository()
    )

    private val paymentHub: PaymentHub = classified.domain.payment.hub.PaymentHub(
        InMemoryPaymentRepository(),
        PaymentProviderFake()
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