package com.example.mastermind.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.mastermind.R

// Wrappa [content] con lo sfondo in legno su tutta l'app.
@Composable
fun WoodBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = Box(modifier.fillMaxSize()) {
    Image(
        painter        = painterResource(R.drawable.wood_background),
        contentDescription = null,
        modifier       = Modifier.fillMaxSize(),
        contentScale   = ContentScale.Crop
    )
    content()
}
