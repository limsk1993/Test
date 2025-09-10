package com.sumkim.view.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshBox(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: (() -> Unit)? = null,
    content: @Composable () -> Unit = {},
) {
    val refreshState = rememberPullToRefreshState()
    Box(
        modifier = modifier
            .pullToRefresh(
                state = refreshState,
                isRefreshing = isRefreshing,
                onRefresh = { onRefresh?.invoke() }
            )
    ) {
        content()
        PullToRefreshDefaults.Indicator(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing,
            state = refreshState
        )
    }
}