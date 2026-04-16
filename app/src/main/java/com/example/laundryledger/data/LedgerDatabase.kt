package com.example.laundryledger.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LedgerEntry::class], version = 1, exportSchema = false)
abstract class LedgerDatabase : RoomDatabase() {
    abstract val ledgerDao: LedgerDao
    
    companion object {
        const val DATABASE_NAME = "laundry_ledger_db"
    }
}
