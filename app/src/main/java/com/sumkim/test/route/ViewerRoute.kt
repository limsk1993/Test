package com.sumkim.test.route

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sumkim.api.response.Document
import com.sumkim.test.CommonEvent
import com.sumkim.test.Extensions.collectWithLifecycle
import com.sumkim.test.R
import com.sumkim.test.Route
import com.sumkim.test.RouteProvider
import com.sumkim.test.ui.theme.TestTheme
import com.sumkim.test.viewModel.MainViewModel
import com.sumkim.view.component.CustomAsyncImage
import com.sumkim.view.component.CustomImageButton

@Composable
fun ViewerRoute(
    vm: MainViewModel = hiltViewModel(),
    selectedIsbn: String? = null,
) {
    val context = LocalContext.current
    vm.eventChannel.collectWithLifecycle {
        when (it) {
            is CommonEvent.Toast -> {
                if (it.msg != null) Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                else if (it.resId != null) Toast.makeText(context, it.resId, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ViewerScreen(
        document = vm.selectedSortItems(selectedIsbn),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewerScreen(
    document: Document?,
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
                        contentDescription = "close",
                        onClick = {
                            nav.popBackStack()
                        }
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
fun ViewerPreview() {
    TestTheme {
        RouteProvider {
            ViewerRoute()
        }
    }
}