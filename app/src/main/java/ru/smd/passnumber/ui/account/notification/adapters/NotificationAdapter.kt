package ru.smd.passnumber.ui.account.notification.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.smd.passnumber.data.core.Constants
import ru.smd.passnumber.data.entities.Notification
import ru.smd.passnumber.databinding.ItemNotificationBinding
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(val onReadListner: OnReadListner) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>(){

    interface OnReadListner {
        fun readNotification(notification: Notification)
    }

    var items = mutableListOf<Notification>()

    var layoutManager:RecyclerView.LayoutManager?=null

    private var prevDate: String? = null

    private var count: Int = 0

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        layoutManager=recyclerView.layoutManager
    }


    inner class ViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Notification) {
            binding.run {
                if (data.readAt==null
                   //TODO выдимый элемент или нет
                        ){
                    onReadListner.readNotification(data)
                }
                val time=data.createdAt.replaceBefore("T","")
                    .replace("T","")
                    .subSequence(0,5)
                txtTime.setText(time)
                val regNumber = data.data.body.subSequence(0, 6)
                txtNotificationRegNumber.setText(regNumber.toString().toLowerCase())
                val lenghtRegion = data.data.body.substringBefore(':')
                val region = data.data.body.subSequence(6, lenghtRegion.length)
                txtNotificationRegion.setText(region)
                val title =
                    data.data.body.subSequence(lenghtRegion.length + 1, data.data.body.length)
                        .toString()
                val result = data.pass.validityPeriod?.let { title.replace(it, "") }
                txtNotificationTitle.setText(result)
                txtNotificationType.setText(data.pass.validityPeriod)
                val year = data.createdAt.subSequence(0, 4).toString().toInt()
                val moth = data.createdAt.subSequence(5, 7).toString().toInt()
                val day = data.createdAt.subSequence(8, 10).toString().toInt()
                val dateNotificaton = (year.toString() + moth.toString() + day.toString())
                val calendar = java.util.Calendar.getInstance()
                calendar.set(year, moth-1, day)
                val date =
                    SimpleDateFormat(Constants.DATE_MASK, Locale.getDefault()).run {
                        format(calendar.time)
                    }
                txtNotificationDate.setText(date)
                if (prevDate != null && dateNotificaton.equals(prevDate)) {
                    txtHeaderNotification.visibility = View.GONE
                } else {
                    items.forEach {
                        val year1 = it.createdAt.subSequence(0, 4).toString().toInt()
                        val moth1 = it.createdAt.subSequence(5, 7).toString().toInt()
                        val day1 = it.createdAt.subSequence(8, 10).toString().toInt()
                        val dateNotificaton1 =
                            (year1.toString() + moth1.toString() + day1.toString())
                        if (dateNotificaton1.equals(dateNotificaton)) {
                            count++
                        }
                    }
                    txtNotificationSize.setText(count.toString())
                    count = 0
                    txtHeaderNotification.visibility = View.VISIBLE
                }
                prevDate = dateNotificaton
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
        prevDate=null
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.get(position))
    }

    override fun getItemCount() = items.size
}