package com.example.unotes.uiscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.unotes.R
import com.example.unotes.roomdatabase.data.Note
import com.example.unotes.roomdatabase.presentation.NoteEvent
import com.example.unotes.roomdatabase.presentation.NoteState
import com.example.unotes.ui.theme.AssistChipExample
import com.example.unotes.ui.theme.MoreOptionsButton
import com.example.unotes.ui.theme.generatePdf
import com.example.unotes.ui.theme.satoshiLight
import com.example.unotes.ui.theme.satoshiRegular
import com.example.unotes.ui.theme.sharePdf
import com.example.unotes.ui.theme.shareText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    state: NoteState,
    navController: NavController,
    onEvent: (NoteEvent) -> Unit,
) {
//    var searchQuery by remember { mutableStateOf("") }
//    var isSearchActive by remember { mutableStateOf(false) }
    var isSearchExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }



    // Filtered notes based on search query
    val filteredNotes = state.notes.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.description.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {

            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()

                        ) {
                            Text(
                                text = "U Notes",
                                fontFamily = satoshiRegular,
                                fontSize = 44.sp
                            )
                            IconButton(onClick = { isSearchExpanded = !isSearchExpanded }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.search),
                                    contentDescription = "Search",
                                    Modifier.size(44.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                },
                actions = {
                    if (isSearchExpanded) {
                        BasicTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .padding(horizontal = 16.dp)
                                .height(56.dp)
                                .align(alignment = Alignment.CenterVertically),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),

                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle =  TextStyle(
                                fontSize = 20.sp,
                                fontFamily = satoshiLight,
                                color = MaterialTheme.colorScheme.onPrimary,
                            ),
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "Search Notes...",
                                            fontFamily = satoshiLight,
                                            fontSize = 20.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    innerTextField()

                                }
                            },

                            )
                    }
                }
            )
            Spacer(modifier = Modifier.height(108.dp))
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("AddNoteScreen")
            },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
//            if (state.isLoading){
//                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
//                    CircularProgressIndicator()
//                }
//            } else if (state.error != null){
//                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
//                    Text(text = state.error)
//                }
//            } else {
            Spacer(modifier = Modifier.height(24.dp))
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item {
                        AssistChipExample(
                            tag = "All Notes",
                        )
                    }
                    item {
                        AssistChipExample(
                            tag = "Work",
                        )
                    }
                    item {
                        AssistChipExample(
                            tag = "Home",
                        )
                    }
                    item {
                        AssistChipExample(
                            tag = "Private",
                        )
                    }

//                    items(state.notes.map { it.title }) { tag ->
//                        AssistChipExample(tag)
//                    }
//                    item {
//                        AssistChipAddOnlyBtn()
//                    }


                }
            Spacer(modifier = Modifier.height(16.dp))
                // Display filtered notes
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalItemSpacing = 16.dp,
                ) {
                    items(filteredNotes.size) { index ->
                        NoteItem(
                            note = filteredNotes[index], // Use filtered notes here
                            onEvent = onEvent,
                            navController = navController
                        )
                    }
                }

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalItemSpacing = 16.dp,
                ){
                    items(state.notes.size){ index ->
                        NoteItem(
                            note = state.notes[index],
                            onEvent = onEvent,
                            navController = navController
                        )
                    }
                }
//            }
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onEvent: (NoteEvent) -> Unit,
    navController: NavController,

){
    val formattedDate = formatTimestamp(note.timestamp)
    var title by remember { mutableStateOf(note.title ?: "") }
    var descriptionText by remember { mutableStateOf(note.title ?: "")}

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(8.dp)
            .clickable {
                navController.navigate("AddNoteScreen/${note.id}")
            }
    ){
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = note.title,
                    fontSize = 18.sp,
                    fontFamily = satoshiLight,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 2, // Limit the title to one line
                    overflow = TextOverflow.Ellipsis
                )
                MoreOptionsButton(onOptionSelected = { option ->
                    when (option) {
                        "Delete" -> {
                            onEvent(NoteEvent.DeleteNote(note))
                        }
                        "Share as PDF" -> {
                            val pdfFile = generatePdf(
                                context = navController.context,
                                noteTitle = title,
                                noteDescription = descriptionText
                            )
                            sharePdf(context = navController.context, pdfFile = pdfFile)
                        }
                        "Share as Text" -> {
                            shareText(
                                context = navController.context,
                                title = title,
                                description = descriptionText
                            )
                        }
                    }
                })
            }
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = note.description,
                fontSize = 16.sp,
                fontFamily = satoshiLight,
                color = Color.White,
                lineHeight = 20.sp,
                maxLines = 8, // Limit the title to one line
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))
            Column (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ){
                Text(
                    text = formattedDate,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


fun formatTimestamp(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd MMM yy", Locale.getDefault())

    return dateFormat.format(Date(timestamp))
}


