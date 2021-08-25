package test.ddt

import classified.domain.model.*
import classified.domain.payment.model.Address
import classified.domain.payment.model.CardDetails
import classified.domain.payment.model.PaymentId
import classified.domain.payment.model.PaymentState
import com.ubertob.pesticide.core.DdtProtocol
import com.ubertob.pesticide.core.DomainActions

interface ClassifiedActions : DomainActions<DdtProtocol> {
    fun usersExistInTheSystem(vararg users: ClassifiedUser)
    fun createAd(item: AdDetails): AdId
    fun findOffersFor(adId: AdId): List<Offer>
    fun acceptOffer(offerId: OfferId)
    fun itemMailed(offerId: OfferId)
    fun findItem(itemName: String): AdId
    fun createOffer(offer: OfferDetails): OfferId
    fun createPayment(offerId: OfferId, address: Address, cardDetails: CardDetails): PaymentId
    fun itemReceived(offerId: OfferId)
    fun stateOf(paymentId: PaymentId): PaymentState
    fun stateOf(offerId: OfferId): OfferState
    fun stateOf(adId: AdId): AdState
    fun paymentSettled(paymentId: PaymentId)
}