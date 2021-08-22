package classified

import classified.domain.adapter.InMemoryAdRepository
import classified.domain.model.*
import classified.domain.port.socket.AdHub
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

    fun createAd(item: AdDetails): AdId {
        return adHub.createAd(item).orThrow()
    }

    fun acceptOffer(offerId: OfferId) {
        TODO("Not yet implemented")
    }

    fun itemMailed(offerId: OfferId) {
        TODO("Not yet implemented")
    }

    fun createOffer(offer: OfferDetails): OfferId {
        TODO("Not yet implemented")
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