package com.ninzywangy.editor.render

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import kotlin.math.cos
import kotlin.math.sin

/**
 * Lightweight rendering engine for the editor preview.
 * This is not a full production video renderer, but it provides a real Canvas-based
 * visual output for the preview window and demonstrates how keyframe-driven motion
 * can be mapped onto a paint pipeline.
 */
class RenderEngine {
    fun sampleFrame(frame: Int): Float {
        return (frame % 240) / 240f
    }
}

@Composable
fun RenderPreview(
    modifier: Modifier = Modifier,
    frame: Int,
    layerName: String?
) {
    val engine = rememberRenderEngine()
    val progress = engine.sampleFrame(frame)
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Background gradient
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF0B1020),
                    Color(0xFF111827),
                    Color(0xFF8B5CF6)
                )
            ),
            size = size
        )

        // Animated bloom glow
        val glowX = width * (0.5f + sin(frame * 0.13f) * 0.18f)
        val glowY = height * (0.52f + cos(frame * 0.09f) * 0.12f)
        drawCircle(
            color = Color(0xFF8B5CF6).copy(alpha = 0.35f),
            radius = 180f + sin(frame * 0.2f) * 24f,
            center = Offset(glowX, glowY)
        )

        // Animated frame or shape layer
        val boxLeft = width * 0.18f
        val boxTop = height * 0.28f
        val boxWidth = width * 0.64f
        val boxHeight = height * 0.38f
        val boxRadius = 30f

        drawRoundRect(
            color = Color(0xFF1D4ED8).copy(alpha = 0.75f),
            topLeft = Offset(boxLeft, boxTop),
            size = androidx.compose.ui.geometry.Size(boxWidth, boxHeight),
            cornerRadius = CornerRadius(boxRadius, boxRadius),
            style = Stroke(width = 1.5f)
        )

        // Decoration lines based on frame motion
        val lineAlpha = 0.6f + sin(frame * 0.15f) * 0.25f
        for (i in 0..6) {
            val offsetY = boxTop + 30f + i * 40f
            drawRoundRect(
                color = Color(0xFFD1D5DB).copy(alpha = lineAlpha),
                topLeft = Offset(boxLeft + 30f, offsetY),
                size = androidx.compose.ui.geometry.Size(boxWidth - 60f, 10f),
                cornerRadius = CornerRadius(7f, 7f)
            )
        }

        // Dummy title text
        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = 68f
                isFakeBoldText = true
                isAntiAlias = true
            }

            val message = layerName ?: "Preview"
            canvas.nativeCanvas.drawText(message, width * 0.5f, height * 0.52f, paint)

            val subtitle = "Frame ${frame.toString().padStart(3, '0')} • ${"%.2f".format(progress)}s"
            val smallPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.parseColor("#C7D2FE")
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = 22f
                isAntiAlias = true
            }
            canvas.nativeCanvas.drawText(subtitle, width * 0.5f, height * 0.58f, smallPaint)
        }

        // Animated progress ribbon
        drawRoundRect(
            color = Color(0xFF8B5CF6),
            topLeft = Offset(40f, height - 66f),
            size = androidx.compose.ui.geometry.Size((width - 80f) * progress, 24f),
            cornerRadius = CornerRadius(12f, 12f)
        )

        drawRoundRect(
            color = Color.White.copy(alpha = 0.12f),
            topLeft = Offset(40f, height - 66f),
            size = androidx.compose.ui.geometry.Size(width - 80f, 24f),
            cornerRadius = CornerRadius(12f, 12f),
            style = Stroke(width = 1f)
        )
    }
}

@Composable
private fun rememberRenderEngine(): RenderEngine {
    return androidx.compose.runtime.remember { RenderEngine() }
}
