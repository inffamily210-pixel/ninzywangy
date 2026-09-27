package com.ninzywangy.editor.effects

/** A serializable effect definition. The renderer can map [id] to a GPU/Canvas implementation. */
data class EffectDefinition(
    val id: String,
    val name: String,
    val category: EffectCategory,
    val description: String,
    val parameters: List<String> = emptyList()
)

enum class EffectCategory(val label: String) {
    COLOR("Color"), BLUR("Blur & Focus"), DISTORTION("Distortion"),
    LIGHT("Light"), GLITCH("Glitch"), PARTICLE("Particle"),
    STYLIZE("Stylize"), TRANSITION("Transition"), AUDIO_REACTIVE("Audio Reactive"),
    CHROMA("Chroma")
}

/**
 * Built-in effect catalog. These are editor presets and metadata; a production renderer
 * should implement each id as a shader, Canvas operation, or media pipeline node.
 */
object EffectCatalog {
    private fun item(category: EffectCategory, index: Int, name: String, description: String) =
        EffectDefinition(
            id = "${category.name.lowercase()}_${index.toString().padStart(3, '0')}",
            name = name,
            category = category,
            description = description,
            parameters = listOf("intensity", "opacity", "blendMode")
        )

    private fun family(category: EffectCategory, prefix: String, names: List<String>) =
        names.mapIndexed { index, name -> item(category, index + 1, "$prefix $name", "$prefix effect: $name") }

