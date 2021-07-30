package ru.smd.passnumber.ui.account.notification.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.smd.passnumber.data.entities.Notification
import ru.smd.passnumber.databinding.ItemNotificationBinding

class NotificationAdapter(): RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {



    var items = mutableListOf<Notification>()

    inner class ViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data:Notification ) {
            binding.run {

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        ItemNotificationBinding.inflate(
            LayoutInflater.from(
                parent.context
            ), parent, false
        )
    )

    fun setData(data: List<Notification>) {
        items = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.get(position))
    }

    override fun getItemCount()= items.size
}