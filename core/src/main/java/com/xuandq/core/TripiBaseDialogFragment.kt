package com.xuandq.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

abstract class TripiBaseDialogFragment<VB : ViewDataBinding> : DialogFragment() {
    abstract val layoutId: Int
    lateinit var binding: VB

    override fun getTheme(): Int {
        return R.style.Noty_Dialog
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}