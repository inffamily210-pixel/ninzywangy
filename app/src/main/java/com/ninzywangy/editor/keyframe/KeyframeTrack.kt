package com.ninzywangy.editor.keyframe

import androidx.compose.runtime.mutableStateListOf
import kotlin.math.pow

/**
 * Interpolation curve type for keyframe animation.
 */
enum class InterpolationType {
    LINEAR, EASE_IN, EASE_OUT, EASE_IN_OUT, CUSTOM_BEZIER, HOLD
}

/**
 * A single keyframe at a specific time with a value and interpolation type.
 */
data class Keyframe(
    val frame: Int,
    val value: Float,
    val interpolation: InterpolationType = InterpolationType.LINEAR,
    val bezierControlIn: Pair<Float, Float>? = null,  // for CUSTOM_BEZIER
    val bezierControlOut: Pair<Float, Float>? = null
)

/**
 * A keyframe track animates a single property (e.g., position X, scale, opacity).
 */
class KeyframeTrack(
    val property: String = "opacity",
    val minValue: Float = 0f,
    val maxValue: Float = 1f
) {
    private val _keyframes = mutableStateListOf<Keyframe>()
    val keyframes: List<Keyframe> get() = _keyframes.sortedBy { it.frame }

    /** Add or update a keyframe. */
    fun addKeyframe(keyframe: Keyframe) {
        val existing = _keyframes.firstOrNull { it.frame == keyframe.frame }
        if (existing != null) {
            _keyframes[_keyframes.indexOf(existing)] = keyframe
        } else {
            _keyframes.add(keyframe)
        }
    }

    /** Remove a keyframe. */
    fun removeKeyframe(frame: Int) {
        _keyframes.removeAll { it.frame == frame }
    }

    /** Get value at a specific frame (interpolated). */
    fun getValueAtFrame(frame: Int): Float {
        if (_keyframes.isEmpty()) return 0f
        if (_keyframes.size == 1) return _keyframes[0].value

        val sorted = keyframes
        val before = sorted.lastOrNull { it.frame <= frame }
        val after = sorted.firstOrNull { it.frame > frame }

        if (before == null) return sorted.first().value
        if (after == null) return sorted.last().value
        if (before.frame == frame) return before.value

        val t = (frame - before.frame).toFloat() / (after.frame - before.frame).toFloat()
        return interpolate(before.value, after.value, t, before.interpolation, before.bezierControlOut)
    }

    /** Interpolate between two values based on interpolation type. */
    private fun interpolate(from: Float, to: Float, t: Float, type: InterpolationType, controlOut: Pair<Float, Float>?): Float {
        val eased = when (type) {
            InterpolationType.LINEAR -> t
            InterpolationType.EASE_IN -> easeIn(t)
            InterpolationType.EASE_OUT -> easeOut(t)
            InterpolationType.EASE_IN_OUT -> easeInOut(t)
            InterpolationType.CUSTOM_BEZIER -> if (controlOut != null) bezierInterpolate(t, controlOut) else t
            InterpolationType.HOLD -> if (t < 1f) 0f else 1f
        }
        return from + (to - from) * eased
    }

    private fun easeIn(t: Float) = t * t
    private fun easeOut(t: Float) = 1f - (1f - t).pow(2)
    private fun easeInOut(t: Float) = if (t < 0.5f) 2f * t * t else -1f + (4f - 2f * t) * t

    /** Simple cubic Bezier approximation. */
    private fun bezierInterpolate(t: Float, control: Pair<Float, Float>): Float {
        val x = control.first
        val y = control.second
        // Simplified cubic Bezier: B(t) = (1-t)³P0 + 3(1-t)²tP1 + 3(1-t)t²P2 + t³P3
        // For control point, treat as influence on curve shape
        return (1 - t).pow(3) * 0f + 3 * (1 - t).pow(2) * t * x + 3 * (1 - t) * t.pow(2) * y + t.pow(3) * 1f
    }

    /** Get all keyframes for UI rendering. */
    fun getAllKeyframes(): List<Keyframe> = _keyframes.sortedBy { it.frame }

    /** Get keyframe at or before a specific frame. */
    fun getKeyframeAt(frame: Int): Keyframe? = _keyframes.firstOrNull { it.frame == frame }

    /** Clear all keyframes. */
    fun clear() = _keyframes.clear()
}

/**
 * Multi-property animation: holds multiple keyframe tracks for complex object animation.
 * (e.g., position X, position Y, scale, rotation, opacity)
 */
class MultiPropertyAnimation {
    private val _tracks = mutableMapOf<String, KeyframeTrack>()

    fun addTrack(property: String, track: KeyframeTrack) {
        _tracks[property] = track
    }

    fun getTrack(property: String): KeyframeTrack? = _tracks[property]

    fun getAllTracks(): Map<String, KeyframeTrack> = _tracks

    fun getValues(frame: Int): Map<String, Float> {
        return _tracks.mapValues { (_, track) -> track.getValueAtFrame(frame) }
    }
}
