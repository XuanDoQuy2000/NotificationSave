package com.xuandq.notificationsave.ui.block_noti.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xuandq.notificationsave.databinding.ItemAppBinding
import com.xuandq.notificationsave.model.App

class AppAdapter : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

    private var items : ArrayList<App> = ArrayList()

    var itemClickListener : ((App,Int) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData(value : List<App>?){
        value?.let {
            items.clear()
            items.addAll(value)
            notifyDataSetChanged()
        }
    }

    inner class AppViewHolder(val binding: ItemAppBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindData(app : App){
            binding.app = app
            binding.root.setOnClickListener {
                itemClickListener?.invoke(app, adapterPosition)
                notifyDataSetChanged()
            }

            binding.switchBlockNoti.setOnClickListener {
                itemClickListener?.invoke(app, adapterPosition)
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder(
            ItemAppBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bindData(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}