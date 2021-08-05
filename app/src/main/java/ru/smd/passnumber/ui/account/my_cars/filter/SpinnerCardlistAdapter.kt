package ru.smd.passnumber.ui.account.my_cars.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import ru.smd.passnumber.R
import java.util.*

/**
 * Created by Siddikov Mukhriddin on 6/25/21
 */

class SpinnerCardlistAdapter(
    context: Context, val cards: List<String>
) : ArrayAdapter<String>(context, 0, cards) {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View =
            convertView ?: layoutInflater.inflate(R.layout.item_select_card, parent, false)
        getItem(position)?.let { card ->
            setItemForCountry(view, card)
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View = layoutInflater.inflate(R.layout.item_select_card_dropdown, parent, false)
        getItem(position)?.let { card ->
            setItemForCountryList(position, view, card)
        }
        return view
    }

    private fun setItemForCountry(view: View, card: String) {
        val tvCountry = view.findViewById<TextView>(R.id.tvSelectCardName)
        val cardNumber = Locale("", card).displayCountry
        tvCountry.text = cardNumber
    }

    private fun setItemForCountryList(position: Int, view: View, card: String) {
        val tvCountry = view.findViewById<TextView>(R.id.tvSelectCardName)
        val cardNumber = Locale("", card).displayCountry
        tvCountry.text = cardNumber
        view.findViewById<LinearLayout>(R.id.bottomView).isGone = position==cards.size-1
    }
    }

