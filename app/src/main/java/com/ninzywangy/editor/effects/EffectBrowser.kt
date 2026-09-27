package com.ninzywangy.editor.effects

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EffectBrowser(selectedId: String?, onSelect: (EffectDefinition) -> Unit) {
    LazyColumn(Modifier.padding(12.dp)) {
        items(EffectCatalog.all) { effect ->
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f)) {
                    Text(effect.name, color = if (effect.id == selectedId) Color(0xFF8B5CF6) else Color.White, fontSize = 14.sp)
                    Text(effect.category.label, color = Color(0xFF9896AA), fontSize = 11.sp)
                }
                Text("+", color = Color(0xFF8B5CF6), modifier = Modifier.padding(horizontal = 8.dp))
            }
        }
    }
}
