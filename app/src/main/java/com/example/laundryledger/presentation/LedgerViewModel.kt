package com.example.laundryledger.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laundryledger.data.EntryType
import com.example.laundryledger.data.LedgerEntry
import com.example.laundryledger.domain.LedgerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LedgerViewModel @Inject constructor(
    private val repository: LedgerRepository
) : ViewModel() {

    companion object {
        const val COST_PER_CLOTH = 6.0
    }

    val history: StateFlow<List<LedgerEntry>> = repository.getHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val runningBalance: StateFlow<Double> = repository.getRunningBalance()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun logCharge(clothCount: Int, notes: String? = null) {
        viewModelScope.launch {
            val amount = clothCount * COST_PER_CLOTH
            val entry = LedgerEntry(
                type = EntryType.CHARGE,
                amount = amount,
                clothCount = clothCount,
                timestamp = System.currentTimeMillis(),
                notes = notes
            )
            repository.insertEntry(entry)
        }
    }

    fun logPayment(amount: Double, notes: String? = null) {
        viewModelScope.launch {
            val entry = LedgerEntry(
                type = EntryType.PAYMENT,
                amount = amount,
                clothCount = null,
                timestamp = System.currentTimeMillis(),
                notes = notes
            )
            repository.insertEntry(entry)
        }
    }
}
