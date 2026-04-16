package com.example.laundryledger.ui.screens

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.laundryledger.presentation.theme.AccentGreen
import com.example.laundryledger.presentation.theme.AccentRed
import com.example.laundryledger.ui.components.GlassBox

@Composable
fun BiometricLockScreen(
    onUnlockSuccess: () -> Unit
) {
    val context = LocalContext.current
    var authError by remember { mutableStateOf<String?>(null) }
    var hasPrompted by remember { mutableStateOf(false) }

    val activity = context as? FragmentActivity

    fun authenticate() {
        if (activity == null) {
            authError = "Activity context is missing."
            return
        }

        val executor = ContextCompat.getMainExecutor(context)
        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    authError = "Authentication error: $errString"
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onUnlockSuccess()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    authError = "Authentication failed. Try again."
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Laundry Ledger Logic")
            .setSubtitle("Authenticate to access your ledger")
            .setDeviceCredentialAllowed(true)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    LaunchedEffect(Unit) {
        if (!hasPrompted) {
            hasPrompted = true
            authenticate()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (authError != null) {
            GlassBox(
                modifier = Modifier.padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Access Blocked",
                        style = MaterialTheme.typography.titleLarge,
                        color = AccentRed
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = authError ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { authenticate() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}
