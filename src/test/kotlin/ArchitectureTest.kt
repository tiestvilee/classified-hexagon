import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isEmpty
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition
import org.junit.jupiter.api.Test

@Suppress("HasPlatformType", "PropertyName")
@AnalyzeClasses(packagesOf = [ArchitectureTest::class], importOptions = [ImportOption.DoNotIncludeTests::class])
class ArchitectureTest {
    private val BASE_PACKAGE = "classified"
    private val DOMAIN = "$BASE_PACKAGE.domain"
    private val DOMAIN_HUB = "$DOMAIN.hub"
    private val DOMAIN_MODEL = "$DOMAIN.model"
    private val DOMAIN_ADAPTER = "$DOMAIN.adapter"
    private val DOMAIN_PORT = "$DOMAIN.port"
    private val PORT_SOCKET = "$DOMAIN_PORT.socket"
    private val PORT_PLUG = "$DOMAIN_PORT.plug"

    @ArchTest
    val `there are no package cycles` =
        SlicesRuleDefinition.slices()
            .matching("$BASE_PACKAGE.(**)..")
            .should()
            .beFreeOfCycles()

    @ArchTest
    val `the domain model does not have outgoing dependencies` =
        ArchRuleDefinition.noClasses()
            .that()
            .resideInAPackage("$DOMAIN_MODEL..")
            .should()
            .accessClassesThat()
            .resideInAnyPackage(
                "$DOMAIN_HUB..",
                "$DOMAIN_ADAPTER..",
                "$PORT_PLUG..",
                "$PORT_SOCKET..",
            )

    @Test
    fun `only adapters can implement plugs`() {
        val results = ClassFileImporter().importClasspath()
            .filter { it.name.contains(PORT_PLUG) }
            .filter { it.isInterface }
            .flatMap { plug ->
                plug.allSubclasses.flatMap { implementer ->
                    if (implementer.packageName.contains(DOMAIN_ADAPTER)) {
                        emptyList()
                    } else {
                        listOf("${implementer.name} implements \n${plug.name} but isn't in\n$DOMAIN_ADAPTER")
                    }
                }
            }
        assertThat(results, isEmpty)
    }

    @Test
    fun `only hubs can implement sockets`() {
        val results = ClassFileImporter().importClasspath()
            .filter { it.name.contains(PORT_SOCKET) }
            .filter { it.isInterface }
            .flatMap { socket ->
                socket.allSubclasses.flatMap { implementer ->
                    if (implementer.packageName.contains(DOMAIN_HUB)) {
                        emptyList()
                    } else {
                        listOf("${implementer.name} implements ${socket.name} but isn't in $DOMAIN_HUB")
                    }
                }
            }
        assertThat(results, isEmpty)
    }


    /*
        @ArchTest
    val `the domain model does not have outgoing dependencies` =
        noClasses()
            .that()
            .resideInAPackage("$DOMAIN_MODEL_PACKAGE..")
            .should()
            .accessClassesThat()
            .resideInAnyPackage(
                "$SERVICES_PACKAGE..",
                "$APPLICATION_PACKAGE..",
                "$ADAPTERS_PACKAGE.."
            )

     */
}