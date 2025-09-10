package com.sumkim.view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sumkim.view.R

@Composable
fun ItemCard(
    title: String?,
    thumbnail: String?,
    modifier: Modifier = Modifier,
    onItemClick: (() -> Unit)? = null,
    isFavorite: Boolean = false,
    onFavoriteClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.clickable { onItemClick?.invoke() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            CustomAsyncImage(
                model = thumbnail,
                contentDescription = title,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .aspectRatio(3f / 2f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            CustomImageButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp),
                painter = painterResource(if (isFavorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off),
                contentDescription = "IsFavorite",
                colorFilter = ColorFilter.tint(Color(0xFFFF0000)),
                onClick = { onFavoriteClick?.invoke() }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        CustomText(
            title ?: "",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))
    }
}