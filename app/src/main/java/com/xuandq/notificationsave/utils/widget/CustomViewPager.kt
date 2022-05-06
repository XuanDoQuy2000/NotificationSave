package com.xuandq.notificationsave.utils.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager

class CustomViewPager(
    context: Context,
    attributeSet: AttributeSet
) : ViewPager(context,attributeSet) {
    override fun canScroll(
        v: View,
        checkV: Boolean,
        dx: Int,
        x: Int,
        y: Int
    ): Boolean {
        return if (v !== this && v is ViewPager) {
            true
        } else super.canScroll(v, checkV, dx, x, y)
    }
}