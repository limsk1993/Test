package com.sumkim.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomCallBackLazyColumn(
    modifier: Modifier = Modifier,
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    atBottom: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: LazyListScope.() -> Unit
) {
    val scrollState = rememberLazyListState()
    ScrollAtBottomCallback(scrollState) {
        atBottom?.invoke()
    }
    LazyColumn(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        state = scrollState,
        contentPadding = contentPadding
    ) {
        content()
    }
}

@Composable
fun ScrollAtBottomCallback(scrollState: LazyListState, callback: () -> Unit) {
    val isAtBottom = scrollState.isAtBottom()
    LaunchedEffect(isAtBottom) {
        if (isAtBottom) callback.invoke()
    }
}

@Composable
fun LazyListState.isAtBottom(): Boolean {
    return remember(this) {
        derivedStateOf {
            if (layoutInfo.totalItemsCount == 0) {
                false
            } else {
                val lastVisibleItem =
                    layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
                val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset
                (lastVisibleItem.index == layoutInfo.totalItemsCount - 1) && (lastVisibleItem.offset + lastVisibleItem.size <= viewportHeight)
            }
        }
    }.value
}