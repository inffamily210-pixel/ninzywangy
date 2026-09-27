package com.ninzywangy.editor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF0B0B12)
private val Panel = Color(0xFF151522)
private val Purple = Color(0xFF8B5CF6)
private val Muted = Color(0xFF9896AA)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { NinzywangyEditor() }
    }
}

@Composable
fun NinzywangyEditor() {
    var selectedTool by remember { mutableStateOf("Select") }
    var playing by remember { mutableStateOf(false) }
    var frame by remember { mutableStateOf(0) }
    var selectedLayer by remember { mutableStateOf(0) }
    MaterialTheme(colorScheme = darkColorScheme(background = Bg, surface = Panel, primary = Purple)) {
        Column(Modifier.fillMaxSize().background(Bg)) {
            TopBar(onExport = { selectedTool = "Export queued" })
            Row(Modifier.weight(1f).fillMaxWidth()) {
                ToolRail(selectedTool) { selectedTool = it }
                PreviewPanel(frame, selectedTool, Modifier.weight(1f))
                InspectorPanel(selectedLayer) { selectedLayer = it }
            }
            Timeline(
                frame = frame,
                playing = playing,
                selectedLayer = selectedLayer,
                onFrameChange = { frame = it },
                onPlay = { playing = !playing },
                onLayer = { selectedLayer = it }
            )
        }
    }
}

@Composable
private fun TopBar(onExport: () -> Unit) {
    Row(Modifier.fillMaxWidth().height(52.dp).background(Panel).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Text("NINZY", color = Purple, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("WANGY", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.width(24.dp))
        Text("Untitled Project", color = Color.White)
        Spacer(Modifier.weight(1f))
        Text("1080p  •  30 FPS", color = Muted, fontSize = 12.sp)
        Spacer(Modifier.width(16.dp))
        Button(onClick = onExport, colors = ButtonDefaults.buttonColors(containerColor = Purple), contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp)) { Text("Export") }
    }
}

@Composable
private fun ToolRail(selected: String, onSelect: (String) -> Unit) {
    val tools = listOf("Select", "Media", "Text", "Shape", "Effect", "Audio")
    Column(Modifier.width(72.dp).fillMaxHeight().background(Panel).padding(vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        tools.forEach { tool ->
            val active = selected == tool
            Box(Modifier.padding(4.dp).fillMaxWidth().height(48.dp).background(if (active) Purple.copy(.25f) else Color.Transparent, RoundedCornerShape(8.dp)).clickable { onSelect(tool) }, contentAlignment = Alignment.Center) {
                Text(tool.take(1), color = if (active) Purple else Color.White, fontWeight = FontWeight.Bold)
            }
            Text(tool, color = if (active) Purple else Muted, fontSize = 9.sp)
        }
    }
}

@Composable
private fun PreviewPanel(frame: Int, tool: String, modifier: Modifier) {
    Column(modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.weight(1f).fillMaxWidth().background(Color.Black, RoundedCornerShape(8.dp)).border(1.dp, Color(0xFF303044), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Preview Canvas", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Frame ${frame.toString().padStart(3, '0')}  •  Tool: $tool", color = Muted, fontSize = 12.sp)
            }
        }
        Spacer(Modifier.height(10.dp))
        Text("◀   00:00:${(frame / 30).toString().padStart(2, '0')}   ▶", color = Color.White)
    }
}

@Composable
private fun InspectorPanel(selected: Int, onLayer: (Int) -> Unit) {
    Column(Modifier.width(210.dp).fillMaxHeight().background(Panel).padding(14.dp)) {
        Text("Properties", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(Modifier.height(16.dp))
        Text("Transform", color = Purple, fontWeight = FontWeight.Bold)
        listOf("Position     540, 960", "Scale        100%", "Rotation     0°", "Opacity      100%").forEach { value ->
            Row(Modifier.fillMaxWidth().padding(vertical = 9.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text(value.substringBefore("     "), color = Muted, fontSize = 12.sp); Text(value.substringAfter("     "), color = Color.White, fontSize = 12.sp) }
        }
        Spacer(Modifier.height(10.dp)); Text("Keyframe controls", color = Purple, fontWeight = FontWeight.Bold)
        Text("◆ Add keyframe", color = Color.White, modifier = Modifier.padding(top = 12.dp).clickable { onLayer(selected) })
        Text("◆ Previous / Next", color = Muted, modifier = Modifier.padding(top = 10.dp))
    }
}

@Composable
private fun Timeline(frame: Int, playing: Boolean, selectedLayer: Int, onFrameChange: (Int) -> Unit, onPlay: () -> Unit, onLayer: (Int) -> Unit) {
    Column(Modifier.fillMaxWidth().height(220.dp).background(Color(0xFF10101A))) {
        Row(Modifier.height(48.dp).fillMaxWidth().padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = onPlay, colors = ButtonDefaults.buttonColors(containerColor = Purple), modifier = Modifier.size(42.dp), contentPadding = PaddingValues(0.dp)) { Text(if (playing) "Ⅱ" else "▶") }
            Spacer(Modifier.width(12.dp)); Text("Frame $frame", color = Color.White); Spacer(Modifier.weight(1f)); Text("＋  Add Layer", color = Purple, modifier = Modifier.clickable { onLayer(selectedLayer + 1) })
        }
        Row(Modifier.fillMaxWidth().weight(1f)) {
            Column(Modifier.width(130.dp).padding(start = 12.dp)) {
                listOf("🎞 Video", "T  Title", "◇ Shape").forEachIndexed { i, name ->
                    Text(name, color = if (selectedLayer == i) Color.White else Muted, modifier = Modifier.height(38.dp).fillMaxWidth().clickable { onLayer(i) }.padding(10.dp))
                }
            }
            Column(Modifier.horizontalScroll(rememberScrollState()).padding(end = 20.dp)) {
                Row(Modifier.height(22.dp)) { (0..10).forEach { Text("${it}s       ", color = Muted, fontSize = 10.sp) } }
                listOf(Purple, Color(0xFF2DD4BF), Color(0xFFF59E0B)).forEachIndexed { i, color ->
                    Box(Modifier.padding(vertical = 4.dp).height(30.dp).width(620.dp).background(color.copy(.22f), RoundedCornerShape(5.dp)).border(1.dp, color, RoundedCornerShape(5.dp))) {
                        Row(Modifier.fillMaxSize().padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) { Text("◆                 ◆                 ◆", color = color, fontSize = 12.sp) }
                    }
                }
                Slider(value = frame.toFloat(), onValueChange = { onFrameChange(it.toInt()) }, valueRange = 0f..300f, modifier = Modifier.width(620.dp), colors = SliderDefaults.colors(thumbColor = Purple, activeTrackColor = Purple))
            }
        }
    }
}
