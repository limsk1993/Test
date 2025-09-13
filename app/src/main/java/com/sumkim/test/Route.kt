package com.sumkim.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sumkim.api.response.Document

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

fun NavHostController.moveToDetail(document: Document) {
    currentBackStackEntry?.savedStateHandle?.set("document", document)
    navigate(Route.Detail)
}

object Route {
    val nav: NavHostController
        @Composable get() = LocalRouteConfig.current.nav!!

    const val Main = "main"
    const val Detail = "detail"
}