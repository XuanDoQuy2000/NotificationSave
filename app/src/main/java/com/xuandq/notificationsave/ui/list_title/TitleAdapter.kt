package com.xuandq.notificationsave.ui.list_title

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xuandq.notificationsave.databinding.ItemTitleWithNotiBinding
import com.xuandq.notificationsave.model.App
import com.xuandq.notificationsave.model.Notification
import com.xuandq.notificationsave.model.TitleWithNoti

class TitleAdapter : RecyclerView.Adapter<TitleAdapter.TitleViewHolder>() {

    private val items : ArrayList<TitleWithNoti> = ArrayList()
    var itemClickListener : ((TitleWithNoti, Int) -> Unit)? = null
    private var app : App? = null

    fun setData(list : List<TitleWithNoti>?, app: App?) {
        this.app = app
        list?.let {
            items.clear()
            items.addAll(it)
            notifyDataSetChanged()
        }
    }

    inner class TitleViewHolder(val binding: ItemTitleWithNotiBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindData(item : TitleWithNoti) {
            val firstNoti = item.listNoties.firstOrNull()
            binding.item = firstNoti
            binding.appIconPath = app?.iconPath
            binding.numberNoti = item.listNoties.size.toString()
            binding.root.setOnClickListener {
                itemClickListener?.invoke(item, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        return TitleViewHolder(
            ItemTitleWithNotiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        holder.bindData(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}