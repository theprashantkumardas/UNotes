package com.example.unotes.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalDividerExample(text: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        HorizontalDivider(thickness = 2.dp)
        Text(
            text = text,
            color = Color.Gray,
            fontFamily = satoshiLight
        )
    }
}