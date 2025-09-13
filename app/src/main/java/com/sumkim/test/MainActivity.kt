package com.sumkim.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sumkim.test.route.DetailRoute
import com.sumkim.test.route.MainRoute
import com.sumkim.test.ui.theme.TestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestTheme {
                RouteProvider {
                    val nav = Route.nav
                    NavHost(
                        nav,
                        Route.Main,
                    ) {
                        composable(Route.Main) { MainRoute() }
                        composable(Route.Detail) { DetailRoute() }
                    }
                }
            }
        }
    }
}