package com.lukaslechner.coroutineusecasesonandroid.playground.flow.concurrency

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow

suspend fun main() = coroutineScope {

    val fastFlow = flow {
        repeat(10) {
            println("Produced $it")
            emit(it)
        }
    }

    fastFlow
        .buffer(1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        .collect {
            delay(100) // slow consumer
            println("Consumed $it")
        }

    val flow = flow {
        repeat(5) {
            val pancakeIndex = it + 1
            println("Emitter:    Start Cooking Pancake $pancakeIndex")
            delay(100)
            println("Emitter:    Pancake $pancakeIndex ready!")
            emit(pancakeIndex)
        }
    }.buffer(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    flow.collect {
        println("Collector:  Start eating pancake $it")
        delay(300)
        println("Collector:  Finished eating pancake $it")
    }
}