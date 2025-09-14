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
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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
}

fun String.getLocalDate(): String {
    val zonedDateTime = ZonedDateTime.parse(this)
    val localDate: LocalDate = zonedDateTime.toLocalDate()
    return localDate.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"))
}

sealed interface CommonEvent {
    data class Toast(val msg: String? = null, @StringRes val resId: Int? = null) : CommonEvent
}

enum class Sort(val value: String) {
    ACCURACY("accuracy"),
    LATEST("latest"),
    ASC("asc"),
    DESC("desc"),
    LOWEST_PRICE("lowest_price"),
    HIGHEST_PRICE("highest_price"),
}