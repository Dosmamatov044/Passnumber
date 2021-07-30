package ru.smd.passnumber.ui.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_account.*
import ru.smd.passnumber.R
import ru.smd.passnumber.data.tools.PreferencesHelper
import ru.smd.passnumber.ui.account.my_data.MyDataFragment
import ru.smd.passnumber.ui.account.notification.NotificationFragment
import javax.inject.Inject


/**
 * Created by Siddikov Mukhriddin on 7/21/21
 */
@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_notification.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, NotificationFragment()).addToBackStack(null).commit()
        }
        btn_profile_my_data.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.mainContainer, MyDataFragment())
                .addToBackStack(null).commit()
        }
        tvCompany.setText(preferencesHelper.restoreCompany())
        tvPhone.setText(preferencesHelper.restorePhone())
        tvFio.setText(preferencesHelper.restoreFio())
    }

}