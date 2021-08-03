package ru.smd.passnumber.ui.checking_pass

/**
 * Created by Siddikov Mukhriddin on 7/22/21
 */

import android.graphics.Color
import android.graphics.PorterDuff
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_pass_numbers.view.*
import ru.smd.passnumber.R
import ru.smd.passnumber.data.entities.PassesData
import ru.smd.passnumber.utils.boldNumbers


class PassNumbersAdapter :
    ListAdapter<PassesData, PassNumbersAdapter.PassNumberViewHolder>(DiffCallback()) {
    var regNumber = ""
    var btnHelpClicked=MutableLiveData<Boolean>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassNumberViewHolder {
        return PassNumberViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pass_numbers, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PassNumberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PassNumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        init {
//            itemView.setOnClickListener {
//
//            }
//        }

        fun bind(item: PassesData) {
            itemView.apply {
                tvTopStart.text = regNumber
                tvTopEnd.text = item.area
                tvSecondStart.setText(Html.fromHtml(boldNumbers(item.status ?: "")))
                tvSecondEnd.text = item.number
                tvThirdEnd.text = item.validityPeriod
                tvThirdStart.setText(Html.fromHtml(item.getTime()))
                llThirdLine.isGone = false
                viewTop.isGone = false
                when (item.daysLeft) {
                    null -> {
                        tvTopStart.setTextColor(Color.parseColor("#686868"))
                        tvSecondStart.setTextColor(Color.parseColor("#686868"))
                        tvThirdStart.setTextColor(Color.parseColor("#686868"))
                        tvTopEnd.setTextColor(Color.parseColor("#686868"))
                        tvSecondStart.text = "Пропуск не найден"
                        tvTopEnd.text = ""
                        tvSecondEnd.text = ""
                        llThirdLine.isGone = true
                        viewTop.isGone = true
                        llPassNumber.background.setColorFilter(
                            Color.parseColor("#DDDDDD"),
                            PorterDuff.Mode.SRC_ATOP
                        )
                    }
                    0 -> {
                        tvTopStart.setTextColor(Color.parseColor("#8F533F"))
                        tvSecondStart.setTextColor(Color.parseColor("#8F533F"))
                        tvThirdStart.setTextColor(Color.parseColor("#8F533F"))
                        tvTopEnd.setTextColor(Color.parseColor("#8F533F"))
                        llPassNumber.background.setColorFilter(
                            Color.parseColor("#FDD9CD"),
                            PorterDuff.Mode.SRC_ATOP
                        )
                    }
                    else -> {
                        tvTopStart.setTextColor(Color.parseColor("#406D2E"))
                        tvSecondStart.setTextColor(Color.parseColor("#406D2E"))
                        tvThirdStart.setTextColor(Color.parseColor("#406D2E"))
                        tvTopEnd.setTextColor(Color.parseColor("#406D2E"))
                        llPassNumber.background.setColorFilter(
                            Color.parseColor("#E1F1D7"),
                            PorterDuff.Mode.SRC_ATOP
                        )
                    }
                }
                btnHelpRegistration.isGone = !(item.daysLeft == null || item.daysLeft <= 60)
                btnHelpRegistration.setOnClickListener {
                    btnHelpClicked.value=true
                }

            }
        }
    }

}

class DiffCallback : DiffUtil.ItemCallback<PassesData>() {
    override fun areItemsTheSame(oldItem: PassesData, newItem: PassesData): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: PassesData, newItem: PassesData): Boolean {
        return oldItem == newItem
    }
}
