package com.sumkim.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.sumkim.view.R

@Composable
fun CustomAsyncImage(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String?,
    contentScale: ContentScale = ContentScale.FillWidth
) {
    SubcomposeAsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        loading = {
            CircularProgressIndicator(modifier = Modifier.requiredSize(40.dp))
        },
        error = {
            Image(
                painter = painterResource(R.drawable.ic_broken_image),
                contentDescription = "load_error"
            )
        },
    )
}