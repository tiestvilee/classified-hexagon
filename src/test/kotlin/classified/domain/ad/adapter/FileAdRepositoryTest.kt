package classified.domain.ad.adapter

import org.junit.jupiter.api.Test
import java.io.File


class FileAdRepositoryTest {
    @Test
    fun `just construct FileAdRepo`() {
        FileAdRepository(File("name"))
    }
}