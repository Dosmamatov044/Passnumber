package ru.smd.passnumber.ui.account.my_cars.filter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_filter.*
import ru.smd.passnumber.R
import ru.smd.passnumber.ui.account.my_cars.MyCarsFragment.Companion.filterCars
import ru.smd.passnumber.ui.account.my_cars.MyCarsFragment.Companion.filterCarsPosition


/**
 * Created by Siddikov Mukhriddin on 7/21/21
 */
@AndroidEntryPoint
class FilterFragment : Fragment(R.layout.fragment_filter) {


    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinners()
        btnBackFilter.setOnClickListener { requireActivity().onBackPressed() }
        btnQuitFilter.setOnClickListener {
            filterCars.value = FilterCars(false)
            filterCarsPosition.value = FilterCarsPosition()
            requireActivity().onBackPressed()
        }
        btnApplyFilter.setOnClickListener {
            val filterByTypePass = when {
                spinnerSelectPass.selectedItem.toString().contains("ББ") -> "ББ"
                spinnerSelectPass.selectedItem.toString().contains("БА") -> "БА"
                else -> ""
            }
            val filterByPeriod= when {
                spinnerSelectPeriod.selectedItem.toString().contains("День") -> "Дневной"
                spinnerSelectPeriod.selectedItem.toString().contains("Ночь") -> "Ночной"
                spinnerSelectPeriod.selectedItem.toString().contains("Круглосуточный") -> "Круглосуточный"
                else->""
            }
            val sort = spinnerSelectSorts.selectedItemPosition % 2 == 0
            filterCars.value = FilterCars(
                true,
                filterByStatus = spinnerSelectStatus.selectedItem.toString(),
                filterByTypePass = filterByTypePass,
                filterByPeriod=filterByPeriod,
                filterBySort = Pair(spinnerSelectSorts.selectedItem.toString(), sort)
            )
            filterCarsPosition.value = FilterCarsPosition(
                filterByStatus = spinnerSelectStatus.selectedItemPosition,
                filterByTypePass = spinnerSelectPass.selectedItemPosition,
                filterByPeriod=spinnerSelectPeriod.selectedItemPosition,
                filterBySort = spinnerSelectSorts.selectedItemPosition
            )
            requireActivity().onBackPressed()
        }
        hideShowView(true)
    }


    override fun onResume() {
        super.onResume()
        contFilter.run {
            hideShowView(false)
        }
    }

    private fun hideShowView(isGone: Boolean) {
        contFilter.isGone = isGone
        btnApplyFilter.isGone = isGone
        btnQuitFilter.isGone = isGone
    }


    @SuppressLint("FragmentLiveDataObserve")
    private fun setupSpinners() {
        val listStatus = arrayListOf(
            "Все",
            "Активный",
            "Не активный (истек или аннулирован)",
            "Нет пропусков"
        )
        val listPass = arrayListOf("Все", "Годовой (БА)", "Разовый (ББ)")
        val listPeriod= arrayListOf("Все","Круглосуточный","День","Ночь")
        val listSort =
            arrayListOf(
                "Проверен",
                "Проверен",
                "Дата окончание",
                "Дата окончание",
                "Осталось дней",
                "Осталось дней",
                "Номер",
                "Номер",
                "Цветовой статус",
                "Цветовой статус",
                "Дата начала",
                "Дата начала",
                "Дата аннулирования",
                "Дата аннулирования",
                "Серия и номер",
                "Серия и номер",
                "Тип пропуска",
                "Тип пропуска"
            )
        spinnerSelectStatus.adapter = SpinnerCardlistAdapter(requireContext(), listStatus)
        spinnerSelectPass.adapter = SpinnerCardlistAdapter(requireContext(), listPass)
        spinnerSelectSorts.adapter = SpinnerCardlistAdapter(requireContext(), listSort)
        spinnerSelectPeriod.adapter=SpinnerCardlistAdapter(requireContext(),listPeriod)
        filterCarsPosition.observe(this, Observer {
            spinnerSelectPeriod.setSelection(it.filterByPeriod)
            spinnerSelectStatus.setSelection(it.filterByStatus)
            spinnerSelectPass.setSelection(it.filterByTypePass)
            spinnerSelectSorts.setSelection(it.filterBySort)
        })
    }


}