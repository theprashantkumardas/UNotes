import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DockedSearchBarSample(
    query: String,
    onQueryChange: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    placeholder: @Composable (() -> Unit) = { Text("Search ...") },
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiary)

    ){
        DockedSearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = { /* Handle search action */ },
            active = active,
            onActiveChange = onActiveChange,
            placeholder = placeholder,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            modifier = modifier.padding(8.dp),
            shape = RoundedCornerShape(24.dp),
        ) {
            // Optional content to display below the search bar when active
            if (active) {
                SearchSuggestions(onSuggestionClick = { suggestion ->
                    onQueryChange(suggestion)
                    onActiveChange(false) // Close the search bar
                })
            }
        }
    }

}

@Composable
fun SearchSuggestions(onSuggestionClick: (String) -> Unit) {
    // Mocked search suggestions
    val suggestions = listOf("Note 1", "Note 2", "Example Note", "Test Note")

    Column {
        suggestions.forEach { suggestion ->
            TextButton(onClick = { onSuggestionClick(suggestion) }) {
                Text(text = suggestion)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDockedSearchBarSample() {
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    MaterialTheme {
        DockedSearchBarSample(
            query = searchText,
            onQueryChange = { searchText = it },
            active = isSearchActive,
            onActiveChange = { isSearchActive = it },
            placeholder = { Text("Search Notes") }
        )
    }
}
