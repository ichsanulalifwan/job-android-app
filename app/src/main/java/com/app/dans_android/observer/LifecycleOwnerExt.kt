package com.app.dans_android.observer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Observe [source] in coroutine [context] at [minState] lifecycle
 * and will efficiently stop observing as soon the contrary of [minState] is reached.
 */
inline fun <S> LifecycleOwner.observeState(
    source: Flow<S>,
    minState: Lifecycle.State = Lifecycle.State.RESUMED,
    context: CoroutineContext = Dispatchers.Main,
    crossinline action: suspend (newState: S) -> Unit,
) {
    lifecycleScope.launch(context) {
        repeatOnLifecycle(minState) {
            source.collect { state ->
                action(state)
            }
        }
    }
}