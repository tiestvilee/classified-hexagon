package classified

import classified.domain.model.*
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.ubertob.pesticide.core.DdtActorWithContext
import com.ubertob.pesticide.core.StepContext

data class JourneyContext(val itemId: ItemId? = null, val offerId: OfferId? = null, val paymentId: PaymentId? = null)

class ClassifiedUser(override val name: String) : DdtActorWithContext<ClassifiedActions, JourneyContext>() {
    fun `advertises item #`(item: ItemDetails) = step(item.name) { ctx ->
        val itemId = createAd(item)
        ctx.store(JourneyContext(itemId))
    }

    fun `accepts offer`() = step() { ctx ->
        acceptOffer(offerId(ctx))
    }

    fun `mails item`() = step() { ctx ->
        itemMailed(offerId(ctx))
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
        assertThat(stateOf(ctx.get().paymentId!!), equalTo(PaymentState.Settled))
        assertThat(stateOf(offerId(ctx)), equalTo(OfferState.Completed))
        assertThat(stateOf(itemId(ctx)), equalTo(ItemState.Completed))
    }

}

private fun ClassifiedUser.itemId(ctx: StepContext<JourneyContext>) = ctx.get().itemId!!

private fun ClassifiedUser.offerId(ctx: StepContext<JourneyContext>) = ctx.get().offerId!!