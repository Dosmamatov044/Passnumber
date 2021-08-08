package ru.smd.passnumber.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.smd.passnumber.R
import ru.smd.passnumber.ui.chek_pass_number.CheckPassFragment


/**
 * Created by Siddikov Mukhriddin on 7/21/21
 */
@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {


    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.statusBarColor=ContextCompat.getColor(requireContext(),R.color.black)
        GlobalScope.launch {
            delay(1000)
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, CheckPassFragment())
                .commit()

        }

    }


}