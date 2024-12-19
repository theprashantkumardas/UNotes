package com.example.unotes

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.unotes.roomdatabase.data.NotesDatabase
import com.example.unotes.roomdatabase.presentation.NotesViewModel
import com.example.unotes.ui.theme.UNotesTheme
import com.example.unotes.uiscreens.AddNoteScreen
import com.example.unotes.uiscreens.NotesScreen

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make the notification bar black
        WindowCompat.setDecorFitsSystemWindows(window, false) // Adjust content layout
        window.statusBarColor = android.graphics.Color.BLACK // Set color to black
        setContent {
            UNotesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val database = Room.databaseBuilder(
                        applicationContext,
                        NotesDatabase::class.java,
                        "note_db"
                    ).build()
                    val dao = database.dao
                    val viewModelFactory =
                        com.example.unotes.roomdatabase.presentation.ViewModelFactory(dao)
                    val viewModel = ViewModelProvider(
                        this,
                        viewModelFactory
                    ).get(NotesViewModel::class.java)
                    NoteApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun NoteApp(viewModel: NotesViewModel){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "notes_screen"){
        composable("notes_screen"){
            NotesScreen(
                state = viewModel.state.collectAsState().value,
                navController = navController,
                onEvent = viewModel::onEvent
            )
        }
        composable(
            route = "AddNoteScreen",
        ) {
            AddNoteScreen(
                state = viewModel.state.collectAsState().value,
                addEditState = viewModel.addEditState.collectAsState().value,
                navController = navController,
                onEvent = viewModel::onEvent,
                noteId = null
            )
        }
        composable(
            route = "AddNoteScreen/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) { entry ->
            val noteId = entry.arguments?.getInt("noteId")
            AddNoteScreen(
                state = viewModel.state.collectAsState().value,
                addEditState = viewModel.addEditState.collectAsState().value,
                navController = navController,
                onEvent = viewModel::onEvent,
                noteId = noteId
            )
        }
    }
}