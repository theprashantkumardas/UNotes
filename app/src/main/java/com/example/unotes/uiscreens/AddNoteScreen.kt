package com.example.unotes.uiscreens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.unotes.roomdatabase.presentation.AddEditNoteState
import com.example.unotes.roomdatabase.presentation.DescriptionItem
import com.example.unotes.roomdatabase.presentation.NoteEvent
import com.example.unotes.roomdatabase.presentation.NoteState
import com.example.unotes.ui.theme.AlertDialogExample
import com.example.unotes.ui.theme.DescriptionDisplay
import com.example.unotes.ui.theme.HorizontalDividerExample
import com.example.unotes.ui.theme.MoreOptionsButton
import com.example.unotes.ui.theme.satoshiLight
import com.example.unotes.ui.theme.satoshiRegular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    state: NoteState,
    addEditState : AddEditNoteState,
    navController: NavController,
    onEvent: (NoteEvent) -> Unit,
    noteId: Int?
){
    val note = noteId?.let { id -> state.notes.find { it.id == id } }
    var title by remember { mutableStateOf(note?.title ?: "") }
    // var description by remember { mutableStateOf(note?.description ?: "") }
    var descriptionText by remember { mutableStateOf("")}
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
    ) { uris ->
        uris.map { it.toString() }.let {
            onEvent(NoteEvent.AddImage(it))
        }
    }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
    ) { uris ->
        uris.map { it.toString() }.let {
            onEvent(NoteEvent.AddVideo(it))
        }
    }

    LaunchedEffect(key1 = noteId){
        note?.let {
            title = it.title
            descriptionText = it.description
            onEvent(NoteEvent.AddVideo(it.videoUris ?: emptyList()))
            onEvent(NoteEvent.AddImage(it.imageUris ?: emptyList()))
        }
    }
    if (addEditState.error!=null){
        ErrorDialog(message = addEditState.error) {
            onEvent(NoteEvent.ClearState)
        }
    }

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
                    IconButton(onClick = { navController.popBackStack() }) {
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
                                    note?.let {
                                        onEvent(NoteEvent.DeleteNote(it))
                                        navController.popBackStack()
                                    }
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
            val isSaveEnabled = title.isNotEmpty() && descriptionText.isNotEmpty()
            FloatingActionButton(
                onClick = {
                    if (noteId == null) {
                        if (title.isNotEmpty() && descriptionText.isNotEmpty()) {
                            onEvent(
                                NoteEvent.SaveNote(
                                    title = title,
                                    description = descriptionText
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
                            description = descriptionText
                        ))
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .alpha(if (isSaveEnabled && !addEditState.isLoading) 1f else 0.3f)
                    .then(if (addEditState.isLoading) Modifier.clickable(enabled = false) {} else Modifier),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,


            ) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            BasicTextField(
                value = title,
                onValueChange = {
                    title = it
                },
                textStyle = TextStyle(
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = satoshiRegular,
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
                    .background(MaterialTheme.colorScheme.background)

            )
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDividerExample(text = "")
            Spacer(modifier = Modifier.height(4.dp))
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                value = descriptionText,
                onValueChange = {
                    descriptionText = it
                    onEvent(NoteEvent.UpdateDescription(it))
                },
                textStyle =  TextStyle(
                    fontSize = 20.sp,
                    fontFamily = satoshiLight,
                    color = MaterialTheme.colorScheme.onPrimary,

                    ),
                decorationBox = { innerTextField ->
                    if (descriptionText.isEmpty()) {
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
            DescriptionDisplay(description = addEditState.descriptionItems.joinToString(separator = "") { item ->
                when (item) {
                    is DescriptionItem.TextItem -> item.text
                    is DescriptionItem.ImageItem -> "<image>${item.uri}</image>"
                    is DescriptionItem.VideoItem -> "<video>${item.uri}</video>"
                }
            })

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(onClick = {
                    imagePickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }, enabled = !addEditState.isLoading) {
                    Text("Add Images")
                }
                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = {
                    videoPickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.VideoOnly
                        )
                    )
                }, enabled = !addEditState.isLoading) {
                    Text("Add Videos")
                }
            }
        }
    }
}

@Composable
fun ErrorDialog(message:String, onDismiss: () -> Unit){
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)) {
        Card {
            Column(Modifier.padding(16.dp)) {
                Text(text = message)
                Button(onClick = onDismiss) {
                    Text(text = "Dismiss")
                }
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
    val dummyAddEditState = AddEditNoteState()
    val dummyNavController = rememberNavController()
    AddNoteScreen(
        state = dummyState,
        addEditState = dummyAddEditState,
        navController = dummyNavController,
        onEvent = {},
        noteId = null
    )
}