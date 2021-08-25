package test.ddt

import classified.deployable.cli.AdCliParserError
import classified.deployable.cli.ClassifiedCli
import classified.domain.ad.adapter.InMemoryAdRepository
import classified.domain.ad.adapter.toAd
import classified.domain.ad.adapter.toAdId
import classified.domain.ad.hub.AdHub
import classified.domain.model.*
import classified.domain.offer.adapter.InMemoryOfferRepository
import classified.domain.offer.hub.OfferHub
import classified.domain.payment.adapter.InMemoryPaymentRepository
import classified.domain.payment.adapter.PaymentProviderFake
import classified.domain.payment.hub.PaymentHub
import classified.domain.payment.model.*
import com.ubertob.pesticide.core.DdtProtocol
import com.ubertob.pesticide.core.DomainOnly
import com.ubertob.pesticide.core.DomainSetUp
import com.ubertob.pesticide.core.Ready
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.orThrow

class CliActions : ClassifiedActions {
    override val protocol: DdtProtocol = DomainOnly

    override fun prepare(): DomainSetUp = Ready
    override fun usersExistInTheSystem(vararg users: ClassifiedUser) {
        // do something?
    }

    private val cli = ClassifiedCli(
        AdHub(
            InMemoryAdRepository()
        ), OfferHub(
            InMemoryOfferRepository()
        ), PaymentHub(
            InMemoryPaymentRepository(),
            PaymentProviderFake()
        )
    )

    override fun createAd(item: AdDetails): AdId {
        return cli.process("ad create '${item.name}' ${item.askingPrice}").toAdId().orThrow()
    }

    override fun findOffersFor(adId: AdId): List<Offer> {
        return cli.process("offer find -ad ${adId.value}").toOffers().orThrow()
    }

    override fun acceptOffer(offerId: OfferId) {
        cli.process("offer accept ${offerId.value}")
    }

    override fun itemMailed(offerId: OfferId) {
        cli.process("offer mailed ${offerId.value}")
    }

    override fun findItem(itemName: String): AdId {
        return cli.process("ad find -name $itemName").toAd().orThrow().id
    }

    override fun createOffer(offer: OfferDetails): OfferId {
        return cli.process("offer create ${offer.adId.value} ${offer.offer.amount}").toOfferId().orThrow()
    }

    override fun createPayment(offerId: OfferId, address: Address, cardDetails: CardDetails): PaymentId {
        val offer = cli.process("offer find -id ${offerId.value}").toOffer().orThrow()
        return cli.process("payment create ${offerId.value} ${address.location}, ${cardDetails.cardType}, ${offer.details.offer.amount}")
            .toPaymentId().orThrow()
    }

    override fun itemReceived(offerId: OfferId) {
        cli.process("offer received ${offerId.value}")
    }

    override fun stateOf(paymentId: PaymentId): PaymentState {
        return cli.process("payment find -id ${paymentId.value}").toPayment().orThrow().state
    }

    override fun stateOf(offerId: OfferId): OfferState {
        return cli.process("offer find -id ${offerId.value}").toOffer().orThrow().state
    }

    override fun stateOf(adId: AdId): AdState {
        return cli.process("ad find -id ${adId.value}").toAd().orThrow().state
    }

    override fun paymentSettled(paymentId: PaymentId) {
        cli.process("payment settled ${paymentId.value}")
    }
}


private fun String.toOfferId(): Result<OfferId, AdCliParserError> {
    return Success(OfferId.parse(this))
}

private fun String.toOffer(): Result<Offer, AdCliParserError> {
    TODO("Not yet implemented")
}

private fun String.toOffers(): Result<List<Offer>, AdCliParserError> {
    TODO("Not yet implemented")
}

private fun String.toPaymentId(): Result<PaymentId, AdCliParserError> {
    return Success(PaymentId.parse(this))
}

private fun String.toPayment(): Result<Payment, AdCliParserError> {
    TODO("Not yet implemented")
}

