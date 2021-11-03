package ru.smd.passnumber.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import ru.smd.passnumber.R

fun alertDialog(context: Context) {
    val url = "https://reestr.ovga.mos.ru/"
    val builder:  AlertDialog.Builder= AlertDialog.Builder(context)
    builder.setTitle(context. getString(R.string.title_text))
    builder.setMessage(context.getString(R.string.message_text))

    builder.setPositiveButton(context. getString(R.string.positive_text)) { dialog, which -> }
    builder.setNegativeButton(context.getString(R.string.negative_text)) { dialog, which ->

        val query = Uri.encode(url, "UTF-8")
        val browserIntent = Intent(Intent.CATEGORY_BROWSABLE, Uri.parse(Uri.decode(query)))
        browserIntent.action = Intent.ACTION_VIEW
        context.startActivity( browserIntent)
    }
    builder.show()

}