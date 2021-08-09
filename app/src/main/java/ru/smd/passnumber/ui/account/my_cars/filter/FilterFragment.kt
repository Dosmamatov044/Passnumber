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

        btnQuitFilter.setOnClickListener {
            filterCars.value = FilterCars(false)
            filterCarsPosition.value = FilterCarsPosition()
            requireActivity().onBackPressed()
        }
        btnApplyFilter.setOnClickListener {
            val filterByTypePass= when {
                spinnerSelectPass.selectedItem.toString().contains("ББ") -> "ББ"
                spinnerSelectPass.selectedItem.toString().contains("БА") -> "БА"
                else -> ""
            }
            val sort=spinnerSelectSorts.selectedItemPosition % 2 == 0
            filterCars.value = FilterCars(
                true,
               filterByStatus =  spinnerSelectStatus.selectedItem.toString(),
               filterByTypePass =  filterByTypePass,
               filterBySort =  Pair(spinnerSelectSorts.selectedItem.toString(),sort)
            )
            filterCarsPosition.value = FilterCarsPosition(
                filterByStatus =  spinnerSelectStatus.selectedItemPosition,
                filterByTypePass =  spinnerSelectPass.selectedItemPosition,
                filterBySort =  spinnerSelectSorts.selectedItemPosition)
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
            "Действует",
            "Не действует",
            "Действует более 14 дней",
            "Осталось 0-14 дней",
            "Начинается завтра",
            "Закончивается сегодня",
            "Закончивается завтра",
            "Закончился",
            "Аннулирован",
            "Аннулирован сегодня",
            "Не найден"
        )
        val listPass = arrayListOf("Все", "Постоянный (БА)", "Разовый (ББ)")
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
        filterCarsPosition.observe(this, Observer {
            spinnerSelectStatus.setSelection(it.filterByStatus)
            spinnerSelectPass.setSelection(it.filterByTypePass)
            spinnerSelectSorts.setSelection(it.filterBySort)
        })
    }


}