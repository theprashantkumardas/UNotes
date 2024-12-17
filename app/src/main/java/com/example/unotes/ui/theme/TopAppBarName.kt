package com.example.unotes.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.unotes.R


@Composable
fun TopAppBarName(){
    Text(
        text = stringResource(id = R.string.app_name),
        modifier = Modifier,
        fontFamily = satoshiBold,
        fontSize = 40.sp,

        color = MaterialTheme.colorScheme.onPrimary
    )
}