package com.ninzywangy.editor.layer

/**
 * Represents a text layer with styling properties.
 */
data class TextLayerData(
    val text: String = "",
    val fontSize: Float = 48f,
    val fontFamily: String = "sans-serif",
    val fontColor: Int = 0xFFFFFFFF.toInt(),
    val fontWeight: String = "normal", // normal, bold, italic
    val alignment: String = "center", // left, center, right
    val letterSpacing: Float = 0f,
    val lineHeight: Float = 1.2f
)

/**
 * Represents a shape layer (rectangle, circle, polygon).
 */
data class ShapeLayerData(
    val shapeType: ShapeType = ShapeType.RECTANGLE,
    val width: Float = 100f,
    val height: Float = 100f,
    val fillColor: Int = 0xFF8B5CF6.toInt(),
    val strokeColor: Int? = null,
    val strokeWidth: Float = 2f,
    val cornerRadius: Float = 0f // for rectangle
)

enum class ShapeType {
    RECTANGLE, CIRCLE, POLYGON, STAR, HEART, DIAMOND
}

/**
 * Represents an image/video layer with file path and playback settings.
 */
data class MediaLayerData(
    val filePath: String = "",
    val startTimeMs: Long = 0,
    val speedMultiplier: Float = 1f,
    val isMuted: Boolean = false
)

/**
 * Represents an audio layer.
 */
data class AudioLayerData(
    val filePath: String = "",
    val startTimeMs: Long = 0,
    val volume: Float = 1f,
    val isMuted: Boolean = false
)

/**
 * Adjustment layer for effects and color corrections.
 */
data class AdjustmentLayerData(
    val brightness: Float = 0f,  // -100 to 100
    val contrast: Float = 0f,     // -100 to 100
    val saturation: Float = 0f,   // -100 to 100
    val hue: Float = 0f,          // 0 to 360
    val exposure: Float = 0f      // -10 to 10
)

/**
 * Transform properties for any layer.
 */
data class TransformData(
    val positionX: Float = 540f,  // center of 1080px
    val positionY: Float = 960f,  // center of 1920px
    val scaleX: Float = 1f,
    val scaleY: Float = 1f,
    val rotation: Float = 0f,     // degrees
    val anchorX: Float = 0.5f,    // 0-1 relative
    val anchorY: Float = 0.5f,
    val skewX: Float = 0f,
    val skewY: Float = 0f
)

/**
 * Factory for creating layer-specific data objects.
 */
object LayerDataFactory {
    fun createTextLayerData(text: String = "Text Layer"): TextLayerData {
        return TextLayerData(text = text)
    }

    fun createShapeLayerData(shapeType: ShapeType = ShapeType.RECTANGLE): ShapeLayerData {
        return ShapeLayerData(shapeType = shapeType)
    }

    fun createMediaLayerData(filePath: String): MediaLayerData {
        return MediaLayerData(filePath = filePath)
    }

    fun createAudioLayerData(filePath: String): AudioLayerData {
        return AudioLayerData(filePath = filePath)
    }

    fun createAdjustmentLayerData(): AdjustmentLayerData {
        return AdjustmentLayerData()
    }
}
