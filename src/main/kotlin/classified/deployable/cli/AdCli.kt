package classified.deployable.cli

import classified.domain.ad.adapter.dependency.FileAdRepository
import classified.domain.ad.adapter.service.CliAdHubServer
import classified.domain.ad.hub.AdHub
import java.io.File

fun main(args: Array<String>) {
    println(
        CliAdHubServer(AdHub(FileAdRepository(File("./ads.db"))))
            .process()(args.joinToString(" "))
    )
}