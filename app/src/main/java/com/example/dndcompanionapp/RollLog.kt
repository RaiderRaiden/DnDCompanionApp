package com.example.dndcompanionapp

object RollLog {
    private val entries = mutableListOf<RollEntry>()

    fun addEntry(total: Int, breakdown: String) {
        entries.add(RollEntry(total, breakdown))
    }

    fun getEntries(): List<RollEntry> = entries.toList()

    data class RollEntry(val total: Int, val breakdown: String)
}
