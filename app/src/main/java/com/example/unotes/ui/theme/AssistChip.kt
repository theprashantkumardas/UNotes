package com.example.unotes.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// AssistChipExample for individual chips
@Composable
fun AssistChipExample(tag: String) {
    AssistChip(
        onClick = { /* Handle tag click here */ },
        label = { Text(tag) },
//        leadingIcon = {
//            Icon(
//                imageVector = Icons.Filled.Settings,
//                contentDescription = "Assist Chip Icon",
//                modifier = Modifier.size(AssistChipDefaults.IconSize)
//            )
//        },

        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.height(36.dp),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.tertiary // Set background color

        ),

    )
}

@Composable
fun AssistChipAddOnlyBtn(){

    Box(
        modifier = Modifier
//            .size(48.dp) // Set a fixed size for the rounded box
            .height(36.dp)
            .width(50.dp)
            .background(
                color = MaterialTheme.colorScheme.primary, // Background color
                shape = RoundedCornerShape(24.dp) // Rounded corners
            ),
        contentAlignment = Alignment.Center // Center the icon inside the box
    ) {
        Icon(
            imageVector = Icons.Filled.Add, // Icon to display
            contentDescription = "Assist Chip Icon",
            modifier = Modifier, // Adjust icon size
            tint = MaterialTheme.colorScheme.onPrimary // Icon color
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChip(){
    AssistChipExample("Hekko")
}