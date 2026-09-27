package com.ninzywangy.editor.export

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.ninzywangy.editor.timeline.TimelineComposition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Export preset with predefined settings.
 */
data class ExportPreset(
    val name: String,
    val width: Int,
    val height: Int,
    val fps: Int = 30,
    val bitrate: String = "8000k",
    val format: String = "mp4",
    val codec: String = "h264"
)

/**
 * Export configuration for video rendering.
 */
data class ExportConfig(
    val outputPath: String,
    val width: Int = 1080,
    val height: Int = 1920,
    val fps: Int = 30,
    val bitrate: String = "8000k",
    val format: String = "mp4",
    val codec: String = "h264",
    val includeAudio: Boolean = true,
    val quality: ExportQuality = ExportQuality.MEDIUM
)

enum class ExportQuality(val bitrate: String, val label: String) {
    LOW("3000k", "Low (3 Mbps)"),
    MEDIUM("8000k", "Medium (8 Mbps)"),
    HIGH("15000k", "High (15 Mbps)"),
    ULTRA("25000k", "Ultra (25 Mbps)")
}

/**
 * Export engine state and progress tracking.
 */
class ExportEngine(private val context: Context) {
    private val _exportProgress = MutableStateFlow(0f) // 0-1
    private val _exportStatus = MutableStateFlow("Ready")
    private val _isExporting = MutableStateFlow(false)
    private val _currentFrame = MutableStateFlow(0)
    private val _totalFrames = MutableStateFlow(0)

    val exportProgress: StateFlow<Float> = _exportProgress
    val exportStatus: StateFlow<String> = _exportStatus
    val isExporting: StateFlow<Boolean> = _isExporting
    val currentFrame: StateFlow<Int> = _currentFrame
    val totalFrames: StateFlow<Int> = _totalFrames

    val presets = listOf(
        ExportPreset("Instagram Story", 1080, 1920, fps = 30, bitrate = "5000k"),
        ExportPreset("TikTok", 1080, 1920, fps = 30, bitrate = "6000k"),
        ExportPreset("YouTube HD", 1920, 1080, fps = 30, bitrate = "12000k"),
        ExportPreset("YouTube 4K", 3840, 2160, fps = 30, bitrate = "20000k"),
        ExportPreset("Square (1:1)", 1080, 1080, fps = 30, bitrate = "8000k"),
        ExportPreset("Wide (21:9)", 1920, 814, fps = 30, bitrate = "8000k"),
        ExportPreset("Film (DCI 4K)", 4096, 2160, fps = 24, bitrate = "25000k")
    )

    /**
     * Start exporting the composition. This is a mock implementation;
     * production should use FFmpeg or MediaCodec.
     */
    suspend fun startExport(composition: TimelineComposition, config: ExportConfig) {
        _isExporting.value = true
        _totalFrames.value = composition.duration.value
        _exportStatus.value = "Initializing encoder..."

        try {
            for (frame in 0 until composition.duration.value) {
                if (!_isExporting.value) break

                _currentFrame.value = frame
                _exportProgress.value = frame.toFloat() / composition.duration.value
                _exportStatus.value = "Rendering frame $frame / ${composition.duration.value}..."

                // Simulate frame rendering delay
                kotlinx.coroutines.delay(10)
            }

            _exportStatus.value = "Encoding video..."
            kotlinx.coroutines.delay(1000)

            if (_isExporting.value) {
                _exportProgress.value = 1f
                _exportStatus.value = "Export complete: ${config.outputPath}"
            }
        } catch (e: Exception) {
            _exportStatus.value = "Export failed: ${e.message}"
        } finally {
            _isExporting.value = false
        }
    }

    fun cancelExport() {
        _isExporting.value = false
        _exportStatus.value = "Export cancelled"
    }
}
