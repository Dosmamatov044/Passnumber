package ru.smd.passnumber.ui.notification


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.smd.passnumber.databinding.FragmentNotificationBinding
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : Fragment(), NotificationContract.View {

    @Inject
    lateinit var presenter: NotificationContract.Presenter

    lateinit var binding: FragmentNotificationBinding


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
        binding.run {
            btnBackNotification.setOnClickListener { presenter.onClickBack() }
        }
    }

    override fun onBack() {
        requireActivity().onBackPressed()
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