package ru.smd.passnumber.ui.feedback

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_feedback.*
import ru.smd.passnumber.R
import ru.smd.passnumber.data.core.Constants
import ru.smd.passnumber.ui.activities.main.MainActivity
import ru.smd.passnumber.utils.showToast
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher


/**
 * Created by Siddikov Mukhriddin on 7/21/21
 */
@AndroidEntryPoint
class FeedbackFragment : Fragment(R.layout.fragment_feedback) {

    private val viewModel: FeedbackViewModel by viewModels()
    var setUserNumber=""

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPhone()
        handleBack()
        btnSendFeedback.setOnClickListener {
            viewModel.sendFeedback(edtPhone.text.toString(),edtFeedback.text.toString())
        }
        btnBackFeedback.setOnClickListener {
            moveBack()
        }
        viewModel.isFeedbackSend.observe(this, Observer {
            if (it){
                requireActivity().showToast("Отправлено")
                moveBack()
            }
        })
    }

    @SuppressLint("FragmentBackPressedCallback")
    private fun handleBack() {
        val callback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                this.isEnabled = false
                remove()
                moveBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun moveBack() {
        (requireActivity() as MainActivity).hideBottomMenu()
        requireActivity().onBackPressed()
    }

    private fun setupPhone() {
        val mask = MaskImpl.createTerminated(Constants.PHONE_RUS)
        val watcher: FormatWatcher = MaskFormatWatcher(mask)
        watcher.installOn(edtPhone)
        edtPhone.setText(setUserNumber)
    }


}