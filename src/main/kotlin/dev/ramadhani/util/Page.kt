package dev.ramadhani.util

interface Identifiable {
    val id: String
}

data class Page<T: Identifiable, U: Comparable<*>>(val result: List<T>, val size: UInt, val hasNextPage: Boolean = false, val cursor: U) {
    val count: UInt = result.size.toUInt()
}