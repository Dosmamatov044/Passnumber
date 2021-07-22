package ru.smd.passnumber.ui.chek_pass_number

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.smd.passnumber.data.entities.PassData
import ru.smd.passnumber.data.service.ApiService
import ru.smd.passnumber.data.service.enqueue


/**
 * Created by Siddikov Mukhriddin on 2/13/21
 */

class CheckPassViewModel @ViewModelInject constructor(private val api: ApiService) : ViewModel() {


    val data = MutableLiveData<PassData>()

    fun checkPassData(number: String) {
        GlobalScope.launch {
            api.checkPassNumber(number).enqueue {
                data.value = it.data
            }
        }
    }
}



