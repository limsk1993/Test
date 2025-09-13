package com.sumkim.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastJoinToString
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sumkim.test.Extensions.encodeUrl

data class RouteConfig(
    val nav: NavHostController?
)

val LocalRouteConfig = staticCompositionLocalOf {
    RouteConfig(
        nav = null
    )
}

@Composable
fun RouteProvider(
    navController: NavHostController = rememberNavController(),
    content: @Composable () -> Unit
) {
    val routeConfig = RouteConfig(
        nav = navController
    )
    CompositionLocalProvider(LocalRouteConfig provides routeConfig) {
        content()
    }
}

fun NavHostController.moveToDetail(isbn: String) {
    val uri = buildNavUri(Route.Detail) {
        optionalEncoded("isbn", isbn)
    }
    navigate(uri)
}

internal inline fun buildNavUri(
    authority: String,
    builderAction: NavUriBuilder.() -> Unit
): String {
    return NavUriBuilder(authority).apply(builderAction).build()
}

class NavUriBuilder(private val authority: String) {
    private val pathList = ArrayList<String>()
    private val queryList = ArrayList<Pair<String, String>>()

    fun required(path: String) = pathList.add(path)

    fun requiredEncoded(path: String) = pathList.add(path.encodeUrl())

    fun optional(key: String, value: String?) {
        if (value == null) return
        queryList.add(key to value)
    }

    fun optionalEncoded(key: String, value: String?) {
        if (value == null) return
        queryList.add(key to value.encodeUrl())
    }

    fun build() = buildString {
        append(authority)
        pathList.fastForEach {
            append("/$it")
        }
        if (queryList.isNotEmpty()) {
            append("?")
            append(queryList.fastJoinToString(separator = "&") { "${it.first}=${it.second}" })
        }
    }
}

object Route {
    val nav: NavHostController
        @Composable get() = LocalRouteConfig.current.nav!!

    const val Main = "main"
    const val Detail = "detail"

    fun getDetailRoute() = "$Detail?isbn={isbn}"
}