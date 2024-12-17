package com.example.unotes.uiscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unotes.roomdatabase.presentation.NoteEvent
import com.example.unotes.roomdatabase.presentation.NoteState
import com.example.unotes.ui.theme.AlertDialogExample
import com.example.unotes.ui.theme.HorizontalDividerExample
import com.example.unotes.ui.theme.MoreOptionsButton
import com.example.unotes.ui.theme.satoshiLight
import com.example.unotes.ui.theme.satoshiRegular

@OptIn(ExperimentalMaterial3Api::class)
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
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                title = {
                    Text("")
                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        MoreOptionsButton(onOptionSelected = { option ->
                            when (option) {
                                "Delete" -> {
                                    note?.let { NoteEvent.DeleteNote(it) }?.let { onEvent(it) }
                                }
                                "Option 2" -> { /* Handle Option 2 */ }
                                "Option 3" -> { /* Handle Option 3 */ }
                            }
                        })
                    }
                },
                scrollBehavior = scrollBehavior,
            )

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

                BasicTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    textStyle =  TextStyle(
                        fontSize = 32.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = satoshiRegular,
//                        fontWeight = FontWeight.SemiBold
                    ),
                    decorationBox = { innerTextField ->
                        if (title.isEmpty()) {
                            Text(
                                text = "Title",
                                fontSize = 32.sp,
                                fontFamily = satoshiLight,

                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    },
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
//                        .height(108.dp)
                        .background(MaterialTheme.colorScheme.background)

                )
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDividerExample(text ="")
                Spacer(modifier = Modifier.height(4.dp))
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    textStyle =  TextStyle(
                        fontSize = 20.sp,
                        fontFamily = satoshiLight,
                        color = MaterialTheme.colorScheme.onPrimary,


                    ),
                    decorationBox = { innerTextField ->
                        if (description.isEmpty()) {
                            Text(
                                text = "Description",
                                fontFamily = satoshiLight,
                                fontSize = 20.sp,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    },
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),


                )
            }
        }
    }
}


@Preview(showBackground = true, name = "Add Note Screen")
@Composable
fun AddNoteScreenPreview() {
    val dummyState = NoteState(
        notes = listOf() // Provide an empty list for notes
    )
    val dummyNavController = rememberNavController()
    AddNoteScreen(
        state = dummyState,
        navController = dummyNavController,
        onEvent = {},
        noteId = null
    )
}
