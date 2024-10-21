import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import kotlin.concurrent.thread

sealed class SystemEvent {
    data class Created(val path: Path) : SystemEvent()
    data class Modified(val path: Path) : SystemEvent()
    data class Deleted(val path: Path) : SystemEvent()
}

class SystemWatcher(rootDirectory: Path, private val recursive: Boolean = false) {
    private val watchService: WatchService = FileSystems.getDefault().newWatchService()
    private val watchKeys = mutableMapOf<WatchKey, Path>()
    val eventHandlers = mutableMapOf<Class<out SystemEvent>, (SystemEvent) -> Unit>()

    init {
        if (recursive) {
            registerAll(rootDirectory)
        } else {
            register(rootDirectory)
        }

        start()
    }

    private fun register(dir: Path) {
        val key = dir.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY
        )
        watchKeys[key] = dir
    }

    private fun registerAll(start: Path) {
        Files.walkFileTree(start, object : SimpleFileVisitor<Path>() {
            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                register(dir)
                return FileVisitResult.CONTINUE
            }
        })
    }

    inline fun <reified T : SystemEvent> on(noinline handler: (T) -> Unit) {
        eventHandlers[T::class.java] = handler as (SystemEvent) -> Unit
    }

    fun start() {
        thread {
            while (true) {
                val key = watchService.take() ?: continue
                val dir = watchKeys[key] ?: continue

                for (event in key.pollEvents()) {
                    val kind = event.kind()
                    val fileName = event.context() as Path
                    val fullPath = dir.resolve(fileName)

                    if (recursive && kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        if (Files.isDirectory(fullPath, LinkOption.NOFOLLOW_LINKS)) {
                            registerAll(fullPath)
                        }
                    }

                    val systemEvent = when (kind) {
                        StandardWatchEventKinds.ENTRY_CREATE -> SystemEvent.Created(fullPath)
                        StandardWatchEventKinds.ENTRY_MODIFY -> SystemEvent.Modified(fullPath)
                        StandardWatchEventKinds.ENTRY_DELETE -> SystemEvent.Deleted(fullPath)
                        else -> null
                    }

                    systemEvent?.let { evt ->
                        eventHandlers[evt::class.java]?.invoke(evt)
                    }
                }

                if (!key.reset()) {
                    watchKeys.remove(key)
                    if (watchKeys.isEmpty()) {
                        break
                    }
                }
            }
        }
    }

    fun stop() {
        watchService.close()
    }
}