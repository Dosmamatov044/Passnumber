package ru.smd.passnumber.ui.checking_pass

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_menu.*
import kotlinx.android.synthetic.main.fragment_checking_pass.*
import kotlinx.android.synthetic.main.fragment_checking_pass.ivTopTruck
import ru.smd.passnumber.R
import ru.smd.passnumber.data.core.Constants
import ru.smd.passnumber.data.core.hideKeyboard
import ru.smd.passnumber.data.entities.PassData
import ru.smd.passnumber.data.entities.PassesData
import ru.smd.passnumber.data.tools.PreferencesHelper
import ru.smd.passnumber.ui.activities.main.MainActivity
import ru.smd.passnumber.ui.help_registration.HelpRegistrationFragment
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import javax.inject.Inject


/**
 * Created by Siddikov Mukhriddin on 7/21/21
 */
@AndroidEntryPoint
class CheckingPassFragment : Fragment(R.layout.fragment_checking_pass) {

    @Inject
    lateinit var prefs: PreferencesHelper

    private val viewModel: CheckingPassViewModel by viewModels()
    private val adapter = PassNumbersAdapter()
    var fromFragment: String? = null
    var regNumberData: String?=null

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mask = MaskImpl.createTerminated(Constants.PHONE_RUS)
        val watcher: FormatWatcher = MaskFormatWatcher(mask)
        watcher.installOn(phone_input)
        rvPassNumbers.adapter = adapter
        if (fromFragment != null) {
            cont_pass.visibility = View.GONE
            llSubscribe.visibility = View.GONE
        } else {
            cont_pass.visibility = View.VISIBLE
        }
        fromFragment = null
        etNameUser.addTextChangedListener {
            chekNamePhone()
        }
        phone_input.addTextChangedListener {
            chekNamePhone()
        }
        btn_back_checking_pass.setOnClickListener {
            hideKeyboard()
            requireActivity().onBackPressed()
        }

        ivTopTruck1.setOnClickListener {
            ivTopTruck1()
        }
        ivTopTruck.setOnClickListener {
            ivTopTruck()
        }
        viewModel.addCarData.observe(this, addCar)
        viewModel.check.observe(this, check)
        viewModel.data.observe(this, passData)
        adapter.btnHelpClicked.observe(this, btnHelpClicked)
    }

    fun ivTopTruck1() {
        ivTopTruck1.isClickable = false
        ivTopTruck1.isGone = true
        ivTopTruck.isClickable = true
        ivTopTruck.isGone = false
        llSubscribe.isGone = prefs.restoreToken() != null
    }

    fun ivTopTruck() {
        ivTopTruck.isClickable = false
        ivTopTruck.isGone = true
        ivTopTruck1.isClickable = true
        ivTopTruck1.isGone = false
        llSubscribe.isGone = true
    }

    fun chekNamePhone() {
        if (!etNameUser.text.isNullOrEmpty() && phone_input.text?.length == 16) {
            btnRegister.isEnabled = true
        } else btnRegister.isEnabled = false
    }

    private val btnHelpClicked = Observer<Boolean> {
        val main = requireActivity() as MainActivity
        main.bottomSelected(main.btnBottom3)
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, HelpRegistrationFragment()).commit()
    }

    override fun onStart() {
        super.onStart()
        regNumberData?.let { viewModel.checkPassData(it) }

    }


    private val addCar = Observer<Boolean> {
        if (it) {
            contAdd.visibility = View.GONE
            Toast.makeText(requireContext(), R.string.success_add_car, Toast.LENGTH_SHORT).show()
        }
    }

    private val check = Observer<Boolean> {
        if (!it) {
            cont_pass.visibility = View.GONE
            contAdd.visibility = View.VISIBLE
            btnAddChekingPass.setOnClickListener {
                viewModel.addCar(
                    viewModel.data.value?.regNumber ?: "",
                    viewModel.data.value?.mark ?: "",
                    viewModel.data.value?.driverName ?: ""
                )
            }
        } else contAdd.isGone
    }

    private val passData = Observer<PassData> {
        btnRegister.setOnClickListener {
            (requireActivity() as MainActivity).openRegistrationFragment(
                phone_input.text.toString(),
                etNameUser.text.toString(),
            )
            prefs.storeCarRegistration(
                viewModel.data.value?.regNumber ?: "",
                viewModel.data.value?.mark ?: "",
                viewModel.data.value?.driverName ?: ""
            )
        }
        if (!prefs.restoreToken().isNullOrEmpty()) {
            regNumberData?.let { viewModel.checkCar(it) }
        }
        adapter.regNumber = it.regNumber ?: ""
        if (it.passes.isNullOrEmpty())
            adapter.submitList(
                mutableListOf(
                    PassesData(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                )
            )
        else
            adapter.submitList(it.passes)
    }


}