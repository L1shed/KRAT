fun main() {
    val start = System.nanoTime()
    repeat(1_000_000) {
        if (it % 100_000 == 0)
            println("Hello my ky")
    }
    println("Took ${System.nanoTime() - start} ms")
}