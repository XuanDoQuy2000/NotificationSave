package com.xuandq.core

import android.os.Bundle
import android.view.View
import com.xuandq.core.databinding.CoreDialogTwoButtonBinding

class NotyAlertDialog : TripiBaseDialogFragment<CoreDialogTwoButtonBinding>() {
    override val layoutId: Int = R.layout.core_dialog_two_button

    private var title: CharSequence? = null
    private var titleResId: Int? = null

    private var message: CharSequence? = null
    private var messageResId: Int? = null

    private var negativeButtonResId: Int? = null
    private var positiveButtonResId: Int? = null

    private var negativeAction: (() -> Unit)? = null
    private var positiveAction: (() -> Unit)? = null

    private var singleButton = false

    fun title(title: CharSequence?): NotyAlertDialog {
        this.title = title
        return this
    }

    fun title(resId: Int?): NotyAlertDialog {
        this.titleResId = resId
        return this
    }

    fun message(message: CharSequence?): NotyAlertDialog {
        this.message = message
        return this
    }

    fun message(resId: Int?): NotyAlertDialog {
        this.messageResId = resId
        return this
    }

    fun negativeButton(resId: Int?, callback: (() -> Unit)? = null): NotyAlertDialog {
        this.negativeButtonResId = resId
        this.negativeAction = callback
        return this
    }

    fun positiveButton(resId: Int?, callback: (() -> Unit)? = null): NotyAlertDialog {
        this.positiveButtonResId = resId
        this.positiveAction = callback
        return this
    }

    fun singleButton(single: Boolean): NotyAlertDialog {
        this.singleButton = single
        return this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (title != null) {
            binding.tvTitle.text = title
        } else {
            binding.tvTitle.setText(titleResId ?: R.string.core_label_notice)
        }

        if (message != null) {
            binding.tvMessage.text = message
        } else {
            binding.tvMessage.setText(messageResId ?: R.string.core_label_notice)
        }

        if (singleButton) {
            binding.btnNegative.visibility = View.GONE
            binding.vVerLine.visibility = View.GONE
        } else {
            binding.btnNegative.setText(negativeButtonResId ?: R.string.core_label_cancel)
            binding.btnNegative.setOnClickListener {
                negativeAction?.invoke()
                dismissAllowingStateLoss()
            }
        }

        binding.btnPositive.setText(positiveButtonResId ?: R.string.core_label_ok)
        binding.btnPositive.setOnClickListener {
            positiveAction?.invoke()
            dismissAllowingStateLoss()
        }
    }

}