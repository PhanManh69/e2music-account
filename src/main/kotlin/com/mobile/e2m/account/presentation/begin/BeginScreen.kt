package com.mobile.e2m.account.presentation.begin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder
import com.mobile.e2m.account.R
import com.mobile.e2m.core.ui.theme.E2MTheme
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
    modifier: Modifier = Modifier,
    goToStarted: () -> Unit = { },
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(R.raw.img_background_started)
            .build()
        imageLoader.enqueue(request)

        delay(1000)
        goToStarted()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(E2MTheme.alias.color.surface.backgroundDark)
    ) {
        AsyncImage(
            modifier = Modifier
                .size(E2MTheme.alias.size.icon.largeX)
                .align(Alignment.Center),
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.raw.img_logo_e2music)
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
    }
}
