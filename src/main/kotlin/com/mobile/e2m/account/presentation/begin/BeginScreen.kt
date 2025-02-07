package com.mobile.e2m.account.presentation.begin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mobile.e2m.account.R
import com.mobile.e2m.core.ui.theme.BackgroundBegin
import kotlinx.coroutines.delay

@Composable
internal fun BeginScreen(
    goToStarted: () -> Unit = { }
) {
    BeginScaffold(
        goToStarted = { goToStarted() }
    )
}

@Composable
private fun BeginScaffold(
    goToStarted: () -> Unit = { },
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(R.raw.img_background_started)
            .build()
        imageLoader.enqueue(request)

        delay(8000)
        goToStarted()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBegin)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            model = ImageRequest.Builder(context)
                .data(R.raw.gif_logo_e2music)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
    }
}
