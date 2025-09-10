package com.sumkim.test

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.sumkim.api.repository.ApiRepository
import com.sumkim.api.repository.ApiRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Module
@InstallIn(ViewModelComponent::class)
internal class RepositoryModule {
    @Provides
    @ViewModelScoped
    fun apiRepository(): ApiRepository = ApiRepositoryImpl()
}

object Extensions {
    @Composable
    inline fun <reified T> Flow<T>.collectWithLifecycle(
        lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        noinline action: suspend (T) -> Unit
    ) {
        LaunchedEffect(key1 = Unit) {
            lifecycleOwner.lifecycleScope.launch {
                flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState).collect(action)
            }
        }
    }

    fun String.encodeUrl(): String = URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
}

sealed interface CommonEvent {
    data class Toast(val msg: String? = null, @StringRes val resId: Int? = null) : CommonEvent
}