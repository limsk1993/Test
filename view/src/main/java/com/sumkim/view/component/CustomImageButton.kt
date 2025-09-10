package com.sumkim.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun CustomImageButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String?,
    colorFilter: ColorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .noRippleClickable { onClick?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter,
            contentDescription = contentDescription,
            colorFilter = colorFilter
        )
    }
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}