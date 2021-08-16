package ru.smd.passnumber.ui.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_menu.*
import kotlinx.android.synthetic.main.fragment_check_pass.*
import ru.smd.passnumber.ui.chek_pass_number.CheckPassFragment
import ru.smd.passnumber.R
import ru.smd.passnumber.data.core.showKeyBoard
import ru.smd.passnumber.data.tools.PreferencesHelper
import ru.smd.passnumber.ui.account.AccountFragment
import ru.smd.passnumber.ui.account.registration.RegistrationFragment
import ru.smd.passnumber.ui.help_registration.HelpRegistrationFragment
import ru.smd.passnumber.ui.splash.SplashFragment
import ru.smd.passnumber.utils.showKeyBoard
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var mainCompositeDisposable: CompositeDisposable
        val handleLoad = MutableLiveData<Boolean>()
        val handleError = MutableLiveData<String>()
    }

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainCompositeDisposable = CompositeDisposable()
        setContentView(R.layout.activity_main)
        handleBottomClick()
        handleLiveData()
        sendPushToken()
    }

    fun handleLiveData() {
        handleLoad.observe(this, Observer {
            progressBarMain.isGone = !it
        })
        handleError.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    fun handleBottomClick() {
        btnBottom1.bottomSelelected()
        supportFragmentManager.beginTransaction().replace(R.id.mainContainer, CheckPassFragment())
            .commit()
        btnBottom1.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, CheckPassFragment())
                .commit()
            it.bottomSelelected()
        }
        btnBottom2.setOnClickListener {
            if (preferencesHelper.restoreToken() == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainContainer, RegistrationFragment()).commit()
                it.bottomSelelected()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainContainer, AccountFragment())
                    .commit()
                it.bottomSelelected()
            }
        }
        btnBottom3.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, HelpRegistrationFragment())
                .commit()
            it.bottomSelelected()
        }

    }

    fun openRegistrationFragment(userNumber: String, userName: String,regNumber:String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, RegistrationFragment().apply {
                userNumberFromCheckPass = userNumber
                userNameFromCheckPass = userName
            }).commit()
        btnBottom2.bottomSelelected()
    }

    fun hideBottomMenu(hide: Boolean = false) {
        lvBottomMenu.isGone = hide
    }

    fun bottomSelected(view: View) {
        view.bottomSelelected()

    }

    fun View.bottomSelelected() {
        btnBottom1.isSelected = false
        btnBottom2.isSelected = false
        btnBottom3.isSelected = false
        btnBottom1.isClickable = true
        btnBottom2.isClickable = true
        btnBottom3.isClickable = true
        this.isSelected = true
        this.isClickable = false

    }

    override fun onDestroy() {
        super.onDestroy()
        mainCompositeDisposable.dispose()
    }

    fun sendPushToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isComplete) {
                val fbToken = it.result.toString()
                Log.e("TTT", "pushToken:$fbToken")
//                firebaseDomain.sendPushToken(fbToken,  Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID))
            }
        }
    }
}