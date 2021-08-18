package ru.smd.passnumber.ui.account.notification


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.smd.passnumber.R
import ru.smd.passnumber.data.entities.Notification
import ru.smd.passnumber.databinding.FragmentNotificationBinding
import ru.smd.passnumber.ui.account.my_cars.data_car.DataCarFragment
import ru.smd.passnumber.ui.account.notification.adapters.NotificationAdapter
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : Fragment(), NotificationContract.View,
    NotificationAdapter.OnReadListner {

    @Inject
    lateinit var presenter: NotificationContract.Presenter

    lateinit var binding: FragmentNotificationBinding

    lateinit var adapter: NotificationAdapter

    private var idVehicle:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentNotificationBinding.inflate(inflater).run {
        binding = this
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NotificationAdapter(this)
        binding.run {
            recycleNotification.adapter = adapter
            btnBackMyNotification.setOnClickListener { presenter.onClickBack() }
            if (idVehicle==0){
                contFilterNotification.setOnClickListener {
                    if (txtFilter.text.contains(getString(R.string.all))) {
                        txtFilter.setText(getString(R.string.unread))
                        presenter.getUnreadNotifications()
                    } else {
                        txtFilter.setText(getString(R.string.all))
                        presenter.getNotifications()
                    }
                }
            }else contFilterNotification.visibility=View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idVehicle = arguments?.getInt("id_vehicle_for_notification")?:0
    }
    override fun onBack() {
        requireActivity().onBackPressed()
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorInternet() {
        Toast.makeText(requireContext(), R.string.hasntInternet, Toast.LENGTH_SHORT).show()
    }

    override fun showNotifications(notifications: List<Notification>) {
        adapter.setData(notifications)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart(this)
        if (idVehicle==0){
            presenter.getNotifications()
        }else presenter.getNotificationsForCar(idVehicle)
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
        idVehicle=0
    }

    override fun readNotification(notification: Notification) {
        if (idVehicle==0){
            presenter.sendReadNotifications(notification)
        }
    }

    companion object {
        fun create(id: Int) = NotificationFragment().apply {
            arguments = Bundle().apply {
                putInt("id_vehicle_for_notification", id)
            }
        }
    }

}