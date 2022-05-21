package com.xuandq.notificationsave.ui.list_notification

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.xuandq.core.BaseFragment
import com.xuandq.litekoin.core.viewModels
import com.xuandq.notificationsave.R
import com.xuandq.notificationsave.databinding.FragmentNotficationBinding
import com.xuandq.notificationsave.model.App
import com.xuandq.notificationsave.model.Title

class NotificationFragment : BaseFragment<FragmentNotficationBinding,NotificationViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_notfication
    override val viewModel: NotificationViewModel by viewModels()

    private val notiAdapter = NotiAdapter()
    private var app : App? = null
    private var title : Title? = null

    override fun initData() {
        arguments?.let {
            app = NotificationFragmentArgs.fromBundle(it).app
            title = NotificationFragmentArgs.fromBundle(it).title
            viewModel.initData(title!!.packageName, title!!.title)
        }
    }

    override fun initView() {
        binding.viewModel = viewModel
        binding.iconPath = app?.iconPath
        binding.title = title?.title
        binding.rvNoties.adapter = notiAdapter
        val linear = LinearLayoutManager(context)
        linear.reverseLayout = true
        binding.rvNoties.layoutManager = linear
    }

    override fun initEvent() {
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onObserve() {
        viewModel.listNoties.observe(viewLifecycleOwner) {
            notiAdapter.setData(it, app)
        }
    }
}