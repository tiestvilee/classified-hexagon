package classified

import classified.domain.model.*
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.ubertob.pesticide.core.DdtActorWithContext
import com.ubertob.pesticide.core.StepContext

data class JourneyContext(val adId: AdId? = null, val offerId: OfferId? = null, val paymentId: PaymentId? = null)

class ClassifiedUser(override val name: String) : DdtActorWithContext<ClassifiedActions, JourneyContext>() {
    fun `advertises item #`(item: AdDetails) = step(item.name) { ctx ->
        val itemId = createAd(item)
        ctx.store(JourneyContext(itemId))
    }

    fun `accepts highest offer`() = step() { ctx ->
        val offerId: OfferId = findOffersFor(itemId(ctx)).maxOfWith(
            { a: Offer, b: Offer -> a.details.offer.amount.compareTo(b.details.offer.amount) },
            { it }
        ).id
        acceptOffer(offerId)
        ctx.store(ctx.get().copy(offerId = offerId))
    }

    fun `mails item`() = step() { ctx ->
        itemMailed(offerId(ctx))
    }

    fun `finds item #`(itemName: String) = step(itemName) { ctx ->
        val itemId = findItem(itemName)
        ctx.store(JourneyContext(itemId))
    }

    fun `offers to buy item for #`(offer: Money) = step(offer) { ctx ->
        val offerId = createOffer(OfferDetails(itemId(ctx), offer))
        ctx.store(ctx.get().copy(offerId = offerId))
    }

    fun `authorises payment`(address: Address, cardDetails: CardDetails) = step() { ctx ->
        val paymentId = createPayment(offerId(ctx), address, cardDetails)
        ctx.store(ctx.get().copy(paymentId = paymentId))
    }

    fun `receives item and payment is settled`() = step() { ctx ->
        itemReceived(offerId(ctx))
        assertThat(stateOf(offerId(ctx)), equalTo(OfferState.Completed))
        assertThat(stateOf(itemId(ctx)), equalTo(AdState.Completed))
        assertThat(stateOf(ctx.get().paymentId!!), equalTo(PaymentState.Settled))
    }

}

private fun itemId(ctx: StepContext<JourneyContext>) = ctx.get().adId!!

private fun offerId(ctx: StepContext<JourneyContext>) = ctx.get().offerId!!