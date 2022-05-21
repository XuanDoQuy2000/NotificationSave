package com.xuandq.notificationsave.shared

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.xuandq.notificationsave.R

@BindingAdapter("iconApp")
fun setAppIcon(imageView: ImageView, url : String?){
    Glide.with(imageView.context)
        .load(url)
        .error(R.mipmap.ic_launcher)
        .into(imageView)
}