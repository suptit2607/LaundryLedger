package com.example.laundryledger.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class EntryType {
    CHARGE,
    PAYMENT
}

@Entity(tableName = "ledger_entries")
data class LedgerEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: EntryType,
    val amount: Double,
    val clothCount: Int? = null,
    val timestamp: Long,
    val notes: String? = null
)
