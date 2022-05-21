package com.xuandq.notificationsave.ui.list_title

import androidx.navigation.fragment.findNavController
import com.xuandq.core.BaseFragment
import com.xuandq.litekoin.core.viewModels
import com.xuandq.notificationsave.R
import com.xuandq.notificationsave.databinding.FragmentListTitleBinding
import com.xuandq.notificationsave.model.App

class ListTitleFragment : BaseFragment<FragmentListTitleBinding, ListTitleViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_list_title
    override val viewModel: ListTitleViewModel by viewModels()

    private val titleNotiAdapter = TitleAdapter()
    private var app : App? = null

    override fun initData() {
        arguments?.let {
            app = ListTitleFragmentArgs.fromBundle(it).app
            viewModel.initData(app!!.packageName)
        }
    }

    override fun initView() {
        binding.viewModel = viewModel
        binding.title = app?.label
        binding.rvTitles.adapter = titleNotiAdapter
    }

    override fun initEvent() {
        binding.toolbar.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        titleNotiAdapter.itemClickListener = { item, pos ->
            app?.let {
                findNavController().navigate(
                    ListTitleFragmentDirections.actionListTitleFragmentToNotificationFragment(
                        it,
                        item.title
                    )
                )
            }
        }
    }

    override fun onObserve() {
        viewModel.listTitle.observe(viewLifecycleOwner) {
            titleNotiAdapter.setData(it, app)
        }

        viewModel.listNoties.observe(viewLifecycleOwner) {
            app?.let { it1 -> viewModel.fetchListTitleWithNoti(it1.packageName) }
        }
    }
}