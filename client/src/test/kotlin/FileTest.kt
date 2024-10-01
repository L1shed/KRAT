import org.junit.jupiter.api.Test
import utils.FileUtils
import java.io.File

class FileTest {
    @Test
    fun `should download the file from the url`() {
        FileUtils.download("https://raw.githubusercontent.com/KRAT-the-fusion-dev/KRAT/main/README.md", KRAT.cacheDir)
        // check if the file was downloaded
        assert(File("${KRAT.cacheDir.absolutePath}/README.md").exists())
    }
}