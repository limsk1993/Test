package com.sumkim.view.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sumkim.view.R

@Composable
fun CustomBottomNavigation(
    nav: NavHostController,
    screens: List<MainScreen>
) {
    val navBackStackEntry by nav.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    BottomAppBar(
        containerColor = Color.White
    ) {
        screens.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { navigateRoute(nav, screen) },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomImageButton(
                    painter = painterResource(if (isSelected) screen.enabledIconId else screen.disabledIconId),
                    contentDescription = stringResource(screen.labelId),
                    onClick = { navigateRoute(nav, screen) }
                )
                Spacer(Modifier.height(4.dp))
                CustomText(stringResource(screen.labelId))
            }
        }
    }
}

private fun navigateRoute(nav: NavHostController, screen: MainScreen) {
    nav.navigate(screen.route) {
        popUpTo(nav.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

sealed class MainScreen(
    val route: String,
    @StringRes val labelId: Int,
    @DrawableRes val disabledIconId: Int,
    @DrawableRes val enabledIconId: Int
) {
    data object Search :
        MainScreen(
            BottomNavigationRoute.Search,
            R.string.bottom_navigation_search,
            R.drawable.ic_search,
            R.drawable.ic_search
        )

    data object Favorite :
        MainScreen(
            BottomNavigationRoute.Favorite,
            R.string.bottom_navigation_favorites,
            R.drawable.ic_favorite_off,
            R.drawable.ic_favorite_on
        )
}

object BottomNavigationRoute {
    const val Search = "main_search"
    const val Favorite = "main_favorite"
}