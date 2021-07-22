package ru.smd.passnumber.ui.chek_pass_number

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_check_pass.*
import ru.smd.passnumber.R
import ru.smd.passnumber.data.entities.PassData
import ru.smd.passnumber.ui.checking_pass.CheckingPassFragment


/**
 * Created by Siddikov Mukhriddin on 7/21/21
 */
@AndroidEntryPoint
class CheckPassFragment : Fragment(R.layout.fragment_check_pass) {

    private val viewModel: CheckPassViewModel by viewModels()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnCheckPassNumber.setOnClickListener {
            viewModel.checkPassData(etStartNumber.text.toString() + etEndNumber.text.toString())
        }
        viewModel.data.observe(this,passData)

    }
    private val passData= Observer<PassData> {
        parentFragmentManager.beginTransaction().replace(R.id.mainContainer,CheckingPassFragment().apply {
            data.value=it
        }).addToBackStack(null).commit()
    }


}