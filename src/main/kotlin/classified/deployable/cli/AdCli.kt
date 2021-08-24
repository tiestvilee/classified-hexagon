package classified.deployable.cli

import classified.domain.ad.adapter.FileAdRepository
import classified.domain.ad.hub.AdHub
import classified.domain.model.AdDetails
import classified.domain.payment.model.Money
import java.io.File

fun main() {
    val adRepo = FileAdRepository(File("./ads.db"))
    val adHub = AdHub(adRepo)

    println("Ads: " + adRepo.ads())
    adHub.createAd(AdDetails("blah", Money(100, 1)))
    adHub.createAd(AdDetails("blue", Money(200, 2)))
    println("Ads: " + adRepo.ads())
}