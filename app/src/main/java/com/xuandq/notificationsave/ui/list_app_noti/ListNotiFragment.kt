package com.xuandq.notificationsave.ui.list_app_noti

import android.util.Log
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.fragment.findNavController
import com.xuandq.core.BaseFragment
import com.xuandq.litekoin.core.viewModels
import com.xuandq.notificationsave.R
import com.xuandq.notificationsave.databinding.FragmentListNotiBinding
import com.xuandq.notificationsave.ui.list_app_noti.adapter.AppWithNotiAdapter

class ListNotiFragment : BaseFragment<FragmentListNotiBinding,ListNotiViewModel>() {
    override val layoutId: Int = R.layout.fragment_list_noti
    override val viewModel: ListNotiViewModel by viewModels()

    private val appNotiAdapter = AppWithNotiAdapter()

    override fun initData() {
        viewModel.initData()
    }

    override fun initView() {
        binding.viewModel = viewModel
        binding.rvAppNoti.adapter = appNotiAdapter
    }

    override fun initEvent() {
        appNotiAdapter.itemClickListener = { item, pos ->
            findNavController().navigate(
                ListNotiFragmentDirections.actionListNotiFragmentToListTitleFragment(item.app)
            )
        }
    }

    override fun onObserve() {
        viewModel.listAppWithNoti.observe(viewLifecycleOwner){
            appNotiAdapter.setData(it)
        }
        viewModel.listNoties.distinctUntilChanged().observe(viewLifecycleOwner) {
            Log.d("nnn", "onObserve: ${it.size}")
            viewModel.fetchListAppWithNoti()
        }
    }

}