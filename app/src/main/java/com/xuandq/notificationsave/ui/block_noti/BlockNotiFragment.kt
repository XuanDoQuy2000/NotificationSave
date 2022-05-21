package com.xuandq.notificationsave.ui.block_noti

import com.xuandq.core.BaseFragment
import com.xuandq.litekoin.core.viewModels
import com.xuandq.notificationsave.R
import com.xuandq.notificationsave.databinding.FragmentBlockNotiBinding
import com.xuandq.notificationsave.ui.block_noti.adapter.AppAdapter

class BlockNotiFragment : BaseFragment<FragmentBlockNotiBinding,BlockNotiViewModel>() {
    override val layoutId: Int = R.layout.fragment_block_noti
    override val viewModel: BlockNotiViewModel by viewModels()

    private val appAdapter = AppAdapter()

    override fun initData() {
        viewModel.initData()
    }

    override fun initView() {
        binding.rvApp.adapter = appAdapter
    }

    override fun initEvent() {
        appAdapter.itemClickListener = { item,b ->
            item.isBlockNoti = !item.isBlockNoti
            viewModel.updateBlockStatus(item)
        }
    }

    override fun onObserve() {
        viewModel.listApp.observe(viewLifecycleOwner) {
            appAdapter.setData(it)
        }
    }
}