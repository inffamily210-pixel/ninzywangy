package com.ninzywangy.editor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ninzywangy.editor.effects.EffectCatalog
import com.ninzywangy.editor.timeline.LayerType
import com.ninzywangy.editor.timeline.TimelineComposition
import com.ninzywangy.editor.timeline.TimelineLayer

private val BG = Color(0xFF0A0C12)
private val PANEL = Color(0xFF151A25)
private val SURFACE = Color(0xFF1C2431)
private val PURPLE = Color(0xFF8B5CF6)
private val MUTE = Color(0xFF9AA3B2)

@Composable
fun NinzywangyStudioScreen() {
    val composition = remember { TimelineComposition() }
    val selectedLayer = remember { mutableStateOf<TimelineLayer?>(null) }
    val selectedEffect = remember { mutableStateOf(EffectCatalog.all.firstOrNull()) }
    val showEffects = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        composition.addLayer(TimelineLayer("l1", "Video 1", LayerType.VIDEO, 0, 300, true))
        composition.addLayer(TimelineLayer("l2", "Text Title", LayerType.TEXT, 0, 300, true))
        composition.addLayer(TimelineLayer("l3", "Color Glow", LayerType.ADJUSTMENT, 0, 300, true))
        selectedLayer.value = composition.layers.firstOrNull()
    }

    MaterialTheme(colorScheme = androidx.compose.material3.darkColorScheme(background = BG, surface = PANEL, primary = PURPLE)) {
        Column(Modifier.fillMaxSize().background(BG)) {
            TopToolbar(
                projectName = composition.projectName.collectAsState().value,
                fps = composition.fps.collectAsState().value,
                onExport = { showEffects.value = !showEffects.value }
            )

            Row(Modifier.weight(1f).fillMaxWidth()) {
                LeftSidebar(
                    selectedLayer = selectedLayer.value,
                    onSelect = { selectedLayer.value = it }
                )

                VideoPreviewPanel(
                    frame = composition.currentFrame.collectAsState().value,
                    selectedLayer = selectedLayer.value
                )

                RightInspector(
                    layer = selectedLayer.value,
                    effect = selectedEffect.value,
                    onEffectPicked = { selectedEffect.value = it }
                )
            }

            TimelinePanel(
                composition = composition,
                selectedLayer = selectedLayer.value,
                onSelectLayer = { selectedLayer.value = it },
                showEffects = showEffects.value
            )
        }
    }
}

@Composable
private fun TopToolbar(projectName: String, fps: Int, onExport: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().height(56.dp).background(PANEL).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("NINZY", color = PURPLE, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("WANGY", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.width(18.dp))
        Text(projectName, color = Color.White, fontSize = 14.sp)
        Spacer(Modifier.weight(1f))
        Text("${fps} FPS  •  1080 × 1920", color = MUTE, fontSize = 12.sp)
        Spacer(Modifier.width(20.dp))
        Button(
            onClick = onExport,
            colors = ButtonDefaults.buttonColors(containerColor = PURPLE)
        ) {
            Text("Export")
        }
    }
}

