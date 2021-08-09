package ru.smd.passnumber.utils

import android.content.Intent
import android.net.Uri
import android.widget.TextView
import androidx.fragment.app.Fragment

/**
 * Created by Siddikov Mukhriddin on 7/23/21
 */

fun TextView.callToNumber(){
    this.isClickable=true
    setOnClickListener {
        val number = Uri.parse("tel:$text");
        val callIntent =  Intent(Intent.ACTION_DIAL, number);
        context.startActivity(callIntent)
    }
}
fun Fragment.openLink(url:String){
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse(url)
    startActivity(i)

}
fun Fragment.callToNumber(number:String){
    val number = Uri.parse("tel:$number");
    val callIntent =  Intent(Intent.ACTION_DIAL, number);
    this.startActivity(callIntent)
}
fun Fragment.openMail(mail:String){
    val intent =  Intent(Intent.ACTION_VIEW);
    val data = Uri.parse("mailto:${mail}");
    intent.setData(data);
    this.startActivity(intent)
}

fun Fragment.openMail(mail: String,title:String){
    val intent =  Intent(Intent.ACTION_VIEW);
    val data = Uri.parse("mailto:");
    intent.setData(data);
    val address= Array<String>(1){mail}
    intent.putExtra(Intent.EXTRA_EMAIL, address)
    intent.putExtra(Intent.EXTRA_SUBJECT, title)
    this.startActivity(intent)
//    startActivityForResult(intent,12)
}

fun TextView.openMail(){
    setOnClickListener {
        val intent =  Intent(Intent.ACTION_VIEW);
        val data = Uri.parse("mailto:${text}");
        intent.setData(data);
        this.context.startActivity(intent)
    }
}