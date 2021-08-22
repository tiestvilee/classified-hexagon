package classified

import classified.domain.adapter.InMemoryAdRepository
import classified.domain.adapter.InMemoryOfferRepository
import classified.domain.model.*
import classified.domain.port.socket.AdHub
import classified.domain.port.socket.OfferHub
import com.ubertob.pesticide.core.*
import dev.forkhandles.result4k.orThrow

class ClassifiedActions : DomainActions<DdtProtocol> {
    override val protocol: DdtProtocol = DomainOnly

    override fun prepare(): DomainSetUp = Ready
    fun usersExistInTheSystem(vararg users: ClassifiedUser) {
        // do something?
    }

    val adHub: AdHub = classified.domain.hub.AdHub(
        InMemoryAdRepository()
    )

    val offerHub: OfferHub = classified.domain.hub.OfferHub(
        InMemoryOfferRepository()
    )

    fun createAd(item: AdDetails): AdId {
        return adHub.createAd(item).orThrow()
    }

    fun acceptOffer(offerId: OfferId) {
        TODO("Not yet implemented")
    }

    fun itemMailed(offerId: OfferId) {
        TODO("Not yet implemented")
    }

    fun findItem(itemName: String): AdId {
        return adHub.findAdByName(itemName).orThrow().id
    }

    fun createOffer(offer: OfferDetails): OfferId {
        return offerHub.createOffer(offer).orThrow()
    }

    fun createPayment(offerId: OfferId, address: Address, cardDetails: CardDetails): PaymentId {
        TODO("Not yet implemented")
    }

    fun itemReceived(offerId: OfferId) {
        TODO("Not yet implemented")
    }

    fun stateOf(paymentId: PaymentId): PaymentState {
        TODO("Not yet implemented")
    }

    fun stateOf(offerId: OfferId): OfferState {
        TODO("Not yet implemented")
    }

    fun stateOf(adId: AdId): AdState {
        TODO("Not yet implemented")
    }
}