@Composable
private fun LeftSidebar(selectedLayer: TimelineLayer?, onSelect: (TimelineLayer) -> Unit) {
    val layers = listOf(
        TimelineLayer("video1", "Video 1", com.ninzywangy.editor.timeline.LayerType.VIDEO),
        TimelineLayer("text1", "Text Title", com.ninzywangy.editor.timeline.LayerType.TEXT),
        TimelineLayer("adjust1", "Glow", com.ninzywangy.editor.timeline.LayerType.ADJUSTMENT),
        TimelineLayer("audio1", "Music", com.ninzywangy.editor.timeline.LayerType.AUDIO)
    )

    Column(
        Modifier.width(180.dp).fillMaxHeight().background(PANEL).padding(12.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Layers", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(Modifier.height(12.dp))
        layers.forEach { layer ->
            val active = selectedLayer?.id == layer.id
            Box(
                Modifier.fillMaxWidth().height(42.dp).background(if (active) PURPLE.copy(alpha = 0.18f) else SURFACE, RoundedCornerShape(10.dp)).border(1.dp, if (active) PURPLE else Color.Transparent, RoundedCornerShape(10.dp)).clickable { onSelect(layer) },
                contentAlignment = Alignment.CenterStart
            ) {
                Row(Modifier.padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(layer.type.icon, fontSize = 14.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(layer.name, color = Color.White, fontSize = 13.sp)
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun VideoPreviewPanel(frame: Int, selectedLayer: TimelineLayer?) {
    Box(
        Modifier.weight(1f).fillMaxHeight().padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier.fillMaxWidth().fillMaxHeight(0.8f).background(Color(0xFF111827), RoundedCornerShape(18.dp)).border(1.dp, Color(0xFF2C3544), RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Preview Canvas", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 26.sp)
                Spacer(Modifier.height(10.dp))
                Text("Frame ${frame.toString().padStart(3, '0')}  •  ${selectedLayer?.name ?: "No Layer"}", color = MUTE, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun RightInspector(layer: TimelineLayer?, effect: com.ninzywangy.editor.effects.EffectDefinition?, onEffectPicked: (com.ninzywangy.editor.effects.EffectDefinition) -> Unit) {
    Column(
        Modifier.width(260.dp).fillMaxHeight().background(PANEL).padding(14.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Inspector", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(Modifier.height(12.dp))

        if (layer != null) {
            Text("Layer: ${layer.name}", color = PURPLE, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(Modifier.height(12.dp))
            Text("Transform", color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            listOf(
                "Position: 540, 960",
                "Scale: 100%",
                "Rotation: 0°",
                "Opacity: 100%",
                "Blend: normal"
            ).forEach {
                Text(it, color = MUTE, fontSize = 12.sp, modifier = Modifier.padding(vertical = 4.dp))
            }
        }

        Spacer(Modifier.height(18.dp))
        Text("Keyframe", color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Add keyframe", color = MUTE, fontSize = 12.sp)
        Text("Linear / Ease In / Ease Out", color = MUTE, fontSize = 12.sp)

        Spacer(Modifier.height(18.dp))
        Text("Effect", color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        val selectedName = effect?.name ?: "No effect selected"
        Text(selectedName, color = PURPLE, fontSize = 13.sp)
        Spacer(Modifier.height(10.dp))

        val sampleEffects = EffectCatalog.all.take(8)
        sampleEffects.forEach { effectDef ->
            Text(
                effectDef.name,
                color = if (effectDef.id == effect?.id) PURPLE else MUTE,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onEffectPicked(effectDef) },
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun TimelinePanel(
    composition: TimelineComposition,
    selectedLayer: TimelineLayer?,
    onSelectLayer: (TimelineLayer) -> Unit,
    showEffects: Boolean
) {
    Column(Modifier.fillMaxWidth().height(230.dp).background(Color(0xFF0F141C))) {
        Row(Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { composition.setPlaying(!composition.isPlaying.value) }, colors = ButtonDefaults.buttonColors(containerColor = PURPLE)) {
                Text(if (composition.isPlaying.value) "Pause" else "Play")
            }
            Spacer(Modifier.width(18.dp))
            Text("Frame ${composition.currentFrame.collectAsState().value}", color = Color.White, fontSize = 13.sp)
            Spacer(Modifier.weight(1f))
            Text("${composition.duration.value} frames", color = MUTE, fontSize = 12.sp)
        }

        Row(Modifier.fillMaxSize()) {
            Column(Modifier.width(160.dp).padding(10.dp)) {
                val layers = composition.layers
                layers.forEachIndexed { index, layer ->
                    val active = selectedLayer?.id == layer.id
                    Box(
                        Modifier.fillMaxWidth().height(38.dp).background(if (active) PURPLE.copy(alpha = 0.2f) else SURFACE, RoundedCornerShape(8.dp)).border(1.dp, if (active) PURPLE else Color.Transparent, RoundedCornerShape(8.dp)).clickable { onSelectLayer(layer) },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(Modifier.padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(layer.type.icon, fontSize = 14.sp)
                            Spacer(Modifier.width(8.dp))
                            Text(layer.name, color = Color.White, fontSize = 12.sp)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }

            Column(Modifier.weight(1f).fillMaxHeight().padding(end = 10.dp)) {
                Row(Modifier.fillMaxWidth().height(22.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    (0 until 9).forEach { i ->
                        Text("${i * 5}s", color = MUTE, fontSize = 10.sp)
                    }
                }

                Box(
                    Modifier.fillMaxWidth().height(100.dp).background(Color(0xFF171F2A), RoundedCornerShape(10.dp)).padding(12.dp)
                ) {
                    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(PURPLE, Color(0xFF2DD4BF), Color(0xFFF59E0B), Color(0xFF10B981)).forEach { c ->
                            Box(
                                Modifier.weight(1f).fillMaxHeight().background(c.copy(alpha = 0.22f), RoundedCornerShape(6.dp)).border(1.dp, c, RoundedCornerShape(6.dp))
                            )
                        }
                    }
                }

                if (showEffects) {
                    Box(
                        Modifier.fillMaxWidth().height(70.dp).background(Color(0xFF111827), RoundedCornerShape(8.dp)).padding(8.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            repeat(6) { index ->
                                Box(
                                    Modifier.weight(1f).fillMaxHeight().background(Color(0xFF1E293B), RoundedCornerShape(6.dp)).padding(6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("FX ${index + 1}", color = MUTE, fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
