package com.example.laundryledger.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LedgerDao {

    @Query("SELECT * FROM ledger_entries ORDER BY timestamp DESC")
    fun getAllEntriesDescending(): Flow<List<LedgerEntry>>

    @Query("""
        SELECT 
            COALESCE(SUM(CASE WHEN type = 'PAYMENT' THEN amount ELSE 0 END), 0) - 
            COALESCE(SUM(CASE WHEN type = 'CHARGE' THEN amount ELSE 0 END), 0) 
        FROM ledger_entries
    """)
    fun getRunningBalance(): Flow<Double>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: LedgerEntry)
}
