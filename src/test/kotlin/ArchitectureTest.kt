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
    private val basePackage = "classified"
    private val domainHub = "hub"
    private val domainModel = "model"
    private val adapter = "adapter"
    private val portSocket = "port.socket"
    private val portPlug = "port.plug"
    private val deployable = "deployable"

    @ArchTest
    val `there are no package cycles` =
        SlicesRuleDefinition.slices()
            .matching("$basePackage.(**)..")
            .should()
            .beFreeOfCycles()

    @ArchTest
    val `the domain model does not have outgoing dependencies` =
        ArchRuleDefinition.noClasses()
            .that()
            .resideInAPackage("$domainModel..")
            .should()
            .accessClassesThat()
            .resideInAnyPackage(
                "$domainHub..",
                "$adapter..",
                "$portPlug..",
                "$portSocket..",
            )

    @Test
    fun `only adapters can implement plugs`() {
        val results = ClassFileImporter().importClasspath()
            .filter { it.name.contains(portPlug) }
            .filter { it.isInterface }
            .flatMap { plug ->
                plug.allSubclasses.flatMap { implementer ->
                    if (implementer.packageName.contains(adapter)) {
                        emptyList()
                    } else {
                        listOf("${implementer.name} implements \n${plug.name} but isn't in\n$adapter")
                    }
                }
            }
        assertThat(results, isEmpty)
    }

    @Test
    fun `only hubs can implement sockets`() {
        val results = ClassFileImporter().importClasspath()
            .filter { it.name.contains(portSocket) }
            .filter { it.isInterface }
            .flatMap { socket ->
                socket.allSubclasses.flatMap { implementer ->
                    if (implementer.packageName.contains(domainHub)) {
                        emptyList()
                    } else {
                        listOf("${implementer.name} implements ${socket.name} but isn't in $domainHub")
                    }
                }
            }
        assertThat(results, isEmpty)
    }

    @Test
    fun `only deployables can create adapters`() {
        val results = ClassFileImporter().importClasspath()
            .filter { it.name.contains(adapter) }
            .flatMap { adapter ->
                adapter.constructorCallsToSelf.flatMap { implementer ->
                    val packageName = implementer.originOwner.packageName
                    if (packageName.contains(deployable) || packageName.contains(this.adapter)) {
                        emptyList()
                    } else {
                        listOf("${implementer.originOwner.name} constructs ${adapter.name} but isn't in $deployable")
                    }
                }
            }.toSet()
        assertThat(results, isEmpty)
    }

    @Test
    fun `only deployables can create hubs`() {
        val results = ClassFileImporter().importClasspath()
            .filter { it.name.contains(domainHub) }
            .flatMap { hub ->
                hub.constructorCallsToSelf.flatMap { implementer ->
                    val packageName = implementer.originOwner.packageName
                    if (packageName.contains(deployable) || packageName.contains(domainHub)) {
                        emptyList()
                    } else {
                        listOf("${implementer.originOwner.name} constructs ${hub.name} but isn't in $deployable")
                    }
                }
            }.toSet()
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