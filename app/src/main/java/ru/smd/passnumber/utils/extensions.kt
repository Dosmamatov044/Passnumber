package ru.smd.passnumber.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.text.style.UnderlineSpan
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.text.SimpleDateFormat
import java.util.*

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
   Toast.makeText(this, message, duration).show()
}

fun Context.showKeyBoard(editText: EditText) {
   val im: InputMethodManager by lazy { getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager }
   editText.requestFocus()
   im.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
}

fun Context.appInstalledOrNot(uri: String): Boolean {
   val pm: PackageManager = packageManager
   try {
      pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
      return true
   } catch (e: PackageManager.NameNotFoundException) {
   }
   return false
}


fun View.showKeyBoard(editText: EditText) {
   val im: InputMethodManager by lazy { context?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager }
   editText.requestFocus()
   im.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
   if (editText.text.isNotEmpty()) {
      editText.setSelection(editText.text.length)
   }

}

fun View.hideKeyboard(view: View) {
   val inputMethodManager =
      context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
   inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun FragmentActivity.changeFragment(
   containerId: Int,
   fragment: Fragment,
   addBackStack: Boolean = false,
   args: Bundle? = null
) {
   val transaction = supportFragmentManager.beginTransaction()
   if (addBackStack) {
      transaction.replace(containerId, fragment)
         .addToBackStack(fragment.javaClass.name)
   } else {
      transaction.replace(containerId, fragment)
   }
   if (args != null) {
      fragment.arguments = args
   }

   transaction.commit()
}

fun FragmentActivity.changeFragment(
   containerId: Int,
   fragment: Fragment,
   addBackStack: Boolean = false
) {
   val transaction = supportFragmentManager.beginTransaction()
   if (addBackStack) {
      transaction.replace(containerId, fragment)
         .addToBackStack(fragment.javaClass.name)
   } else {
      transaction.replace(containerId, fragment)
         .addToBackStack(null)
   }
   transaction.commit()
}

fun String.doUnderLine(): Spannable {
   val spannableString = SpannableString(this)
   spannableString.setSpan(UnderlineSpan(), 0, this.length, 0)
   return spannableString
}

fun String.doStrike(): Spannable {
   val spannableString = SpannableString(this)
   spannableString.setSpan(StrikethroughSpan(), 0, this.length, 0)
   return spannableString
}

fun View.showIf(visible: Boolean) {
   visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.margin(
   left: Float? = null,
   top: Float? = null,
   right: Float? = null,
   bottom: Float? = null
) {
   layoutParams<ViewGroup.MarginLayoutParams> {
      left?.run { leftMargin = dpToPx(this) }
      top?.run { topMargin = dpToPx(this) }
      right?.run { rightMargin = dpToPx(this) }
      bottom?.run { bottomMargin = dpToPx(this) }
   }
}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
   if (layoutParams is T) block(layoutParams as T)
}

fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)

fun View.PxTodp(dp: Float): Int = context.PxToDp(dp)

fun Context.dpToPx(dp: Float): Int =
   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

fun Context.PxToDp(dp: Float): Int =
   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, resources.displayMetrics).toInt()

fun EditText.actionDone(callback: (() -> Unit)? = null) {
   val action = EditorInfo.IME_ACTION_DONE
//    this.multilineIme(action)
   this.imeOptions = action
   setOnEditorActionListener { _, actionId, _ ->
      if (action == actionId) {
         this.hideKeyboard(this)
         callback?.invoke()
         true
      }
      false
   }
}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
   val formatter = SimpleDateFormat(format, locale)
   return formatter.format(this)
}

fun Date.getHour(format: String, locale: Locale = Locale.getDefault()): String {
   val formatter = SimpleDateFormat(format, locale)
   return formatter.format(this)
}