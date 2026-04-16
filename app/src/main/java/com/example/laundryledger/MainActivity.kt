package com.example.laundryledger

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.example.laundryledger.presentation.theme.LaundryLedgerTheme
import com.example.laundryledger.ui.screens.BiometricLockScreen
import com.example.laundryledger.ui.screens.DashboardScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaundryLedgerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isUnlocked by remember { mutableStateOf(false) }
                    
                    if (isUnlocked) {
                        DashboardScreen()
                    } else {
                        BiometricLockScreen(
                            onUnlockSuccess = { isUnlocked = true }
                        )
                    }
                }
            }
        }
    }
}
