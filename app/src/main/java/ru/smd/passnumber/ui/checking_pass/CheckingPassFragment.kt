package ru.smd.passnumber.ui.checking_pass

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_checking_pass.*
import ru.smd.passnumber.R
import ru.smd.passnumber.data.entities.PassData


/**
 * Created by Siddikov Mukhriddin on 7/21/21
 */
@AndroidEntryPoint
class CheckingPassFragment : Fragment(R.layout.fragment_checking_pass) {

    private val viewModel: CheckingPassViewModel by viewModels()
    private val adapter=PassNumbersAdapter()
    val data = MutableLiveData<PassData>()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPassNumbers.adapter=adapter
        ivTopTruck1.setOnClickListener {
            ivTopTruck1.isClickable=false
            ivTopTruck1.isGone=true
            ivTopTruck.isClickable=true
            ivTopTruck.isGone=false
            llSubscribe.isGone=false
        }
        ivTopTruck.setOnClickListener {
            ivTopTruck.isClickable=false
            ivTopTruck.isGone=true
            ivTopTruck1.isClickable=true
            ivTopTruck1.isGone=false
            llSubscribe.isGone=true
        }

        data.observe(this,passData)
    }
    private val passData= Observer<PassData> {
        adapter.regNumber=it.reg_number?:""
       adapter.submitList(it.passes)
    }


}