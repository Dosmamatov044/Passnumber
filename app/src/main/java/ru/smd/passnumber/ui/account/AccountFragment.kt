package ru.smd.passnumber.ui.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.alert_view.view.*
import kotlinx.android.synthetic.main.fragment_account.*
import ru.smd.passnumber.R
import ru.smd.passnumber.data.tools.PreferencesHelper
import ru.smd.passnumber.ui.account.my_cars.MyCarsFragment
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
        btn_my_cars_profile.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.mainContainer, MyCarsFragment())
                .addToBackStack(null).commit()
        }
        setAlert()
        tvCompany.setText(preferencesHelper.restoreCompany())
        tvPhone.setText(preferencesHelper.restorePhone())
        tvFio.setText(preferencesHelper.restoreFio())
    }

    private fun setAlert() {
        singleAlertView.isGone = true
        if (preferencesHelper.restoreEmail().isNullOrEmpty()) {
            singleAlertView.isGone = false
            singleAlertView.tvAlertTitle.text = "Важно!!!"
            singleAlertView.btnAlert.text = "Заполнить"
            singleAlertView.tvAlertDesc.text =
                "Заполните электронную почту в\nпрофиле, чтобы быть с нами на связи"
            singleAlertView.btnAlert.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.mainContainer, MyDataFragment())
                    .addToBackStack(null).commit()
            }
        } else
            if (preferencesHelper.restoreCompany().isNullOrEmpty()) {
                singleAlertView.isGone = false
                singleAlertView.btnAlertEnd.isGone = false
                singleAlertView.tvAlertTitle.text = "Важно!!!"
                singleAlertView.btnAlert.text = "Добавить"
                singleAlertView.tvAlertDesc.text = "Добавьте имя компании в профиле\nпользователя"
                singleAlertView.btnAlert.setOnClickListener {
                    singleAlertView.isGone = true
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.mainContainer, MyDataFragment())
                        .addToBackStack(null).commit()
                }
                singleAlertView.btnAlertEnd.setOnClickListener {
                    singleAlertView.isGone = true
                }

            }
    }

}