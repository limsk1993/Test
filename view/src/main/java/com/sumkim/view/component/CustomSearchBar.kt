package com.sumkim.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sumkim.view.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    onSearch: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var query by rememberSaveable { mutableStateOf("") }
    SearchBar(
        windowInsets = WindowInsets(top = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = { query = it },
                onSearch = {
                    keyboardController?.hide()
                    onSearch(it)
                },
                expanded = false,
                onExpandedChange = { },
                leadingIcon = {
                    Image(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = "search"
                    )
                },
                placeholder = {
                    CustomText(
                        stringResource(R.string.search_write)
                    )
                }
            )
        },
        expanded = false,
        onExpandedChange = { },
        content = {}
    )
}