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
import ru.smd.passnumber.ui.account.notification.adapters.NotificationAdapter
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : Fragment(), NotificationContract.View {

    @Inject
    lateinit var presenter: NotificationContract.Presenter

    lateinit var binding: FragmentNotificationBinding

    lateinit var adapter: NotificationAdapter

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
        adapter = NotificationAdapter()
        binding.run {
            btnBackMyNotification.setOnClickListener { presenter.onClickBack() }
        }
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
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

}