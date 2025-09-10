package com.sumkim.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sumkim.test.route.MainRoute
import com.sumkim.test.route.ViewerRoute
import com.sumkim.test.ui.theme.TestTheme
import com.sumkim.test.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModels()

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
                        composable(Route.Main) { MainRoute(vm) }
                        composable(
                            Route.getViewerRoute(),
                            listOf(
                                navArgument("isbn") { type = NavType.StringType },
                            )
                        ) {
                            ViewerRoute(
                                vm = vm,
                                selectedIsbn = it.arguments?.getString("isbn"),
                            )
                        }
                    }
                }
            }
        }
    }
}