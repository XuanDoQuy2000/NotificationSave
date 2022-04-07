package com.xuandq.litekoin.core

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner


/**
 * Created by rygelouv on 7/7/20.
 * <p>
 * LiteKoin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


fun getLiteKoin() = LiteKoinContext.getLiteKoin()

/**
 * support Factory
 * modified by PhatHv 30/3/2020
 *
 */
inline fun <reified T : Any> get(): T {
    val instance = getLiteKoin().declarations[T::class]
    return if (instance?.kind == Kind.Single) {
        val service = getLiteKoin().resolveInstance(T::class)
        service.instance as T
    } else {
        instance?.declaration?.invoke() as T
    }
}

inline fun <reified T : Any> inject(): Lazy<T> = lazy { get<T>() }

/**
 * getViewModel()
 * added by PhatHV 30/7/2020
 *
 */
inline fun <reified T : ViewModel> getViewModel(owner: ViewModelStoreOwner): T {
    val instance = getLiteKoin().declarations[T::class]
    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            try {
                return instance?.declaration?.invoke() as T
            } catch (e: Exception) {
                throw e
            }
        }
    }
    val viewModelProvider = ViewModelProvider(owner, factory)
    return viewModelProvider.get(T::class.java)
}

inline fun <reified T : ViewModel> getViewModelFactory(): ViewModelProvider.Factory {
    val instance = getLiteKoin().declarations[T::class]
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            try {
                return instance?.declaration?.invoke() as T
            } catch (e: Exception) {
                throw e
            }
        }
    }
}

/**
 * fun viewmodel()
 * support inject viewmodel from fragment
 * added by PhatHV 30/7/2020
 */

inline fun <reified T : ViewModel> ViewModelStoreOwner.viewModels(): Lazy<T> =
    lazy { getViewModel<T>(this) }

inline fun <reified T : ViewModel> Fragment.shareViewModel(): Lazy<T> =
    lazy { getViewModel<T>(requireActivity()) }

inline fun <reified T : ViewModel> Fragment.fragmentViewModel(): Lazy<T> =
    lazy { getViewModel<T>(requireParentFragment()) }

fun module(block: Module.() -> Unit) = Module().apply(block)

