package ru.smd.passnumber.ui.checking_pass

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.smd.passnumber.data.entities.PassData
import ru.smd.passnumber.data.service.ApiService
import ru.smd.passnumber.data.service.enqueue
import ru.smd.passnumber.ui.activities.main.MainActivity.Companion.handleLoad


/**
 * Created by Siddikov Mukhriddin on 2/13/21
 */

class CheckingPassViewModel @ViewModelInject constructor(private val api: ApiService) : ViewModel() {


    val data = MutableLiveData<PassData>()

    fun checkPassData(number:String){
        viewModelScope.launch {
            handleLoad.value=true
            api.checkPassNumber("М579ус750").enqueue{
                data.value= it.data
            }
        }
    }
}



