package ru.smd.passnumber.ui.account.my_cars.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import ru.smd.passnumber.R
import ru.smd.passnumber.data.core.Constants
import ru.smd.passnumber.data.entities.CardCarWrapper
import ru.smd.passnumber.data.entities.PassData
import ru.smd.passnumber.data.entities.Type
import ru.smd.passnumber.databinding.*
import ru.smd.passnumber.ui.account.my_cars.filter.FilterCars
import java.text.SimpleDateFormat
import java.util.*


class MyCarsSwipeAdapter(val onClick: OnClickListner) :
    RecyclerView.Adapter<MyCarsSwipeAdapter.ViewHolder>() {

    var filterCars = FilterCars()
    var isFirstAdded = false

    var items = mutableListOf<CardCarWrapper>()

    private val viewBinderHelper = ViewBinderHelper()

    interface OnClickListner {
        fun onClickEdit(regNumber: String, driverName: String, mark: String)
        fun onClickDelete(id: Int)
        fun onClickCard(regNumber: String)
        fun onClickHelp()
    }

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        class GreenViewHolder(val binding: ItemMyCarsGreenBinding) : ViewHolder(binding.root)
        class BlueViewHolder(val binding: ItemMyCarsBlueBinding) : ViewHolder(binding.root)
        class YellowViewHolder(val binding: ItemMyCarsYellowBinding) : ViewHolder(binding.root)
        class RedViewHolder(val binding: ItemMyCarsRedBinding) : ViewHolder(binding.root)
        class GrayViewHolder(val binding: ItemMyCarsGrayBinding) : ViewHolder(binding.root)
    }

    override fun getItemViewType(position: Int) = items.get(position).type.type

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = when (viewType) {
        Type.Green.type -> ViewHolder.GreenViewHolder(
            ItemMyCarsGreenBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
        Type.Yellow.type -> {
            ViewHolder.YellowViewHolder(
                ItemMyCarsYellowBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
        Type.Blue.type -> {
            ViewHolder.BlueViewHolder(
                ItemMyCarsBlueBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
        Type.Gray.type -> {
            ViewHolder.GrayViewHolder(
                ItemMyCarsGrayBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
        else -> {
            ViewHolder.RedViewHolder(
                ItemMyCarsRedBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
    }

    var unfilteredItems = items

    fun addData(data: List<PassData>) {
        var cardCarWrapper: CardCarWrapper
        data.forEach {
            var days = 0
            if (!it.passes.isNullOrEmpty()) {
                it.passes[0].daysLeft?.let { days = it }
            }
            if (it.passes.isEmpty()) {
                cardCarWrapper = CardCarWrapper(Type.Gray, it)
            } else if (it.passes[0].number?.equals("????") == true) {
                cardCarWrapper = CardCarWrapper(Type.Blue, it)
            } else if (days > 60) {
                cardCarWrapper = CardCarWrapper(Type.Green, it)
            } else if (days > 0 && days < 60) {
                cardCarWrapper = CardCarWrapper(Type.Yellow, it)
            } else {
                cardCarWrapper = CardCarWrapper(Type.Red, it)
            }
            items.add(cardCarWrapper)
        }
        filterItems()
        notifyDataSetChanged()
    }

    fun setData(data: List<PassData>) {
        items.clear()
        var cardCarWrapper: CardCarWrapper
        data.forEach {
            var days = 0
            if (!it.passes.isNullOrEmpty()) {
                it.passes[0].daysLeft?.let { days = it }
            }
            if (it.passes.isEmpty()) {
                cardCarWrapper = CardCarWrapper(Type.Gray, it)
            } else if (it.passes[0].number?.equals("????") == true) {
                cardCarWrapper = CardCarWrapper(Type.Blue, it)
            } else if (days > 60) {
                cardCarWrapper = CardCarWrapper(Type.Green, it)
            } else if (days > 0 && days < 60) {
                cardCarWrapper = CardCarWrapper(Type.Yellow, it)
            } else {
                cardCarWrapper = CardCarWrapper(Type.Red, it)
            }
            items.add(cardCarWrapper)
        }
        filterItems()
        notifyDataSetChanged()
    }

    fun setSearchItems(text: String) {
        items = unfilteredItems.filter {
//            var number =
//                if (it.passData.passes.isNullOrEmpty()) {
//                    false
//                } else {
//                    it.passData.regNumber?.contains(text,ignoreCase = true) == true
//                }
            it.passData.mark?.contains(
                text,
                ignoreCase = true
            ) == true || it.passData.driverName?.contains(
                text,
                ignoreCase = true
            ) == true || it.passData.regNumber?.contains(text, ignoreCase = true) == true
        }.toMutableList()
        notifyDataSetChanged()
    }

    private fun filterItems() {
        unfilteredItems = items
        if (filterCars.isFiltered)
            items = unfilteredItems.filter {
                var passes = it.passData.passes
                if (passes.isNullOrEmpty()) {
                    (filterCars.filterByStatus == "??????") && filterCars.filterByTypePass.isNullOrEmpty() && filterCars.filterByPeriod.isNullOrEmpty()||(filterCars.filterByStatus == "?????? ??????????????????") && filterCars.filterByTypePass.isNullOrEmpty() && filterCars.filterByPeriod.isNullOrEmpty()
                } else {
                    var status: Boolean = when (filterCars.filterByStatus) {
                        "???? ???????????????? (?????????? ?????? ??????????????????????)" -> {
                            passes.first().status?.contains("??????????????????????") == true || passes.first().status?.contains(
                                "????????????????????"
                            ) == true
                        }
                        "????????????????" -> {
                            passes.first().daysLeft ?: 0 >= 0 && passes.first().status?.contains("??????????????????????") == false && passes.first().status?.contains(
                                "????????????????????"
                            ) == false
                        }
                       "?????? ??????????????????"->{
                           passes.isNullOrEmpty()
                       }
                        "??????" -> {
                            true
                        }
                        else -> passes.first().status == filterCars.filterByStatus
                    }
                    var typePass =
                        when (filterCars.filterByTypePass) {
                            "" -> true
                            else -> passes.first().number?.contains(filterCars.filterByTypePass) == true
                        }
                    var period =
                        when (filterCars.filterByPeriod) {
                            "" -> true
                            else -> passes.first().validityPeriod?.contains(filterCars.filterByPeriod) == true
                        }
                    status && typePass && period
                }

            }.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            Type.Green.type -> (holder as ViewHolder.GreenViewHolder).run {
                binding.run {
                    val data = items.get(position)
                    viewBinderHelper.setOpenOnlyOne(true)
                    viewBinderHelper.bind(
                        holder.binding.swipeLayoutMyCars,
                        data.passData.id.toString()
                    )
                    viewBinderHelper.closeLayout(data.passData.id.toString())
                    if (data.passData.mark.isNullOrEmpty()) {
                        imgTruck.visibility = View.GONE
                    } else imgTruck.visibility = View.VISIBLE
                    contEdit.setOnClickListener {
                        var regNumber = ""
                        var driverName = ""
                        var mark = ""
                        if (!data.passData.regNumber.isNullOrEmpty()) {
                            regNumber = data.passData.regNumber
                        }
                        if (!data.passData.driverName.isNullOrEmpty()) {
                            driverName = data.passData.driverName
                        }
                        if (!data.passData.mark.isNullOrEmpty()) {
                            mark = data.passData.mark
                        }
                        onClick.onClickEdit(regNumber, driverName, mark)
                    }
                    contDelete.setOnClickListener {
                        data.passData.id?.let { it1 -> onClick.onClickDelete(it1) }
                    }
                    mainContCardCar.setOnClickListener {
                        data.passData.regNumber?.let { it1 -> onClick.onClickCard(it1) }
                    }
                    val number = data.passData.regNumber?.subSequence(0, 6)
                    val editNumber = (number?.get(0)
                        ?.plus(" ")) + (number?.get(1)) + (number?.get(2)) + (number?.get(3)
                        ?.plus(" ")) + (number?.get(4)) + (number?.get(5))
                    val region =
                        data.passData.regNumber?.subSequence(6, data.passData.regNumber.length)
                    txtCardCarRegNumber.setText(editNumber)
                    txtCardCarRegion.setText(region)
                    txtCardCarModel.setText(data.passData.mark)
                    if (!data.passData.passes.isEmpty()) {
                        txtCardCarWay.setText(data.passData.passes.first().area)
                        if (data.passData.passes[0].number?.equals("????") == true) {
                            txtCardCarPeriod.setText(itemView.context.getString(R.string.one_time))
                        } else txtCardCarPeriod.setText(itemView.context.getString(R.string.annual))
                        txtCardCarId.setText(data.passData.passes[0].number)
                        txtCardCarDriverName.setText(data.passData.driverName)
                        val status = data.passData.passes[0].status?.let {
                            data.passData.passes[0].status?.subSequence(
                                8,
                                it.length
                            )
                        }
                        val remained = data.passData.passes[0].status?.let {
                            data.passData.passes[0].status?.subSequence(
                                0,
                                8
                            )
                        }
                        txtCarCardRemained.setText(remained.toString())
                        txtCardCardRemainedDay.setText(status.toString())
                        var year = data.passData.passes[0].validFrom?.subSequence(0, 4)
                        var month = data.passData.passes[0].validFrom?.subSequence(5, 7)
                        var day = data.passData.passes[0].validFrom?.subSequence(8, 10)
                        val calendar = Calendar.getInstance()
                        calendar.set(
                            year.toString().toInt(),
                            month.toString().toInt(),
                            day.toString().toInt()
                        )
                        var date =
                            SimpleDateFormat(Constants.DATE_MASK_NUMBER, Locale.getDefault()).run {
                                format(calendar.time)
                            }
                        txtCardCarDateFrom.setText(date)
                        year = data.passData.passes[0].validTo?.subSequence(0, 4)
                        month = data.passData.passes[0].validTo?.subSequence(5, 7)
                        day = data.passData.passes[0].validTo?.subSequence(8, 10)
                        calendar.set(
                            year.toString().toInt(),
                            month.toString().toInt(),
                            day.toString().toInt()
                        )
                        date =
                            SimpleDateFormat(Constants.DATE_MASK_NUMBER, Locale.getDefault()).run {
                                format(calendar.time)
                            }
                        txtCardCarDateTo.setText(
                            itemView.context.getString(
                                R.string.to_date,
                                date,
                                itemView.context.getString(R.string.inclusively)
                            )
                        )
                        txtCardCarType.setText(data.passData.passes[0].validityPeriod)
                    }


                }

            }
            Type.Yellow.type -> (holder as ViewHolder.YellowViewHolder).run {
                binding.run {
                    val data = items.get(position)
                    viewBinderHelper.setOpenOnlyOne(true)
                    viewBinderHelper.bind(
                        holder.binding.swipeLayoutMyCars,
                        data.passData.id.toString()
                    )
                    viewBinderHelper.closeLayout(data.passData.id.toString())
                    if (data.passData.mark.isNullOrEmpty()) {
                        imgTruck.visibility = View.GONE
                    } else imgTruck.visibility = View.VISIBLE
                    contEdit.setOnClickListener {
                        var regNumber = ""
                        var driverName = ""
                        var mark = ""
                        if (!data.passData.regNumber.isNullOrEmpty()) {
                            regNumber = data.passData.regNumber
                        }
                        if (!data.passData.driverName.isNullOrEmpty()) {
                            driverName = data.passData.driverName
                        }
                        if (!data.passData.mark.isNullOrEmpty()) {
                            mark = data.passData.mark
                        }
                        onClick.onClickEdit(regNumber, driverName, mark)
                    }
                    mainContCardCar.setOnClickListener {
                        data.passData.regNumber?.let { it1 -> onClick.onClickCard(it1) }
                    }
                    contDelete.setOnClickListener {
                        data.passData.id?.let { it1 -> onClick.onClickDelete(it1) }
                    }
                    btnCardCarHelp.setOnClickListener {
                        onClick.onClickHelp()
                    }
                    val number = data.passData.regNumber?.subSequence(0, 6)
                    val editNumber = (number?.get(0)
                        ?.plus(" ")) + (number?.get(1)) + (number?.get(2)) + (number?.get(3)
                        ?.plus(" ")) + (number?.get(4)) + (number?.get(5))
                    val region =
                        data.passData.regNumber?.subSequence(6, data.passData.regNumber.length)
                    txtCardCarRegNumber.setText(editNumber)
                    txtCardCarRegion.setText(region)
                    txtCardCarModel.setText(data.passData.mark)
                    if (!data.passData.passes.isEmpty()) {
                        txtCardCarWay.setText(data.passData.passes.first().area)
                        if (data.passData.passes[0].number?.toString().equals("????")) {
                            txtCardCarPeriod.setText(itemView.context.getString(R.string.one_time))
                        } else txtCardCarPeriod.setText(itemView.context.getString(R.string.annual))
                        txtCardCarId.setText(data.passData.passes[0].number)
                        txtCardCarDriverName.setText(data.passData.driverName)
                        val status = data.passData.passes[0].status?.let {
                            data.passData.passes[0].status?.subSequence(
                                8,
                                it.length
                            )
                        }
                        val remained = data.passData.passes[0].status?.let {
                            data.passData.passes[0].status?.subSequence(
                                0,
                                8
                            )
                        }
                        txtCarCardRemained.setText(remained.toString())
                        txtCardCardRemainedDay.setText(status.toString())
                        var year = data.passData.passes[0].validFrom?.subSequence(0, 4)
                        var month = data.passData.passes[0].validFrom?.subSequence(5, 7)
                        var day = data.passData.passes[0].validFrom?.subSequence(8, 10)
                        val calendar = Calendar.getInstance()
                        calendar.set(
                            year.toString().toInt(),
                            month.toString().toInt(),
                            day.toString().toInt()
                        )
                        var date =
                            SimpleDateFormat(Constants.DATE_MASK_NUMBER, Locale.getDefault()).run {
                                format(calendar.time)
                            }
                        txtCardCarDateFrom.setText(date)
                        year = data.passData.passes[0].validTo?.subSequence(0, 4)
                        month = data.passData.passes[0].validTo?.subSequence(5, 7)
                        day = data.passData.passes[0].validTo?.subSequence(8, 10)
                        calendar.set(
                            year.toString().toInt(),
                            month.toString().toInt(),
                            day.toString().toInt()
                        )
                        date =
                            SimpleDateFormat(Constants.DATE_MASK_NUMBER, Locale.getDefault()).run {
                                format(calendar.time)
                            }
                        txtCardCarDateTo.setText(
                            itemView.context.getString(
                                R.string.to_date,
                                date,
                                itemView.context.getString(R.string.inclusively)
                            )
                        )
                        txtCardCarType.setText(data.passData.passes[0].validityPeriod)
                    }
                }
            }
            Type.Red.type -> (holder as ViewHolder.RedViewHolder).run {
                binding.run {
                    val data = items.get(position)
                    viewBinderHelper.setOpenOnlyOne(true)
                    viewBinderHelper.bind(
                        holder.binding.swipeLayoutMyCars,
                        data.passData.id.toString()
                    )
                    viewBinderHelper.closeLayout(data.passData.id.toString())
                    if (data.passData.mark.isNullOrEmpty()) {
                        imgTruck.visibility = View.GONE
                    } else imgTruck.visibility = View.VISIBLE
                    contEdit.setOnClickListener {
                        var regNumber = ""
                        var driverName = ""
                        var mark = ""
                        if (!data.passData.regNumber.isNullOrEmpty()) {
                            regNumber = data.passData.regNumber
                        }
                        if (!data.passData.driverName.isNullOrEmpty()) {
                            driverName = data.passData.driverName
                        }
                        if (!data.passData.mark.isNullOrEmpty()) {
                            mark = data.passData.mark
                        }
                        onClick.onClickEdit(regNumber, driverName, mark)
                    }
                    mainContCardCar.setOnClickListener {
                        data.passData.regNumber?.let { it1 -> onClick.onClickCard(it1) }
                    }
                    contDelete.setOnClickListener {
                        data.passData.id?.let { it1 -> onClick.onClickDelete(it1) }
                    }
                    btnCardCarHelp.setOnClickListener {
                        onClick.onClickHelp()
                    }
                    val number = data.passData.regNumber?.subSequence(0, 6)
                    val editNumber = (number?.get(0)
                        ?.plus(" ")) + (number?.get(1)) + (number?.get(2)) + (number?.get(3)
                        ?.plus(" ")) + (number?.get(4)) + (number?.get(5))
                    val region =
                        data.passData.regNumber?.subSequence(6, data.passData.regNumber.length)
                    txtCardCarRegNumber.setText(editNumber)
                    txtCardCarRegion.setText(region)
                    txtCardCarModel.setText(data.passData.mark)
                    if (!data.passData.passes.isEmpty()) {
                        txtCardCarWay.setText(data.passData.passes.first().area)
                        if (data.passData.passes[0].number?.toString().equals("????")) {
                            txtCardCarPeriod.setText(itemView.context.getString(R.string.one_time))
                        } else txtCardCarPeriod.setText(itemView.context.getString(R.string.annual))
                        txtCardCarId.setText(data.passData.passes[0].number)
                        txtCardCarDriverName.setText(data.passData.driverName)
                        var remained: String = ""
                        var status: String = ""
                        if (data.passData.passes[0].status?.contains("????????????????????") == true) {
                            status = data.passData.passes[0].status?.let {
                                data.passData.passes[0].status?.subSequence(
                                    10,
                                    it.length
                                )
                            }.toString()
                            remained = data.passData.passes[0].status?.let {
                                data.passData.passes[0].status?.subSequence(
                                    0,
                                    10
                                )
                            }.toString()

                        }else{
                            status = data.passData.passes[0].status?.let {
                                data.passData.passes[0].status?.subSequence(
                                    11,
                                    it.length
                                )
                            }.toString()
                            remained = data.passData.passes[0].status?.let {
                                data.passData.passes[0].status?.subSequence(
                                    0,
                                    11
                                )
                            }.toString()
                        }

                        txtCarCardRemained.setText(remained)
                        txtCardCardRemainedDay.setText(status.toString())
                        var year = data.passData.passes[0].validFrom?.subSequence(0, 4)
                        var month = data.passData.passes[0].validFrom?.subSequence(5, 7)
                        var day = data.passData.passes[0].validFrom?.subSequence(8, 10)
                        val calendar = Calendar.getInstance()
                        calendar.set(
                            year.toString().toInt(),
                            month.toString().toInt(),
                            day.toString().toInt()
                        )
                        var date =
                            SimpleDateFormat(Constants.DATE_MASK_NUMBER, Locale.getDefault()).run {
                                format(calendar.time)
                            }
                        txtCardCarDateFrom.setText(date)
                        year = data.passData.passes[0].validTo?.subSequence(0, 4)
                        month = data.passData.passes[0].validTo?.subSequence(5, 7)
                        day = data.passData.passes[0].validTo?.subSequence(8, 10)
                        calendar.set(
                            year.toString().toInt(),
                            month.toString().toInt(),
                            day.toString().toInt()
                        )
                        date =
                            SimpleDateFormat(Constants.DATE_MASK_NUMBER, Locale.getDefault()).run {
                                format(calendar.time)
                            }
                        txtCardCarDateTo.setText(
                            itemView.context.getString(
                                R.string.to_date,
                                date,
                                itemView.context.getString(R.string.inclusively)
                            )
                        )
                        txtCardCarType.setText(data.passData.passes[0].validityPeriod)
                    }
                }
            }
            Type.Blue.type -> (holder as ViewHolder.BlueViewHolder).run {
                binding.run {
                    val data = items.get(position)
                    viewBinderHelper.setOpenOnlyOne(true)
                    viewBinderHelper.bind(
                        holder.binding.swipeLayoutMyCars,
                        data.passData.id.toString()
                    )
                    viewBinderHelper.closeLayout(data.passData.id.toString())
                    if (data.passData.mark.isNullOrEmpty()) {
                        imgTruck.visibility = View.GONE
                    } else imgTruck.visibility = View.VISIBLE
                    contEdit.setOnClickListener {
                        var regNumber = ""
                        var driverName = ""
                        var mark = ""
                        if (!data.passData.regNumber.isNullOrEmpty()) {
                            regNumber = data.passData.regNumber
                        }
                        if (!data.passData.driverName.isNullOrEmpty()) {
                            driverName = data.passData.driverName
                        }
                        if (!data.passData.mark.isNullOrEmpty()) {
                            mark = data.passData.mark
                        } else
                            onClick.onClickEdit(regNumber, driverName, mark)
                    }
                    mainContCardCar.setOnClickListener {
                        data.passData.regNumber?.let { it1 -> onClick.onClickCard(it1) }
                    }
                    contDelete.setOnClickListener {
                        data.passData.id?.let { it1 -> onClick.onClickDelete(it1) }
                    }
                    val number = data.passData.regNumber?.subSequence(0, 6)
                    val editNumber = (number?.get(0)
                        ?.plus(" ")) + (number?.get(1)) + (number?.get(2)) + (number?.get(3)
                        ?.plus(" ")) + (number?.get(4)) + (number?.get(5))
                    val region =
                        data.passData.regNumber?.subSequence(6, data.passData.regNumber.length)
                    txtCardCarRegNumber.setText(editNumber)
                    txtCardCarRegion.setText(region)
                    txtCardCarModel.setText(data.passData.mark)
                    if (!data.passData.passes.isEmpty()) {
                        txtCardCarWay.setText(data.passData.passes.first().area)
                        if (data.passData.passes[0].number?.toString().equals("????")) {
                            txtCardCarPeriod.setText(itemView.context.getString(R.string.one_time))
                        } else txtCardCarPeriod.setText(itemView.context.getString(R.string.annual))
                        txtCardCarId.setText(data.passData.passes[0].number)
                        txtCardCarDriverName.setText(data.passData.driverName)
                        val status = data.passData.passes[0].status?.let {
                            data.passData.passes[0].status?.subSequence(
                                8,
                                it.length
                            )
                        }
                        val remained = data.passData.passes[0].status?.let {
                            data.passData.passes[0].status?.subSequence(
                                0,
                                8
                            )
                        }
                        txtCarCardRemained.setText(remained.toString())
                        txtCardCardRemainedDay.setText(status.toString())
                        var year = data.passData.passes[0].validFrom?.subSequence(0, 4)
                        var month = data.passData.passes[0].validFrom?.subSequence(5, 7)
                        var day = data.passData.passes[0].validFrom?.subSequence(8, 10)
                        val calendar = Calendar.getInstance()
                        calendar.set(
                            year.toString().toInt(),
                            month.toString().toInt(),
                            day.toString().toInt()
                        )
                        var date =
                            SimpleDateFormat(Constants.DATE_MASK_NUMBER, Locale.getDefault()).run {
                                format(calendar.time)
                            }
                        txtCardCarDateFrom.setText(date)
                        year = data.passData.passes[0].validTo?.subSequence(0, 4)
                        month = data.passData.passes[0].validTo?.subSequence(5, 7)
                        day = data.passData.passes[0].validTo?.subSequence(8, 10)
                        calendar.set(
                            year.toString().toInt(),
                            month.toString().toInt(),
                            day.toString().toInt()
                        )
                        date =
                            SimpleDateFormat(Constants.DATE_MASK_NUMBER, Locale.getDefault()).run {
                                format(calendar.time)
                            }
                        txtCardCarDateTo.setText(
                            itemView.context.getString(
                                R.string.to_date,
                                date,
                                itemView.context.getString(R.string.inclusively)
                            )
                        )
                        txtCardCarType.setText(data.passData.passes[0].validityPeriod)
                    }
                }
            }
            Type.Gray.type -> (holder as ViewHolder.GrayViewHolder).run {
                binding.run {
                    val data = items.get(position)
                    viewBinderHelper.setOpenOnlyOne(true)
                    viewBinderHelper.bind(
                        holder.binding.swipeLayoutMyCars,
                        data.passData.id.toString()
                    )
                    viewBinderHelper.closeLayout(data.passData.id.toString())
                    if (data.passData.mark.isNullOrEmpty()) {
                        imgTruck.visibility = View.GONE
                    } else imgTruck.visibility = View.VISIBLE
                    contEdit.setOnClickListener {
                        var regNumber = ""
                        var driverName = ""
                        var mark = ""
                        if (!data.passData.regNumber.isNullOrEmpty()) {
                            regNumber = data.passData.regNumber
                        }
                        if (!data.passData.driverName.isNullOrEmpty()) {
                            driverName = data.passData.driverName
                        }
                        if (!data.passData.mark.isNullOrEmpty()) {
                            mark = data.passData.mark
                        }
                        onClick.onClickEdit(regNumber, driverName, mark)
                    }
                    mainContCardCar.setOnClickListener {
                        data.passData.regNumber?.let { it1 -> onClick.onClickCard(it1) }
                    }
                    contDelete.setOnClickListener {
                        data.passData.id?.let { it1 -> onClick.onClickDelete(it1) }
                    }
                    btnCardCarHelp.setOnClickListener {
                        onClick.onClickHelp()
                    }
                    val number = data.passData.regNumber?.subSequence(0, 6)
                    val editNumber = (number?.get(0)
                        ?.plus(" ")) + (number?.get(1)) + (number?.get(2)) + (number?.get(3)
                        ?.plus(" ")) + (number?.get(4)) + (number?.get(5))
                    val region =
                        data.passData.regNumber?.subSequence(6, data.passData.regNumber.length)
                    txtCardCarRegNumber.setText(editNumber)
                    txtCardCarRegion.setText(region)
                    if (!data.passData.driverName.isNullOrEmpty()) {
                        txtCardCarDriverName.setText(data.passData.driverName)
                    }
                    txtCardCarModel.setText(data.passData.mark)
                    txtCardCarDriverName.setText(data.passData.driverName)
                }
            }
        }
        if (isFirstAdded)
            viewBinderHelper.openLayout(items.get(0).passData.id.toString())
    }

    override fun getItemCount() = items.size
}