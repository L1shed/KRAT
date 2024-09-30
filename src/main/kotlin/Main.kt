import java.nio.file.Paths

fun main() {
    val watcher = SystemWatcher(Paths.get("C:/Users/drsna/IdeaProjects/SystemWatcher"), recursive = true)

    watcher.on<SystemEvent.Created> { println("Created: ${it.path}") }
    watcher.on<SystemEvent.Modified> { println("Modified: ${it.path}") }
    watcher.on<SystemEvent.Deleted> { println("Deleted: ${it.path}") }

    Thread.sleep(10000000)

    watcher.stop()
}