package com.xuandq.core

fun String?.isNotNullOrEmpty() : Boolean = !this.isNullOrEmpty()

fun String?.isNotNullOrBlank() : Boolean = this != null && this.isNotBlank()