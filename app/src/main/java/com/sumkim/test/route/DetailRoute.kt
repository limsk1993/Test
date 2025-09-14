package com.sumkim.test.route

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.sumkim.test.getLocalDate
import com.sumkim.test.ui.theme.TestTheme
import com.sumkim.test.viewModel.DetailViewModel
import com.sumkim.view.component.CustomAsyncImage
import com.sumkim.view.component.CustomImageButton
import com.sumkim.view.component.CustomText
import kotlinx.coroutines.launch

@Composable
fun DetailRoute(
    vm: DetailViewModel = hiltViewModel()
) {
    val nav = Route.nav
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

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
        onFavoriteClick = { doc ->  coroutineScope.launch { vm.toggleFavorite(doc) } },
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
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.White
                ),
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
            Column (
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                CustomText(
                    document?.title ?: "",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = Int.MAX_VALUE
                )
                Spacer(Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CustomAsyncImage(
                        model = document?.thumbnail,
                        contentDescription = document?.title,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(2f / 3f)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.weight(1.5f)
                    ) {
                        if (!document?.authors.isNullOrEmpty()) {
                            Row {
                                CustomText(
                                    "저자 : ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                document?.authors?.forEachIndexed { index, author ->
                                    if (index != 0) {
                                        CustomText(
                                            ", $author",
                                            style = MaterialTheme.typography.bodyMedium,
                                            maxLines = Int.MAX_VALUE
                                        )
                                    } else {
                                        CustomText(
                                            author,
                                            style = MaterialTheme.typography.bodyMedium,
                                            maxLines = Int.MAX_VALUE
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                        }

                        document?.publisher?.let { publisher ->
                            Row {
                                CustomText(
                                    "출판사 : ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                                CustomText(
                                    publisher,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = Int.MAX_VALUE
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                        }

                        document?.datetime?.let { datetime ->
                            Row {
                                CustomText(
                                    "출간일 : ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                CustomText(
                                    datetime.getLocalDate(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = Int.MAX_VALUE
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                        }

                        document?.isbn?.let { isbn ->
                            Row {
                                CustomText(
                                    "ISBN : ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                CustomText(
                                    isbn,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = Int.MAX_VALUE
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                        }

                        document?.price?.let { price ->
                            Row {
                                CustomText(
                                    "정상가 : ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                CustomText(
                                    "${price}원",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = Int.MAX_VALUE
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                        }

                        if ((document?.salePrice ?: 0) > 0) {
                            Row {
                                CustomText(
                                    "할인가 : ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                CustomText(
                                    "${document?.salePrice}원",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = Int.MAX_VALUE
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                CustomText(
                    "책 소개",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(16.dp))

                document?.contents?.let { contents ->
                    CustomText(
                        contents,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = Int.MAX_VALUE
                    )
                }
            }
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