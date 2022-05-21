package com.xuandq.notificationsave.ui.list_app_noti.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xuandq.notificationsave.databinding.ItemLastNotiBinding
import com.xuandq.notificationsave.model.Notification

class LastNotiAdapter : ListAdapter<Notification, LastNotiAdapter.LastNotiViewHolder>(
    AsyncDifferConfig.Builder(Notification.DIFF_CALLBACK).build()
) {

    var itemClickListener : ((Notification,Int) -> Unit)? = null

    inner class LastNotiViewHolder(val binding: ItemLastNotiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: Notification) {
            binding.item = item
            binding.clickable = true
            binding.root.setOnClickListener {
                itemClickListener?.invoke(item, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastNotiViewHolder {
        return LastNotiViewHolder(
            ItemLastNotiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LastNotiViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    fun setData(newList : List<Notification>?){
        newList?.let {
            submitList(currentList.toMutableList().let {
                it.clear()
                it.addAll(newList)
                it
            })
        }
    }
}

@BindingAdapter("listLastNoti")
fun setListLastNoti(rv: RecyclerView, newList: List<Notification>?){
    (rv.adapter as LastNotiAdapter).setData(newList)
}