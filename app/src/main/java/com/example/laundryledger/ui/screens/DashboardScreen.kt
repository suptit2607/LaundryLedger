package com.example.laundryledger.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.laundryledger.data.EntryType
import com.example.laundryledger.data.LedgerEntry
import com.example.laundryledger.presentation.LedgerViewModel
import com.example.laundryledger.presentation.theme.AccentGreen
import com.example.laundryledger.presentation.theme.AccentRed
import com.example.laundryledger.ui.components.GlassBox
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: LedgerViewModel = hiltViewModel()
) {
    val history by viewModel.history.collectAsState()
    val balance by viewModel.runningBalance.collectAsState()

    var showCustomSheet by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            
            // Top Section: Running Balance
            GlassBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Running Balance",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val balanceColor = if (balance >= 0) AccentGreen else AccentRed
                    val formatString = if (balance >= 0) "+$%.2f" else "-$%.2f"
                    
                    Text(
                        text = String.format(formatString, kotlin.math.abs(balance)),
                        style = MaterialTheme.typography.displayLarge,
                        color = balanceColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Middle Section: Quick Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { viewModel.logCharge(6, "Quick Log") },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                ) {
                    Text("Quick Log: 6", style = MaterialTheme.typography.bodyLarge)
                }
                
                OutlinedButton(
                    onClick = { showCustomSheet = true },
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Text("Custom", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Past History",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Bottom Section: History List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(history, key = { it.id }) { entry ->
                    HistoryItem(entry = entry)
                }
            }
        }
    }

    if (showCustomSheet) {
        CustomActionBottomSheet(
            onDismiss = { showCustomSheet = false },
            onLogCharge = { clothes, notes -> viewModel.logCharge(clothes, notes) },
            onLogPayment = { amount, notes -> viewModel.logPayment(amount, notes) }
        )
    }
}

@Composable
fun HistoryItem(entry: LedgerEntry) {
    GlassBox(
        modifier = Modifier.fillMaxWidth(),
        blurRadius = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                Text(
                    text = dateFormat.format(Date(entry.timestamp)),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                val typeText = if (entry.type == EntryType.CHARGE) "Charge (${entry.clothCount} clothes)" else "Payment"
                Text(
                    text = typeText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (!entry.notes.isNullOrEmpty()) {
                    Text(
                        text = entry.notes,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            val amountColor = if (entry.type == EntryType.PAYMENT) AccentGreen else AccentRed
            val amountPrefix = if (entry.type == EntryType.PAYMENT) "+" else "-"
            Text(
                text = "$amountPrefix$${String.format("%.2f", entry.amount)}",
                style = MaterialTheme.typography.titleLarge,
                color = amountColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomActionBottomSheet(
    onDismiss: () -> Unit,
    onLogCharge: (Int, String) -> Unit,
    onLogPayment: (Double, String) -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    var selectedTab by remember { mutableStateOf(0) } // 0 = Charge, 1 = Payment
    
    var inputValue by remember { mutableStateOf("") }
    var notesValue by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(bottom = 32.dp)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0; inputValue = "" }) {
                    Text("Charge (Clothes)", modifier = Modifier.padding(16.dp))
                }
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1; inputValue = "" }) {
                    Text("Payment ($)", modifier = Modifier.padding(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = inputValue,
                onValueChange = { inputValue = it },
                label = { Text(if (selectedTab == 0) "Number of clothes" else "Payment Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = notesValue,
                onValueChange = { notesValue = it },
                label = { Text("Notes (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { 
                    if (selectedTab == 0) {
                        val clothes = inputValue.toIntOrNull() ?: 0
                        if (clothes > 0) onLogCharge(clothes, notesValue)
                    } else {
                        val amount = inputValue.toDoubleOrNull() ?: 0.0
                        if (amount > 0) onLogPayment(amount, notesValue)
                    }
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(if (selectedTab == 0) "Log Charge" else "Log Payment")
            }
        }
    }
}
