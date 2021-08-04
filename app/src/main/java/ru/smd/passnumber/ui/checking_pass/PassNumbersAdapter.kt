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


class PassNumbersAdapter() :
    ListAdapter<PassesData, PassNumbersAdapter.PassNumberViewHolder>(DiffCallback()) {
    lateinit var regNumber: String
    var btnHelpClicked = MutableLiveData<Boolean>()
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
                var editNumber: String = ""
                if (!regNumber.isEmpty() && regNumber.length == 8) {
                    editNumber = (regNumber.get(0)
                        .plus(" ")) + (regNumber.get(1)) + (regNumber.get(2)) + (regNumber.get(3)
                        .plus(" ")) + (regNumber.get(4)) + (regNumber.get(5)).plus(" ") + regNumber[6] + regNumber[7]
                } else if (!regNumber.isEmpty() && regNumber.length == 9) {
                    editNumber = (regNumber.get(0)
                        .plus(" ")) + (regNumber.get(1)) + (regNumber.get(2)) + (regNumber.get(3)
                        .plus(" ")) + (regNumber.get(4)) + (regNumber.get(5)).plus(" ") + regNumber[6] + regNumber[7] + regNumber[8]
                }
                if (item.number?.contains("ББ") == true && item.daysLeft != null && item.daysLeft > 0) {
                    tvTopStart.setTextColor(Color.parseColor("#FFFFFF"))
                    tvSecondStart.setTextColor(Color.parseColor("#FFFFFF"))
                    tvThirdStart.setTextColor(Color.parseColor("#FFFFFF"))
                    tvTopEnd.setTextColor(Color.parseColor("#FFFFFF"))
                    tvSecondEnd.setTextColor(Color.parseColor("#000000"))
                    tvTopStart.text = editNumber
                    val text = item.status?.split(" ")
                    if (text?.size==3){
                        tvSecondStart.text=Html.fromHtml(itemView.context.getString(R.string.status,text[0],text[1],text[2]))
                    }else  tvSecondStart.text = item.status
                    tvTopEnd.text = item.area
                    tvSecondEnd.text = item.number
                    tvThirdStart.text = Html.fromHtml(
                        itemView.context.getString(
                            R.string.date_from_date_to,
                            item.validFrom,
                            item.validTo
                        )
                    )
                    tvThirdEnd.text = item.validityPeriod
                    llPassNumber.background.setColorFilter(
                        Color.parseColor("#3AB9FF"),
                        PorterDuff.Mode.SRC_ATOP
                    )
                } else if (item.daysLeft != null && item.daysLeft <= 60 && item.daysLeft > 0) {
                    tvTopStart.setTextColor(Color.parseColor("#C65F00"))
                    tvSecondStart.setTextColor(Color.parseColor("#C65F00"))
                    tvThirdStart.setTextColor(Color.parseColor("#C65F00"))
                    tvTopEnd.setTextColor(Color.parseColor("#C65F00"))
                    tvSecondEnd.setTextColor(Color.parseColor("#1FA6F1"))
                    tvTopStart.text = editNumber
                    val text = item.status?.split(" ")
                    if (text?.size==3){
                        tvSecondStart.text=Html.fromHtml(itemView.context.getString(R.string.status,text[0],text[1],text[2]))
                    }else  tvSecondStart.text = item.status
                    tvTopEnd.text = item.area
                    tvSecondEnd.text = item.number
                    tvThirdStart.text = Html.fromHtml(
                        itemView.context.getString(
                            R.string.date_from_date_to,
                            item.validFrom,
                            item.validTo
                        )
                    )
                    tvThirdEnd.text = item.validityPeriod
                    llPassNumber.background.setColorFilter(
                        Color.parseColor("#FFF6C5"),
                        PorterDuff.Mode.SRC_ATOP
                    )
                } else {
                    when (item.daysLeft) {
                        null -> {
                            tvTopStart.setTextColor(Color.parseColor("#686868"))
                            tvSecondStart.setTextColor(Color.parseColor("#686868"))
                            tvThirdStart.setTextColor(Color.parseColor("#686868"))
                            tvTopEnd.setTextColor(Color.parseColor("#686868"))
                            tvSecondStart.text = "Пропуск не найден"
                            tvTopStart.text = editNumber
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
                            tvSecondEnd.setTextColor(Color.parseColor("#1FA6F1"))
                            tvTopStart.text = editNumber
                            val text = item.status?.split(" ")
                            if (text?.size==3){
                                tvSecondStart.text=Html.fromHtml(itemView.context.getString(R.string.status,text[0],text[1],text[2]))
                            }else  tvSecondStart.text = item.status
                            tvTopEnd.text = item.area
                            tvSecondEnd.text = item.number
                            tvThirdStart.text = Html.fromHtml(
                                itemView.context.getString(
                                    R.string.date_from_date_to,
                                    item.validFrom,
                                    item.validTo
                                )
                            )
                            tvThirdEnd.text = item.validityPeriod
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
                            tvSecondEnd.setTextColor(Color.parseColor("#408E9B"))
                            tvTopStart.text = editNumber
                            val text = item.status?.split(" ")
                            if (text?.size==3){
                                tvSecondStart.text=Html.fromHtml(itemView.context.getString(R.string.status,text[0],text[1],text[2]))
                            }else  tvSecondStart.text = item.status
                            tvTopEnd.text = item.area
                            tvSecondEnd.text = item.number
                            tvThirdStart.text = Html.fromHtml(
                                itemView.context.getString(
                                    R.string.date_from_date_to,
                                    item.validFrom,
                                    item.validTo
                                )
                            )
                            tvThirdEnd.text = item.validityPeriod
                            llPassNumber.background.setColorFilter(
                                Color.parseColor("#E1F1D7"),
                                PorterDuff.Mode.SRC_ATOP
                            )
                        }
                    }
                }
                btnHelpRegistration.isGone = !(item.daysLeft == null || item.daysLeft <= 60)
                btnHelpRegistration.setOnClickListener {
                    btnHelpClicked.value = true
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
