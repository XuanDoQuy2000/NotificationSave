package com.xuandq.litekoin.core

class BeanDeclaration<T>(var kind: Kind, var declaration: Declaration<T>)

enum class Kind {
    Single, Factory, ViewModel
}