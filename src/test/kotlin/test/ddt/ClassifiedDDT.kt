package test.ddt

import classified.domain.model.AdDetails
import classified.domain.payment.model.Address
import classified.domain.payment.model.CardDetails
import classified.domain.payment.model.CardType
import classified.domain.payment.model.Money
import com.ubertob.pesticide.core.DDT
import com.ubertob.pesticide.core.DomainDrivenTest

class ClassifiedDDT : DomainDrivenTest<ClassifiedActions>(setOf(ClassifiedActions())) {
    private val buyer = ClassifiedUser("Bob")
    private val seller = ClassifiedUser("Sally")

    @DDT
    fun `Seller advertises item and buyer purchases it`() = ddtScenario {
        setUp {
            usersExistInTheSystem(buyer, seller)
        }.thenPlay(
            seller.`advertises item #`(AdDetails("Baseball Card", Money(100))),
            buyer.`finds item #`("Baseball Card"),
            buyer.`offers to buy item for #`(Money(90)),
            seller.`accepts highest offer`(),
            buyer.`authorises payment`(Address("my home"), CardDetails(CardType.Amex)),
            seller.`mails item`(),
            buyer.`receives item`()
        )
    }
}