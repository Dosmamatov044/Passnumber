package ru.smd.passnumber.ui.help_registration

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_help_registration.*
import ru.smd.passnumber.R
import ru.smd.passnumber.utils.callToNumber
import ru.smd.passnumber.utils.openLink
import ru.smd.passnumber.utils.openMail


/**
 * Created by Siddikov Mukhriddin on 7/21/21
 */
@AndroidEntryPoint
class HelpRegistrationFragment : Fragment(R.layout.fragment_help_registration) {


    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvCall.callToNumber()
        tvMail.openMail()
        tvUrl.setOnClickListener {
            openLink("http://www.pass.su")
        }
        btnCall.setOnClickListener {
            callToNumber(tvCall.text.toString())
        }
        btnMail.setOnClickListener {
            openMail(tvMail.text.toString())
        }

    }

}