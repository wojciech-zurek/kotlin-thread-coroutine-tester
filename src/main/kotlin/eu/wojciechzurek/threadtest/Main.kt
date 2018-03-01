package eu.wojciechzurek.threadtest

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread


fun main(args: Array<String>) {

    println("==== Classic Threads vs Coroutines ====")
    println()
    println("Available processors: [${Runtime.getRuntime().availableProcessors()}]")
    println()

    test(::thread)
    test(::launch)
    test(::async)
}


fun test(body: (total: Int, counter: AtomicInteger) -> Unit) {

    println("Heap size MB: [${Runtime.getRuntime().totalMemory().shr(20)}]")

    val total = 1_000_000
    val counter = AtomicInteger()
    val start = System.currentTimeMillis()

    try {
        body(total, counter)
    } catch (e: OutOfMemoryError) {
        errorln(e)
        summary(counter, start)

        runBlocking {
            println("Sleeping 2 sec...")
            println()
            delay(2_000)
        }
        return
    }
    summary(counter, start)
}

fun thread(total: Int, counter: AtomicInteger) {
    println("Testing threads...")
    List(total) {
        thread(start = true) {
            try {
                counter.incrementAndGet()
                Thread.sleep(1_000)
            } catch (e: InterruptedException) {
                errorln(e)
            }
        }
    }
}

fun launch(total: Int, counter: AtomicInteger) {
    println("Testing coroutine with launch ...")
    runBlocking {
        List(total) {
            launch {
                counter.incrementAndGet()
                delay(1_000)
            }
        }.forEach {
            it.join()
        }
    }
}

fun async(total: Int, counter: AtomicInteger) {
    println("Testing coroutine with async ...")
    runBlocking {
        val deferred = (1..total).map {
            async {
                delay(1_000)
                1
            }
        }
        counter.addAndGet(deferred.sumBy { it.await() })
    }
}

fun errorln(any: Any) {
    System.err.println(any)
}

fun summary(counter: AtomicInteger, start: Long) {
    println("Total time: [${(System.currentTimeMillis() - start) / 1000f}]")
    println("Total spawned threads/coroutine: [${counter.get()}]")
    println()
}