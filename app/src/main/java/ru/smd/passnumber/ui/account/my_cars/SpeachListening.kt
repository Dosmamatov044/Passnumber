package ru.smd.passnumber.ui.account.my_cars

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.EditText
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.smd.passnumber.R
import ru.smd.passnumber.ui.activities.main.MainActivity
import java.util.*

/**
 * Created by Siddikov Mukhriddin on 8/5/21
 */

val RecordAudioRequestCode: Int = 16

fun getSpeechRecognizerIntent(): Intent {
    return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
    }

}

fun checkResultPermission(requestCode: Int, grantResults: IntArray) {
    if (requestCode == RecordAudioRequestCode && grantResults.isNotEmpty()) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            MainActivity.handleError.value = "Permission Granted"

        }
    }
}

fun Fragment.checkPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.RECORD_AUDIO),
            RecordAudioRequestCode
        )
    }
}

fun Fragment.recognizeSpeech(microphone: ImageView, speechRecognizer: SpeechRecognizer) {
    if (activity != null) {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermission()
        } else {
            microphone.setImageResource(R.drawable.ic_microphone_red)
            speechRecognizer.startListening(getSpeechRecognizerIntent())
        }
    }
}

fun addRecognitionListener(microphone: ImageView, editText: EditText): RecognitionListener {
    return object : RecognitionListener {
        override fun onReadyForSpeech(p0: Bundle?) {
        }

        override fun onBeginningOfSpeech() {
            microphone.setImageResource(R.drawable.ic_microphone_red)
            MainActivity.handleError.value = "Говорите... "
        }

        override fun onRmsChanged(p0: Float) {
        }

        override fun onBufferReceived(p0: ByteArray?) {
        }

        override fun onEndOfSpeech() {
        }

        override fun onError(p0: Int) {
            p0.toString()
            microphone.setImageResource(R.drawable.ic_microphone)
        }

        override fun onResults(bundle: Bundle?) {
            microphone.setImageResource(R.drawable.ic_microphone)
            val data = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            MainActivity.handleError.value = data?.get(0) ?: "none"
            editText.setText(data?.get(0) ?: "")
        }

        override fun onPartialResults(p0: Bundle?) {

        }

        override fun onEvent(p0: Int, p1: Bundle?) {

        }
    }
}