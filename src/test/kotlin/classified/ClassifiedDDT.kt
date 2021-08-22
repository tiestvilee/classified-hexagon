package classified

import classified.domain.model.*
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
            buyer.`offers to buy item for #`(Money(90)),
            seller.`accepts offer`(),
            buyer.`authorises payment`(Address("my home"), CardDetails(CardType.Amex)),
            seller.`mails item`(),
            buyer.`receives item and payment is settled`()
        )
    }
}