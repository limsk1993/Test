package com.sumkim.test.route

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sumkim.test.CommonEvent
import com.sumkim.test.Extensions.collectWithLifecycle
import com.sumkim.test.RouteProvider
import com.sumkim.test.ui.theme.TestTheme
import com.sumkim.test.viewModel.MainViewModel
import com.sumkim.view.component.BottomNavigationRoute
import com.sumkim.view.component.CustomBottomNavigation
import com.sumkim.view.component.CustomText
import com.sumkim.view.component.MainScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainRoute(
    vm: MainViewModel = hiltViewModel()
) {
    val tabNav = rememberNavController()
    val navBackStackEntry by tabNav.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val context = LocalContext.current
    val routeList by remember {
        mutableStateOf(
            listOf(
                MainScreen.Search,
                MainScreen.Favorite
            )
        )
    }

    vm.eventChannel.collectWithLifecycle {
        when (it) {
            is CommonEvent.Toast -> {
                if (it.msg != null) Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                else if (it.resId != null) Toast.makeText(context, it.resId, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.White
                ),
                title = {
                    CustomText(
                        if (currentDestination?.route == BottomNavigationRoute.Favorite) "즐겨찾기"
                        else "검색",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
            )
        },
        bottomBar = {
            CustomBottomNavigation(tabNav, routeList)
        },
        content = { innerPadding ->
            NavHost(
                tabNav,
                BottomNavigationRoute.Search,
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                composable(BottomNavigationRoute.Search) { SearchRoute(vm) }
                composable(BottomNavigationRoute.Favorite) { FavoriteRoute(vm) }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    TestTheme {
        RouteProvider {
            MainRoute()
        }
    }
}