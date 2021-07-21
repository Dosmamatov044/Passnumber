package ru.smd.passnumber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.bottom_menu.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleBottomClick()
    }

    fun handleBottomClick() {
        btnBottom1.isSelected = true
        supportFragmentManager.beginTransaction().replace(R.id.mainContainer,CheckPassFragment()).commit()
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