package ru.smd.passnumber.ui.account.settings_notification

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings_notification.*
import ru.smd.passnumber.R
import ru.smd.passnumber.data.entities.ResponseCheckCode


/**
 * Created by Siddikov Mukhriddin on 7/21/21
 */
@AndroidEntryPoint
class SettingsNotificationFragment : Fragment(R.layout.fragment_settings_notification) {

    private val viewModel: SettingsNotificationViewModel by viewModels()


    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadData()
        super.onViewCreated(view, savedInstanceState)
        handleClick()
        viewModel.user.observe(this, userData)
    }

    val userData = Observer<ResponseCheckCode> {

        switchTop.isSelected = it.notifications_email
        switchBottom.isSelected = it.notifications_push
        timeNotification.text = it.notification_time?.replace(":", " : ")
    }

    fun handleClick() {
        btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        switchTop.setOnClickListener {
            switchTop.isSelected = !switchTop.isSelected
            saveNotification()
        }

        switchBottom.setOnClickListener {
            switchBottom.isSelected = !switchBottom.isSelected
            saveNotification()

        }
        timeNotification.setTimePicker()
    }

    private fun saveNotification() {
        viewModel.saveNotifications(
            timeNotification.text.toString().replace(" ", ""),
            switchTop.isSelected,
            switchBottom.isSelected
        )
    }

    fun TextView.setTimePicker() {
        setOnClickListener {
            val time = text.toString().replace(" ", "")
            val hour = time.substringBefore(":").toInt()
            val minute = time.substringAfter(":").toInt()
            TimePickerDialog(requireActivity(), { view, hourOfDay, minute ->
                val hourTime = if (hourOfDay > 9) hourOfDay.toString() else "0$hourOfDay"
                val minuteTime = if (minute > 9) minute.toString() else "0$minute"
                text = "$hourTime : $minuteTime"
                saveNotification()
            }, hour, minute, true).show()
        }
    }

}