    val all: List<EffectDefinition> = buildList {
        addAll(family(EffectCategory.COLOR, "Color", listOf(
            "Cinematic", "Vintage", "Teal Orange", "Warm Film", "Cool Film", "Monochrome", "Noir", "Sepia", "Faded", "Bleach Bypass", "Cross Process", "Pastel", "Vivid", "Muted", "Golden Hour", "Blue Hour", "Sunset", "Midnight", "Rose", "Lavender", "Emerald", "Amber", "Crimson", "Arctic", "Desert", "Forest", "Ocean", "Neon", "Candy", "Coffee", "Chai", "Retro 70s", "Retro 80s", "Retro 90s", "Kodachrome", "Polaroid", "Lomo", "Technicolor", "Duo Tone", "Tri Tone", "Posterize", "Solarize", "Negative", "Threshold", "Channel Mixer", "Hue Shift", "Saturation Boost", "Contrast Pop", "Shadow Lift", "Highlight Roll", "HDR", "Log Lift", "Skin Tone", "Color Balance", "Selective Color", "Gradient Map", "LUT Film", "LUT Anime", "LUT Cyberpunk", "LUT Summer", "LUT Winter", "LUT Wedding", "LUT Documentary", "LUT Street", "LUT Portrait", "LUT Landscape", "LUT Food", "LUT Night", "LUT Dream", "LUT Bleak", "LUT Clean", "LUT Infrared", "LUT Pastel", "LUT Monochrome", "LUT Teal", "LUT Orange", "LUT Magenta", "LUT Lime", "LUT Gold", "LUT Silver", "LUT Copper", "LUT Rose Gold", "LUT Vapor", "LUT Grunge", "LUT Matte", "LUT High Key", "LUT Low Key", "LUT Film Grain", "LUT Super 8", "LUT VHS", "LUT B&W", "LUT Sepia", "LUT Autumn", "LUT Spring", "LUT Summer Pop", "LUT Winter Blue", "LUT Skin", "LUT Clean Skin", "LUT Studio", "LUT Editorial", "LUT Fashion", "LUT Travel", "LUT Music", "LUT Action", "LUT Sport", "LUT Minimal", "LUT Max Contrast", "LUT Soft Contrast", "LUT Flat", "LUT Rich Blacks", "LUT Creamy", "LUT Electric", "LUT Hologram", "LUT Prism", "LUT Spectrum", "LUT Aurora", "LUT Galaxy", "LUT Moonlight", "LUT Starlight", "LUT Firelight", "LUT Candlelight", "LUT Fluorescent", "LUT Tungsten", "LUT Daylight", "LUT Cloudy", "LUT Shade", "LUT Underwater", "LUT Smoke", "LUT Rust", "LUT Paper", "LUT Ink", "LUT Chalk", "LUT Wash", "LUT Bleed", "LUT Dreamy", "LUT Soft Glow", "LUT Hard Light", "LUT Split", "LUT Film Print", "LUT Cinema Print", "LUT Classic Print", "LUT Modern Print", "LUT Bold Print", "LUT Soft Print", "LUT Neutral Print", "LUT Custom One", "LUT Custom Two", "LUT Custom Three", "LUT Custom Four", "LUT Custom Five", "LUT Custom Six", "LUT Custom Seven", "LUT Custom Eight", "LUT Custom Nine", "LUT Custom Ten"
        )))
        addAll(family(EffectCategory.BLUR, "Blur", listOf("Gaussian", "Motion", "Radial", "Zoom", "Tilt Shift", "Lens", "Box", "Directional", "Smart", "Surface", "Bokeh", "Dream", "Soft Focus", "Glow Blur", "Edge Blur", "Pixel Blur", "Hex Blur", "Kaleido Blur", "Echo Blur", "Trail Blur", "Focus Pull", "Miniature", "Speed Blur", "Spin Blur", "Shake Blur", "Light Blur", "Background Blur", "Portrait Blur", "Depth Blur", "Chromatic Blur")))
        addAll(family(EffectCategory.DISTORTION, "Distort", listOf("Wave", "Ripple", "Twirl", "Pinch", "Bulge", "Spherize", "Fisheye", "Turbulence", "Displace", "Shear", "Perspective", "Lens Warp", "Jelly", "Liquid", "Heat Haze", "Flag", "Wobble", "Shockwave", "Fractal", "Kaleidoscope", "Mirror", "Tunnel", "Sphere", "Cylinder", "Page Curl", "Mesh Warp", "Corner Pin", "Barrel", "Pincushion", "Glitch Warp")))
        addAll(family(EffectCategory.LIGHT, "Light", listOf("Bloom", "Lens Flare", "Sun Rays", "God Rays", "Light Leak", "Anamorphic", "Volumetric", "Flicker", "Strobe", "Flash", "Exposure Pulse", "Neon Edge", "Electric Glow", "Rainbow Flare", "Star Flare", "Prism", "Caustics", "Fire Glow", "Magic Glow", "Soft Glow", "Hard Glow", "Color Glow", "Directional Glow", "Radial Glow", "Edge Glow", "Film Burn", "Light Sweep", "Spotlight", "Aura", "Halo")))
        addAll(family(EffectCategory.GLITCH, "Glitch", listOf("RGB Split", "Scanlines", "Datamosh", "Digital Noise", "Pixel Sort", "Block Shift", "VHS", "VCR", "CRT", "Interlace", "Frame Tear", "Signal Drop", "Static", "Data Bend", "Bit Crush", "Compression", "Error Code", "Time Slip", "Channel Tear", "Line Jitter", "Color Bars", "Tracking", "Tape Warp", "Tape Noise", "Analog Drop", "Digital Drop", "Cyber Grid", "Hologram", "Matrix", "Terminal")))
        addAll(family(EffectCategory.PARTICLE, "Particle", listOf("Dust", "Snow", "Rain", "Ash", "Sparks", "Smoke", "Fog", "Bubbles", "Confetti", "Fireflies", "Stars", "Galaxy", "Leaves", "Petals", "Embers", "Magic", "Energy", "Pixel Dust", "Gold Dust", "Glitter", "Bokeh", "Hearts", "Circles", "Squares", "Triangles", "Lines", "Orbits", "Explosion", "Implosion", "Vortex")))
        addAll(family(EffectCategory.STYLIZE, "Style", listOf("Cartoon", "Comic", "Ink", "Pencil", "Watercolor", "Oil Paint", "Charcoal", "Sketch", "Edge Detect", "Emboss", "Mosaic", "Pixel Art", "Halftone", "Dither", "Ascii", "Duotone", "Outline", "Poster", "Paper", "Sticker", "Clay", "Plastic", "Metal", "Chrome", "Glass", "Wireframe", "Blueprint", "X-Ray", "Thermal", "Infrared")))
        addAll(family(EffectCategory.TRANSITION, "Transition", listOf("Fade", "Dissolve", "Wipe Left", "Wipe Right", "Wipe Up", "Wipe Down", "Circle", "Diamond", "Clock", "Iris", "Zoom In", "Zoom Out", "Spin", "Push", "Slide", "Split", "Ripple", "Glitch", "Flash", "Light Leak", "Film Burn", "Ink", "Paint", "Shape", "Pixel", "Mosaic", "Wave", "Page", "Cube", "Whip")))
        addAll(family(EffectCategory.AUDIO_REACTIVE, "Audio", listOf("Bass Pulse", "Beat Flash", "Spectrum", "Waveform", "Bass Zoom", "Treble Glow", "Vocal Shake", "Beat Shake", "Kick Distort", "Snare Flash", "HiHat Strobe", "Amplitude Warp", "Frequency Color", "Music Particles", "Audio Tunnel", "Beat Spin", "Beat Ripple", "Beat Glitch", "Audio Trails", "Voice Echo", "Bass Bloom", "Treble Spark", "Dynamic Blur", "Rhythm Wave", "Drop Impact", "Build Up", "Chorus Pulse", "Reverb Space", "Stereo Split", "Tempo Zoom")))
        addAll(family(EffectCategory.CHROMA, "Chroma", listOf("Green Screen", "Blue Screen", "Luma Key", "Color Key", "Spill Suppress", "Edge Refine", "Chroma Blur", "Chroma Glow", "Chroma Outline", "Chroma Shadow", "Chroma Replace", "Chroma Matte", "Chroma Invert", "Chroma Poster", "Chroma Split", "Chroma Shift", "Chroma Noise", "Chroma Feather", "Chroma Defringe", "Chroma Composite", "Key Light", "Key Dark", "Key Soft", "Key Hard", "Key Detail", "Key Hair", "Key Motion", "Key Clean", "Key Matte", "Key Track")))
    }

    init { check(all.size >= 300) { "Effect catalog must contain at least 300 effects" } }
    fun byCategory(category: EffectCategory) = all.filter { it.category == category }
    fun find(id: String) = all.firstOrNull { it.id == id }
}
