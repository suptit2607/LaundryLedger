package com.example.laundryledger.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.laundryledger.presentation.theme.GlassWhite

@Composable
fun GlassBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    blurRadius: Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(GlassWhite.copy(alpha = 0.05f))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GlassWhite.copy(alpha = 0.2f),
                        GlassWhite.copy(alpha = 0.0f)
                    )
                ),
                shape = shape
            )
            .blur(radius = blurRadius, edgeTreatment = BlurredEdgeTreatment.Unbounded)
    ) {
        // The background itself is blurred, but Compose `blur` modifier applies to content as well.
        // For true glassmorphism, RenderEffect is typically used, but `blur` can simulate it on API 31+.
        // A simple translucent background with gradient border serves well for "glass" effect here.
    }
    
    // Foreground content (not blurred)
    Box(
        modifier = modifier
            .clip(shape)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GlassWhite.copy(alpha = 0.2f),
                        GlassWhite.copy(alpha = 0.05f)
                    )
                ),
                shape = shape
            )
            .background(GlassWhite.copy(alpha = 0.08f)),
        content = content
    )
}
