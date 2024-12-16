package com.example.unotes.uiscreens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unotes.roomdatabase.presentation.NoteEvent
import com.example.unotes.roomdatabase.presentation.NoteState
import com.example.unotes.ui.theme.AlertDialogExample
import com.example.unotes.ui.theme.TopAppBarName

@Composable
fun AddNoteScreen(
    state: NoteState,
    navController: NavController,
    onEvent: (NoteEvent) -> Unit,
    noteId: Int?
){
    val note = noteId?.let { id -> state.notes.find { it.id == id } }
    var title by remember { mutableStateOf(note?.title ?: "") }
    var description by remember { mutableStateOf(note?.description ?: "") }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp)
                    .height(60.dp)
                    .padding(top = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TopAppBarName()
            }
        },
        floatingActionButton = {
            var showDialog by remember { mutableStateOf(false) }
            val isSaveEnabled = title.isNotEmpty() && description.isNotEmpty()
            FloatingActionButton(
                onClick = {
                    if (noteId == null) {
                        if (title.isNotEmpty() && description.isNotEmpty()) {
                            onEvent(
                                NoteEvent.SaveNote(
                                    title = title,
                                    description = description
                                )
                            )
                            navController.popBackStack()
                        }
                        else {
                            showDialog = true
                        }
                    } else {
                        onEvent(NoteEvent.UpdateNote(
                            noteId = noteId,
                            title = title,
                            description = description
                        ))
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.alpha(if (isSaveEnabled) 1f else 0.3f),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Done Button"
                )
            }
            if (showDialog) {
                AlertDialogExample(
                    onDismissRequest = { showDialog = false },
                    onConfirmation = { showDialog = false },
                    dialogTitle = "Empty Fields",
                    dialogText = "Please enter both a title and description before saving the note.",
                    icon = Icons.Rounded.EditNote
                )
            }
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(top = 20.dp)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                TextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    textStyle =  TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    placeholder = { Text("Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(108.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    textStyle =  TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    placeholder = {
                        Text(
                            text = "Description"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    maxLines = 8,
                )
            }
        }
    }
}