package com.xuandq.notificationsave.ui.list_notification

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xuandq.notificationsave.databinding.ItemChatLeftBinding
import com.xuandq.notificationsave.databinding.ItemChatRightBinding
import com.xuandq.notificationsave.model.App
import com.xuandq.notificationsave.model.Notification

class NotiAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items : ArrayList<Notification> = ArrayList()

    private var app : App? = null

    fun setData(list: List<Notification>?, app: App?) {
        this.app = app
        list?.let {
            items.clear()
            items.addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        if (item.actor == "You"){
            return RIGHT
        }
        return LEFT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LEFT -> {
                LeftViewHolder(
                    ItemChatLeftBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            RIGHT -> {
                return RightViewHolder(
                    ItemChatRightBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                LeftViewHolder(
                    ItemChatLeftBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LeftViewHolder -> {
                holder.bindData(items[position])
            }
            is RightViewHolder -> {
                holder.bindData(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class LeftViewHolder(val binding:ItemChatLeftBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(item : Notification){
            binding.item = item
            binding.icon = item.iconPath ?: app?.iconPath

            if (adapterPosition == items.size - 1 ||
                item.timeStamp / DateUtils.DAY_IN_MILLIS
                != items[position + 1].timeStamp / DateUtils.DAY_IN_MILLIS
            ) {
                binding.date = item.getDateFormat()
            }
        }
    }

    inner class RightViewHolder(val binding: ItemChatRightBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(item : Notification){

        }
    }

    companion object {
        private const val LEFT = 10
        private const val RIGHT = 11
    }
}