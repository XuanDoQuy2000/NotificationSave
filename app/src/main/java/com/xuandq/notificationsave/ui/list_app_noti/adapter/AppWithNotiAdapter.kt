package com.xuandq.notificationsave.ui.list_app_noti.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xuandq.notificationsave.databinding.ItemAppWithNotiBinding
import com.xuandq.notificationsave.model.AppWithNoti

class AppWithNotiAdapter : ListAdapter<AppWithNoti, AppWithNotiAdapter.AppNotiViewHolder>(
    AsyncDifferConfig.Builder(AppWithNoti.DIFF_CALLBACK).build()
) {

    var itemClickListener : ((AppWithNoti, Int) -> Unit)? = null

    private val viewPool = RecyclerView.RecycledViewPool()

    inner class AppNotiViewHolder(val binding: ItemAppWithNotiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: AppWithNoti) {
            binding.item = item
            val adapter = LastNotiAdapter()
            adapter.itemClickListener = { noti,i ->
                this@AppWithNotiAdapter.itemClickListener?.invoke(item,adapterPosition)
            }
            binding.rvLastNoti.adapter = adapter
            binding.rvLastNoti.setRecycledViewPool(viewPool)
            binding.root.setOnClickListener {
                itemClickListener?.invoke(item, adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppNotiViewHolder {
        return AppNotiViewHolder(
            ItemAppWithNotiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AppNotiViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    fun setData(newList : List<AppWithNoti>?){
        newList?.let {
            submitList(currentList.toMutableList().let {
                it.clear()
                it.addAll(newList)
                it
            })
        }
    }
}