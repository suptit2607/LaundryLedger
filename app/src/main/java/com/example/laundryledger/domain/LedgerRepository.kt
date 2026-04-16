package com.example.laundryledger.domain

import com.example.laundryledger.data.LedgerDao
import com.example.laundryledger.data.LedgerEntry
import kotlinx.coroutines.flow.Flow

class LedgerRepository(
    private val dao: LedgerDao
) {
    fun getHistory(): Flow<List<LedgerEntry>> = dao.getAllEntriesDescending()
    
    fun getRunningBalance(): Flow<Double> = dao.getRunningBalance()
    
    suspend fun insertEntry(entry: LedgerEntry) {
        dao.insertEntry(entry)
    }
}
