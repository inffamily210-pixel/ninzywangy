package com.ninzywangy.editor.timeline

import androidx.compose.runtime.mutableStateListOf
import com.ninzywangy.editor.keyframe.KeyframeTrack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Represents a single layer in the timeline (video, text, shape, etc.)
 * Each layer has its own keyframe tracks, effects, and visibility state.
 */
data class TimelineLayer(
    val id: String,
    val name: String,
    val type: LayerType,
    val startFrame: Int = 0,
    val endFrame: Int = 300,
    val visible: Boolean = true,
    val locked: Boolean = false,
    val opacity: Float = 1f,
    val blendMode: String = "normal",
    val keyframeTrack: KeyframeTrack = KeyframeTrack(),
    val effectIds: MutableList<String> = mutableListOf(),
    val parentLayerId: String? = null // for grouping
)

enum class LayerType(val label: String, val icon: String) {
    VIDEO("Video", "🎬"), 
    AUDIO("Audio", "🎵"), 
    TEXT("Text", "T"), 
    SHAPE("Shape", "▪"),
    IMAGE("Image", "🖼"), 
    ADJUSTMENT("Adjustment", "⚙"),
    SOLID("Solid Color", "■"), 
    NULL("Null", "◯")
}

/**
 * Timeline composition manager: holds all layers, playhead, duration, FPS, and project metadata.
 */
class TimelineComposition {
    private val _layers = mutableStateListOf<TimelineLayer>()
    private val _currentFrame = MutableStateFlow(0)
    private val _isPlaying = MutableStateFlow(false)
    private val _fps = MutableStateFlow(30) // 30 FPS default
    private val _duration = MutableStateFlow(300) // frames
    private val _resolution = MutableStateFlow(Pair(1080, 1920)) // width x height (portrait)
    private val _projectName = MutableStateFlow("Untitled Project")
    private val _selectedLayerId = MutableStateFlow<String?>(null)

    val layers: List<TimelineLayer> get() = _layers
    val currentFrame: StateFlow<Int> = _currentFrame
    val isPlaying: StateFlow<Boolean> = _isPlaying
    val fps: StateFlow<Int> = _fps
    val duration: StateFlow<Int> = _duration
    val resolution: StateFlow<Pair<Int, Int>> = _resolution
    val projectName: StateFlow<String> = _projectName
    val selectedLayerId: StateFlow<String?> = _selectedLayerId

    /** Add a new layer to the timeline. */
    fun addLayer(layer: TimelineLayer) {
        _layers.add(layer)
        _selectedLayerId.value = layer.id
    }

    /** Remove a layer by ID. */
    fun removeLayer(layerId: String) {
        _layers.removeAll { it.id == layerId }
        if (_selectedLayerId.value == layerId) {
            _selectedLayerId.value = _layers.firstOrNull()?.id
        }
    }

    /** Reorder layers (move layer at [fromIndex] to [toIndex]). */
    fun reorderLayers(fromIndex: Int, toIndex: Int) {
        if (fromIndex in _layers.indices && toIndex in _layers.indices) {
            val item = _layers.removeAt(fromIndex)
            _layers.add(toIndex, item)
        }
    }

    /** Get layer by ID. */
    fun getLayer(id: String): TimelineLayer? = _layers.firstOrNull { it.id == id }

    /** Update a layer. */
    fun updateLayer(id: String, update: (TimelineLayer) -> TimelineLayer) {
        val index = _layers.indexOfFirst { it.id == id }
        if (index >= 0) {
            _layers[index] = update(_layers[index])
        }
    }

    /** Set playhead to a specific frame. */
    fun setFrame(frame: Int) {
        _currentFrame.value = frame.coerceIn(0, _duration.value - 1)
    }

    /** Start/stop playback. */
    fun setPlaying(playing: Boolean) {
        _isPlaying.value = playing
    }

    /** Set FPS. */
    fun setFps(fps: Int) {
        _fps.value = fps.coerceIn(1, 120)
    }

    /** Set total duration in frames. */
    fun setDuration(frames: Int) {
        _duration.value = frames.coerceAtLeast(1)
    }

    /** Set output resolution. */
    fun setResolution(width: Int, height: Int) {
        _resolution.value = Pair(width, height)
    }

    /** Set project name. */
    fun setProjectName(name: String) {
        _projectName.value = name
    }

    /** Select a layer. */
    fun selectLayer(id: String?) {
        _selectedLayerId.value = id
    }

    /** Toggle layer visibility. */
    fun toggleLayerVisibility(id: String) {
        updateLayer(id) { it.copy(visible = !it.visible) }
    }

    /** Toggle layer lock. */
    fun toggleLayerLock(id: String) {
        updateLayer(id) { it.copy(locked = !it.locked) }
    }

    /** Get current time in seconds. */
    fun getCurrentTimeSeconds(): Double {
        return _currentFrame.value.toDouble() / _fps.value.toDouble()
    }

    /** Get current time formatted as HH:MM:SS:FF. */
    fun getCurrentTimeFormatted(): String {
        val totalSeconds = getCurrentTimeSeconds().toInt()
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        val frame = _currentFrame.value % _fps.value
        return String.format("%02d:%02d:%02d:%02d", hours, minutes, seconds, frame)
    }
}
