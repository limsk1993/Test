package com.sumkim.view.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sumkim.view.R

@Composable
fun CustomPicker(
    modifier: Modifier = Modifier,
    @DrawableRes resId: Int,
    title: String,
    items: List<String>,
    onItemSelected: (String) -> Unit = {}
) {
    var showPicker by remember { mutableStateOf(false) }
    if (showPicker) {
        SystemStyleListPicker(
            items = items,
            onItemSelected = {
                onItemSelected(it)
            },
            onDismissRequest = {
                showPicker = false
            }
        )
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                showPicker = true
            }
            .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(16.dp))
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.size(16.dp),
            painter = painterResource(resId),
            contentDescription = "Picker"
        )
        Spacer(Modifier.width(2.dp))
        CustomText(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SystemStyleListPicker(
    items: List<String>,
    onItemSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        text = {
            Column {
                items.forEach { item ->
                    TextButton(
                        onClick = {
                            onItemSelected(item)
                            onDismissRequest()
                        }
                    ) {
                        CustomText(
                            text = item,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                CustomText(
                    text = stringResource(R.string.close),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}