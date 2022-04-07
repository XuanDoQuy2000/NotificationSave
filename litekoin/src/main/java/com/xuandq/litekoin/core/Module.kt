package com.xuandq.litekoin.core

import androidx.lifecycle.ViewModel
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

/**
 * Created by rygelouv on 7/10/20.
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


class Module {

    val declarationRegistry: MutableMap<KClass<*>, BeanDeclaration<Any>> = ConcurrentHashMap()

    inline fun <reified T: Any> single(noinline declaration: Declaration<T>) {
        val value = BeanDeclaration<Any>(Kind.Single, declaration)
        declarationRegistry[T::class] = value
    }

    /**
     * support declare viewmodel in module
     * added by PhatHV 30/7/3030
     *
     */
    inline fun <reified T: ViewModel> viewModel(noinline declaration: Declaration<T>) {
        val value = BeanDeclaration<Any>(Kind.ViewModel, declaration)
        declarationRegistry[T::class] = value
    }

    /**
     * support declare factory in module
     * added by PhatHV 30/7/3030
     *
     */
    inline fun <reified T: Any> factory(noinline declaration: Declaration<T>) {
        val value = BeanDeclaration<Any>(Kind.Factory, declaration)
        declarationRegistry[T::class] = value
    }

    inline fun <reified T: Any> get(): T {
        val declaration = declarationRegistry[T::class]?.declaration
        var instance = declaration?.invoke()
        if (instance == null) {
            val liteKoin = LiteKoinContext.getLiteKoin()
            instance = liteKoin.declarations[T::class]?.declaration?.invoke() ?: error("Unable to find declaration of type ${T::class.qualifiedName}")
        }
        return instance as T
    }


    operator fun plus(module: Module) = listOf(module, this)
}


operator fun List<Module>.plus(module: Module) = this + listOf(module)


val List<Module>.declarationRegistry: Map<KClass<*>, BeanDeclaration<Any>>
    get() = this.fold(this[0].declarationRegistry) { acc, module -> (acc + module.declarationRegistry) as MutableMap<KClass<*>, BeanDeclaration<Any>> }


