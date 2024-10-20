import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import utils.FileUtils
import java.io.File

class FileDownloadTest {
    // May be renamed to FileTest to include the file run test
    @Test
    @Disabled
    fun `should download the file from the url`() {
        val file = File("${KRAT.cacheDir.absolutePath}/raven-XD.jar")
        if (file.exists())
            file.delete()

        FileUtils.download("https://github.com/xia-mc/Raven-XD/releases/download/v2.7.0/raven-XD.jar", KRAT.cacheDir)

        // check if the file was downloaded
        assert(file.exists())

        // cleanup
        file.delete()
    }
}