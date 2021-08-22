package classified

import classified.domain.model.*
import com.ubertob.pesticide.core.*

class ClassifiedActions : DomainActions<DdtProtocol> {
    override val protocol: DdtProtocol = DomainOnly

    override fun prepare(): DomainSetUp = Ready
    fun usersExistInTheSystem(vararg users: ClassifiedUser) {
        // do something?
    }

    fun createAd(item: ItemDetails): ItemId {
        return ItemId.of(1)
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

    fun stateOf(itemId: ItemId): ItemState {
        TODO("Not yet implemented")
    }
}