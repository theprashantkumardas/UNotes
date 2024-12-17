package com.example.unotes.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.unotes.roomdatabase.presentation.DescriptionItem

@Composable
fun DescriptionDisplay(description: String) {
    val items = parseDescription(description)
    Column(modifier = Modifier.fillMaxWidth()) {
        items.forEach { item ->
            when (item) {
                is DescriptionItem.TextItem -> {
                    Text(
                        text = item.text,
                        fontFamily = satoshiLight,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                is DescriptionItem.ImageItem -> {
                    AsyncImage(
                        model = item.uri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(300.dp)
                            .padding(16.dp)
                    )
                }
//                is DescriptionItem.VideoItem -> {
//                    VideoPlayer(videoUri = item.uri)
//                }
                is DescriptionItem.VideoItem -> TODO()
            }
        }
    }
}


fun parseDescription(description: String): List<DescriptionItem> {
    val items = mutableListOf<DescriptionItem>()
    var remaining = description

    while (remaining.isNotEmpty()) {
        val imageStartIndex = remaining.indexOf("<image>")
        val videoStartIndex = remaining.indexOf("<video>")
        val nextTagIndex = when {
            imageStartIndex == -1 -> videoStartIndex
            videoStartIndex == -1 -> imageStartIndex
            else -> minOf(imageStartIndex, videoStartIndex)
        }


        if (nextTagIndex == -1) {
            items.add(DescriptionItem.TextItem(remaining))
            break
        } else {
            if (nextTagIndex > 0) {
                val text = remaining.substring(0, nextTagIndex)
                items.add(DescriptionItem.TextItem(text))
            }
        }

        if (nextTagIndex == imageStartIndex) {
            val imageEndIndex = remaining.indexOf("</image>", imageStartIndex)
            if (imageEndIndex != -1) {
                val imageUri =
                    remaining.substring(imageStartIndex + "<image>".length, imageEndIndex)
                items.add(DescriptionItem.ImageItem(imageUri))
                remaining = remaining.substring(imageEndIndex + "</image>".length)

            } else{
                items.add(DescriptionItem.TextItem(remaining))
                break
            }


        } else if (nextTagIndex == videoStartIndex){
            val videoEndIndex = remaining.indexOf("</video>", videoStartIndex)
            if (videoEndIndex != -1) {
                val videoUri =
                    remaining.substring(videoStartIndex + "<video>".length, videoEndIndex)
                items.add(DescriptionItem.VideoItem(videoUri))
                remaining = remaining.substring(videoEndIndex + "</video>".length)

            }
            else {
                items.add(DescriptionItem.TextItem(remaining))
                break
            }
        }
    }
    return items
}


//@Composable
//fun VideoPlayer(videoUri: String) {
//    val context = LocalContext.current
//    val mediaItem = MediaItem.fromUri(videoUri)
//    val player = com.google.android.exoplayer2.ExoPlayer.Builder(context).build()
//    player.setMediaItem(mediaItem)
//    player.prepare()
//
//    AndroidView(
//        factory = {
//            StyledPlayerView(it).apply {
//                this.player = player
//            }
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(300.dp)
//    )
//}