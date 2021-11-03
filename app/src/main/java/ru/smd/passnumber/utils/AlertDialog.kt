package ru.smd.passnumber.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import ru.smd.passnumber.R

fun alertDialog(context: Context) {
    val url = "https://reestr.ovga.mos.ru/"
    val message =  TextView(context);
    val s = SpannableString(url)
    Linkify.addLinks(s, Linkify.WEB_URLS);
    message.text = s
    message.movementMethod = LinkMovementMethod.getInstance();

    val builder:  AlertDialog.Builder= AlertDialog.Builder(context)
    builder.setTitle(
        "Приносим свои извинения, на нашем сайте идут технические работы\n" +
                "\n" +
                "Воспользуйтесь пока официальной формой $message для проверки наличия действующего пропуска перед выходом машины в рейс, а также обратите внимание, чтобы увидеть постоянные пропуска нужно выбирать серию БА.")
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