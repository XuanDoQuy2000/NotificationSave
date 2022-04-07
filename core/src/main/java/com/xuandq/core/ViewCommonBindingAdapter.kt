package com.xuandq.core

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("isVisible")
fun setVisible(view : View, predicate : Boolean){
    view.visibility = if (predicate) View.VISIBLE else View.GONE
}

@BindingAdapter("isInvisible")
fun setInvisible(view: View, predicate: Boolean) {
    view.visibility = if (predicate) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("isGone")
fun setGone(view : View, predicate : Boolean){
    view.visibility = if (predicate) View.GONE else View.VISIBLE
}

