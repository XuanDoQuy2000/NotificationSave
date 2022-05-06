package com.xuandq.core

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.showAlert(
    title: CharSequence? = null,
    titleResId: Int? = null,
    message: CharSequence? = null,
    messageResId: Int? = null,
    negativeButtonResId: Int? = null,
    positiveButtonResId: Int? = null,
    negativeAction: (() -> Unit)? = null,
    positiveAction: (() -> Unit)? = null,
    singleButton : Boolean = false
) {
    NotyAlertDialog()
        .title(title)
        .title(titleResId)
        .message(message)
        .message(messageResId)
        .negativeButton(negativeButtonResId,negativeAction)
        .positiveButton(positiveButtonResId,positiveAction)
        .singleButton(singleButton)
        .show(this.supportFragmentManager,"vn.xuandq.core.alert_dialog")
}