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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.unotes.roomdatabase.data.Note
import com.example.unotes.roomdatabase.presentation.NoteEvent
import com.example.unotes.roomdatabase.presentation.NoteState
import com.example.unotes.ui.theme.AssistChipAddOnlyBtn
import com.example.unotes.ui.theme.AssistChipExample
import com.example.unotes.ui.theme.DescriptionDisplay
import com.example.unotes.ui.theme.MoreOptionsButton
import com.example.unotes.ui.theme.satoshiLight
import com.example.unotes.ui.theme.satoshiRegular
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
                                    imageVector = Icons.Default.Search,
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
                                .border(1.dp, MaterialTheme.colorScheme.outline , shape = RoundedCornerShape(24.dp))
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
                                if( searchQuery.isEmpty()) {
                                    Text(
                                        text = "Search Notes...",
                                        fontFamily = satoshiLight,
                                        fontSize = 20.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.align(alignment = Alignment.CenterVertically),


                                        )
                                }
                                innerTextField()
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
                    items(state.notes.map { it.title }) { tag ->
                        AssistChipExample(tag)
                    }
                    item {
                        AssistChipAddOnlyBtn()
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
    navController: NavController
){
    val formattedDate = formatTimestamp(note.timestamp)
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
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 2, // Limit the title to one line
                    overflow = TextOverflow.Ellipsis
                )
                MoreOptionsButton(onOptionSelected = { option ->
                    when (option) {
                        "Delete" -> {
                            onEvent(NoteEvent.DeleteNote(note))
                        }
                        "Option 2" -> { /* Handle Option 2 */ }
                        "Option 3" -> { /* Handle Option 3 */ }
                    }
                })
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            DescriptionDisplay(
                description = note.description
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
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}


fun formatTimestamp(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd MMM yy", Locale.getDefault())
//    val dateFormat = SimpleDateFormat("hh:mm a dd MMM yy", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}




