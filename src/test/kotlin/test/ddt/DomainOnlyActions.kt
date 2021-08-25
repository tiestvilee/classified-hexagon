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
import com.ubertob.pesticide.core.DdtProtocol
import com.ubertob.pesticide.core.DomainOnly
import com.ubertob.pesticide.core.DomainSetUp
import com.ubertob.pesticide.core.Ready
import dev.forkhandles.result4k.orThrow

class DomainOnlyActions : ClassifiedActions {
    override val protocol: DdtProtocol = DomainOnly

    override fun prepare(): DomainSetUp = Ready
    override fun usersExistInTheSystem(vararg users: ClassifiedUser) {
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

    override fun createAd(item: AdDetails): AdId {
        return adHub.createAd(item).orThrow()
    }

    override fun findOffersFor(adId: AdId): List<Offer> {
        return offerHub.offersFor(adId).orThrow()
    }

    override fun acceptOffer(offerId: OfferId) {
        offerHub.acceptOffer(offerId).orThrow()
    }

    override fun itemMailed(offerId: OfferId) {
        offerHub.itemMailed(offerId).orThrow()
    }

    override fun findItem(itemName: String): AdId {
        return adHub.findAdByName(itemName).orThrow().id
    }

    override fun createOffer(offer: OfferDetails): OfferId {
        return offerHub.createOffer(offer).orThrow()
    }

    override fun createPayment(offerId: OfferId, address: Address, cardDetails: CardDetails): PaymentId {
        val offer = offerHub.offer(offerId).orThrow()
        return paymentHub.createPayment(offerId, address, cardDetails, offer.details.offer).orThrow()
    }

    override fun itemReceived(offerId: OfferId) {
        offerHub.itemReceived(offerId)
    }

    override fun stateOf(paymentId: PaymentId): PaymentState {
        return paymentHub.payment(paymentId).orThrow().state
    }

    override fun stateOf(offerId: OfferId): OfferState {
        return offerHub.offer(offerId).orThrow().state
    }

    override fun stateOf(adId: AdId): AdState {
        return adHub.ad(adId).orThrow().state
    }

    override fun paymentSettled(paymentId: PaymentId) {
        paymentHub.settle(paymentId)
    }
}