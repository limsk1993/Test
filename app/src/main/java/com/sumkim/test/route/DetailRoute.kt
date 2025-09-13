package com.sumkim.test.route

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sumkim.api.response.Document
import com.sumkim.test.CommonEvent
import com.sumkim.test.Extensions.collectWithLifecycle
import com.sumkim.test.R
import com.sumkim.test.Route
import com.sumkim.test.RouteProvider
import com.sumkim.test.ui.theme.TestTheme
import com.sumkim.test.viewModel.DetailViewModel
import com.sumkim.view.component.CustomAsyncImage
import com.sumkim.view.component.CustomImageButton

@Composable
fun DetailRoute(
    vm: DetailViewModel = hiltViewModel()
) {
    val nav = Route.nav
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (vm.ensureInit()) return@LaunchedEffect
        vm.setDocument(nav.previousBackStackEntry?.savedStateHandle?.get<Document>("document"))
    }

    val document by vm.document.collectAsStateWithLifecycle()
    val favoriteDocuments by vm.favoriteDocuments.collectAsStateWithLifecycle()

    vm.eventChannel.collectWithLifecycle {
        when (it) {
            is CommonEvent.Toast -> {
                if (it.msg != null) Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                else if (it.resId != null) Toast.makeText(context, it.resId, Toast.LENGTH_SHORT).show()
            }
        }
    }

    DetailScreen(
        document = document,
        favoriteDocuments = favoriteDocuments,
        onFavoriteClick = vm::toggleFavorite
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    document: Document?,
    favoriteDocuments: List<Document>,
    onFavoriteClick: (Document) -> Unit
) {
    val nav = Route.nav
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = {},
                navigationIcon = {
                    CustomImageButton(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = "back",
                        onClick = {
                            nav.popBackStack()
                        }
                    )
                },
                actions = {
                    CustomImageButton(
                        painter = painterResource(if (favoriteDocuments.contains(document)) com.sumkim.view.R.drawable.ic_favorite_on else com.sumkim.view.R.drawable.ic_favorite_off),
                        contentDescription = "IsFavorite",
                        colorFilter = ColorFilter.tint(Color.Red),
                        onClick = { document?.let { onFavoriteClick(it) } }
                    )
                }
            )
        },
        content = { innerPadding ->
            CustomAsyncImage(
                model = document?.thumbnail,
                contentDescription = document?.title,
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    TestTheme {
        RouteProvider {
            DetailRoute()
        }
    }
}