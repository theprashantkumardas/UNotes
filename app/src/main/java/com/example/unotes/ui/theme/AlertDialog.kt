package com.example.unotes.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


//@Composable
//fun AlertDialogExample(
//    onDismissRequest: () -> Unit,
//    onConfirmation: () -> Unit,
//    dialogTitle: String,
//    dialogText: String,
//    icon: ImageVector,
//) {
//    AlertDialog(
//        modifier = Modifier,
//        icon = {
//            Icon(
//                icon,
//                    contentDescription = "Example Icon",
//                    modifier = Modifier.size(40.dp)
//                )
//        },
//        title = {
//            Text(text = dialogTitle)
//        },
//        text = {
//            Text(text = dialogText)
//        },
//        onDismissRequest = {
//            onDismissRequest()
//        },
//        confirmButton = {
//            TextButton(
//                onClick = {
//                    onConfirmation()
//                }
//            ) {
//                Text("Confirm")
//            }
//        },
//        dismissButton = {
//            TextButton(
//                onClick = {
//                    onDismissRequest()
//                }
//            ) {
//                Text("Dismiss")
//            }
//        }
//    )
//}

@Composable
fun CustomAlertDialog(
    onDismissRequest: () -> Unit,
    message: String,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        // Dialog content
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Message text
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(8.dp).align(alignment = Alignment.CenterHorizontally),

            )

            // Horizontal Divider
            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            // OK button
            TextButton(
                onClick = onConfirm,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "OK",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
