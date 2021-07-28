package ru.smd.passnumber.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_menu.*
import ru.smd.passnumber.ui.chek_pass_number.CheckPassFragment
import ru.smd.passnumber.R
import ru.smd.passnumber.ui.account.AccountFragment
import ru.smd.passnumber.ui.help_registration.HelpRegistrationFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        val handleLoad = MutableLiveData<Boolean>()
        val handleError = MutableLiveData<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleBottomClick()
        handleLiveData()
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
            supportFragmentManager.beginTransaction().replace(R.id.mainContainer, CheckPassFragment())
                .commit()
            it.bottomSelelected()
        }
        btnBottom2.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.mainContainer, AccountFragment())
                .commit()
            it.bottomSelelected()

        }
        btnBottom3.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.mainContainer, HelpRegistrationFragment())
                .commit()
            it.bottomSelelected()
        }

    }
    fun bottomSelected(view: View){
        view.bottomSelelected()

    }
    fun View.bottomSelelected(){
        btnBottom1.isSelected = false
        btnBottom2.isSelected = false
        btnBottom3.isSelected = false
        btnBottom1.isClickable = true
        btnBottom2.isClickable = true
        btnBottom3.isClickable = true
        this.isSelected=true
        this.isClickable=false

    }
}