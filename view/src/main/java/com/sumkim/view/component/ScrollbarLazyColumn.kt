package com.sumkim.view.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import my.nanihadesuka.compose.LazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSettings

@Composable
fun ScrollbarLazyColumn(
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumnScrollbar(
        modifier = modifier,
        state = listState,
        settings = ScrollbarSettings(
            scrollbarPadding = 4.dp,
            thumbSelectedColor = MaterialTheme.colorScheme.onSecondary,
            thumbUnselectedColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {
            content()
        }
    }
}