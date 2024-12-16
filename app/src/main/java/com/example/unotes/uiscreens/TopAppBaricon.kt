package com.example.unotes.uiscreens

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unotes.roomdatabase.presentation.NoteEvent


@Composable
fun TopAppBarSortingIcon( onEvent: (NoteEvent) -> Unit ) {
    IconButton(onClick = { onEvent(NoteEvent.SortNotes) }) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.Sort,
            contentDescription = "Sorting Option",
            modifier = Modifier.size(36.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}
@Preview(showBackground = true)
@Composable
fun TopAppBarSortingIconPreview() {
    TopAppBarSortingIcon(
        onEvent = { /* Handle SortNotes event */ }
    )
}
