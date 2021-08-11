package ru.smd.passnumber.ui.account

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.alert_view.view.*
import kotlinx.android.synthetic.main.fragment_account.*
import ru.smd.passnumber.R
import ru.smd.passnumber.data.service.PassNumberRepo
import ru.smd.passnumber.data.tools.PreferencesHelper
import ru.smd.passnumber.ui.account.my_cars.MyCarsFragment
import ru.smd.passnumber.ui.account.my_data.MyDataFragment
import ru.smd.passnumber.ui.account.notification.NotificationFragment
import ru.smd.passnumber.ui.account.registration.RegistrationFragment
import ru.smd.passnumber.ui.account.settings_notification.SettingsNotificationFragment
import ru.smd.passnumber.ui.activities.main.MainActivity
import javax.inject.Inject


/**
 * Created by Siddikov Mukhriddin on 7/21/21
 */
@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    @Inject
    lateinit var repo: PassNumberRepo

    lateinit var compositeDisposable: CompositeDisposable

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
        btnSettingsNotification.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, SettingsNotificationFragment())
                .addToBackStack(null).commit()
        }

        btnFeedback.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW);
            val data = Uri.parse("mailto:");
            intent.setData(data);
            val address = Array<String>(1) { getString(R.string.email_supprt) }
            intent.putExtra(Intent.EXTRA_EMAIL, address)
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.text_title_mail))
            startActivityForResult(intent, 12)
        }

        btnShare.setOnClickListener {
            val message = "Скачайте приложение PASS.SU:\n" +
                    "Для iPhone https://apps.apple.com/app/apple-store/id1579017960\n" +
                    "Для Android https://play.google.com/store/apps/details?id=ru.smd.passnumber"
            startActivity(Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }, "Поделиться"))
        }
btnExit.setOnClickListener {
    val builder = AlertDialog.Builder(requireContext())
    builder.setTitle("Вы уверены, что хотите выйти из аккаунта?")
    builder.setPositiveButton("Да") { dialog, id ->
        preferencesHelper.clearCompany()
        preferencesHelper.clearEmail()
        preferencesHelper.clearFio()
        preferencesHelper.clearToken()
        preferencesHelper.clearPhone()
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, RegistrationFragment()).commit()
    }
    builder.setNegativeButton("Нет"){dialog,id->
    }
    builder.show()

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

    override fun onStart() {
        super.onStart()
        compositeDisposable = CompositeDisposable()
        getMyCars()
        getUnreadNotification()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }

    fun getUnreadNotification() {
        MainActivity.handleLoad.postValue(true)
        repo.getUnreadNotifications().compose(applySchedulers()).subscribe { response, error ->
            MainActivity.handleLoad.value = false
            when {
                error == null -> {
                    if (!response.data.isEmpty()) {
                        alertNotifications.visibility = View.VISIBLE
                    } else alertNotifications.visibility = View.GONE
                }
                else -> {

                }
            }
        }.also(compositeDisposable::add)
    }

    fun getMyCars() {
        MainActivity.handleLoad.postValue(true)
        repo.getCarList().compose(applySchedulers())
            .subscribe { response, error ->
                MainActivity.handleLoad.value = false
                when {
                    error == null -> {
                        if (response.data.size > 0) {
                            countCar.visibility = View.VISIBLE
                            countCar.setText(response.data.size.toString())
                        } else countCar.visibility = View.INVISIBLE
                    }
                    else -> {

                    }
                }
            }.also(compositeDisposable::add)
    }

    fun <T> applySchedulers(): SingleTransformer<T, T> {
        return SingleTransformer {
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

}