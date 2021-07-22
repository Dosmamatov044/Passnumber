package ru.smd.passnumber.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_menu.*
import ru.smd.passnumber.ui.chek_pass_number.CheckPassFragment
import ru.smd.passnumber.R

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
        btnBottom1.isSelected = true
        supportFragmentManager.beginTransaction().replace(R.id.mainContainer, CheckPassFragment())
            .commit()
        btnBottom1.setOnClickListener {
            btnBottom1.isSelected = true
            btnBottom2.isSelected = false
            btnBottom3.isSelected = false
        }
        btnBottom2.setOnClickListener {
            btnBottom1.isSelected = false
            btnBottom2.isSelected = true
            btnBottom3.isSelected = false
        }
        btnBottom3.setOnClickListener {
            btnBottom1.isSelected = false
            btnBottom2.isSelected = false
            btnBottom3.isSelected = true
        }

    }
}