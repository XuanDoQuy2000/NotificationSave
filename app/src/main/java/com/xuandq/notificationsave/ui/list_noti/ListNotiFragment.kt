package com.xuandq.notificationsave.ui.list_noti

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.xuandq.core.BaseFragment
import com.xuandq.litekoin.core.viewModels
import com.xuandq.notificationsave.R
import com.xuandq.notificationsave.databinding.FragmentListNotiBinding

class ListNotiFragment : BaseFragment<FragmentListNotiBinding,ListNotiViewModel>() {
    override val layoutId: Int = R.layout.fragment_list_noti
    override val viewModel: ListNotiViewModel by viewModels()

    override fun initData() {

    }

    override fun initView() {

    }

    override fun initEvent() {

    }

    override fun onObserve() {

    }

}