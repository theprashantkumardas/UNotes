package com.example.unotes.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties


@Composable
fun MoreOptionsButton(onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var popupOffset by remember { mutableStateOf(IntOffset.Zero) }

    // Container for the "More" button and popup
    Box(
        modifier = Modifier
    ) {
        // "More" button with positioning
        IconButton(
            onClick = {
                expanded = !expanded  // Toggle the dropdown visibility
            },
            modifier = Modifier.onGloballyPositioned { coordinates: LayoutCoordinates ->
                // Calculate the global position of the button
                val position = coordinates.positionInWindow()
                popupOffset = IntOffset(position.x.toInt(), position.y.toInt() + coordinates.size.height)
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.MoreHoriz,
                contentDescription = "More Options",
                modifier = Modifier.size(24.dp)
            )
        }

        // Popup for the options
        if (expanded) {
            Popup(
                alignment = Alignment.TopStart,

                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = true),
            ) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.tertiary)
//                        .clip(shape = RoundedCornerShape(24.dp))
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.primary)
                        .padding(8.dp), // Add some padding inside the popup

                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Option 1
                    Row(

                    ) {
                        Text(
                            text = "Delete",
                            modifier = Modifier
                                .clickable {
                                    onOptionSelected("Delete")
                                    expanded = false
                                }
                                .padding(8.dp)
                        )
                    }

                    // Option 2
                    Row {
                        Text(
                            text = "Option 2",
                            modifier = Modifier
                                .clickable {
                                    onOptionSelected("Option 1")
                                    expanded = false
                                }
                                .padding(8.dp)
                        )
                    }

                    // Option 3
                    Row {
                        Text(
                            text = "Option 3",
                            modifier = Modifier
                                .clickable {
                                    onOptionSelected("Option 1")
                                    expanded = false
                                }
                                .padding(8.dp)
                        )
                    }

                }
            }
        }
    }
}