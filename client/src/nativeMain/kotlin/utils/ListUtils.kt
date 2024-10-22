package utils

object ListUtils {
    fun <T> List<T>.removeDuplicates(): List<T> {
        return this.toSet().toList()
    }
}