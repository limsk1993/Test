package com.sumkim.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sumkim.view.R

@Composable
fun DocumentCard(
    title: String?,
    thumbnail: String?,
    publisher: String?,
    authors: List<String>?,
    price: Int?,
    salePrice: Int?,
    modifier: Modifier = Modifier,
    onDocumentClick: (() -> Unit)? = null,
    isFavorite: Boolean = false,
    onFavoriteClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .height(120.dp)
            .padding(8.dp)
            .clickable { onDocumentClick?.invoke() },
    ) {
        CustomAsyncImage(
            model = thumbnail,
            contentDescription = title,
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(4.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            CustomText(
                "도서",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(4.dp))
            CustomText(
                title ?: "",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(4.dp))
            Row {
                CustomText(
                    "출판사 : ",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                CustomText(
                    publisher ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                )
            }
            if (!authors.isNullOrEmpty()) {
                Spacer(Modifier.width(4.dp))
                Row {
                    CustomText(
                        "저자 : ",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    authors.forEach { author ->
                        CustomText(
                            author,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                        )
                    }
                }
            }
        }
        Spacer(Modifier.width(4.dp))
        Box(
            modifier = Modifier.fillMaxHeight()
        ) {
            CustomImageButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp),
                painter = painterResource(if (isFavorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off),
                contentDescription = "IsFavorite",
                colorFilter = ColorFilter.tint(Color.Red),
                onClick = { onFavoriteClick?.invoke() }
            )
            CustomText(
                "${salePrice ?: price ?: 0}원",
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}