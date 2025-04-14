package com.example.lhmind.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lhmind.domain.model.Peg
import com.example.lhmind.domain.model.PegColor
import com.example.lhmind.domain.model.toColor
import com.example.lhmind.ui.theme.LHMindTheme

@Composable
fun PegSelector(
    selectedPegs: List<Peg>,
    onColorSelected: (PegColor) -> Unit,
    onColorRemoved: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            selectedPegs.forEachIndexed { index, peg ->
                Surface(
                    color = peg.color.toColor(),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                ) {
                    IconButton(
                        onClick = { onColorRemoved(index) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove color"
                        )
                    }
                }
            }

            repeat(4 - selectedPegs.size) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                ) {
                    // Empty
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PegColor.entries.forEach { pegColor ->
                Surface(
                    color = pegColor.toColor(),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                        .clickable {
                            if (selectedPegs.size < 4) {
                                onColorSelected(pegColor)
                            }
                        }
                ) {
                    // Empty
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PegSelectorPreview() {
    LHMindTheme {
        var message by remember { mutableStateOf("") }
        var selectedPegs by remember { mutableStateOf(listOf(Peg(0, PegColor.BLACK))) }
        PegSelector(
            selectedPegs = selectedPegs,
            onColorSelected = {selectedPegs = selectedPegs.plus(Peg(selectedPegs.size, it))},
            onColorRemoved = {
                selectedPegs = selectedPegs.toMutableList().also { list -> list.removeAt(it) }
                message = "Color removed $it"
            }
        )
        Text(text = message)
    }
}
