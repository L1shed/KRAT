import org.junit.jupiter.api.Test
import utils.FileUtils
import java.io.File

class FileTest {
    @Test
    fun `should download the file from the url`() {
        FileUtils.download("https://github.com/xia-mc/Raven-XD/releases/download/v2.7.0/raven-XD.jar", KRAT.cacheDir)
        // check if the file was downloaded
        assert(File("${KRAT.cacheDir.absolutePath}/raven-XD.jar").exists())
    }
}