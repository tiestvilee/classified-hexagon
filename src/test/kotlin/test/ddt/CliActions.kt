package test.ddt

import classified.deployable.cli.ClassifiedCli
import classified.domain.ad.adapter.dependency.InMemoryAdRepository
import classified.domain.ad.adapter.service.CliAdHubClient
import classified.domain.ad.hub.AdHub
import classified.domain.model.*
import classified.domain.offer.adapter.dependency.InMemoryOfferRepository
import classified.domain.offer.adapter.service.toOffer
import classified.domain.offer.adapter.service.toOfferId
import classified.domain.offer.adapter.service.toOffers
import classified.domain.offer.hub.OfferHub
import classified.domain.payment.adapter.dependency.InMemoryPaymentRepository
import classified.domain.payment.adapter.dependency.PaymentProviderFake
import classified.domain.payment.adapter.service.toPayment
import classified.domain.payment.adapter.service.toPaymentId
import classified.domain.payment.hub.PaymentHub
import classified.domain.payment.model.Address
import classified.domain.payment.model.CardDetails
import classified.domain.payment.model.PaymentId
import classified.domain.payment.model.PaymentState
import com.ubertob.pesticide.core.DdtProtocol
import com.ubertob.pesticide.core.DomainOnly
import com.ubertob.pesticide.core.DomainSetUp
import com.ubertob.pesticide.core.Ready
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

    private val adClient = CliAdHubClient { command ->
        cli.process("ad $command")
    }

    override fun createAd(item: AdDetails): AdId {
        return adClient.createAd(item).orThrow()
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
        return adClient.findAdByName(itemName).orThrow().id
    }

    override fun createOffer(offer: OfferDetails): OfferId {
        return cli.process("offer create ${offer.adId.value} ${offer.offer.amount}").toOfferId().orThrow()
    }

    override fun createPayment(offerId: OfferId, address: Address, cardDetails: CardDetails): PaymentId {
        val offer = cli.process("offer find -id ${offerId.value}").toOffer().orThrow()
        return cli.process("payment create ${offerId.value} ${address.location} ${cardDetails.cardType} ${offer.details.offer.amount}")
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
        return adClient.ad(adId).orThrow().state
    }

    override fun paymentSettled(paymentId: PaymentId) {
        cli.process("payment settled ${paymentId.value}")
    }
}

