package ru.smd.passnumber.ui.account.registration

import android.content.Context
import android.os.CountDownTimer
import androidx.core.content.ContextCompat
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import ru.smd.passnumber.R
import ru.smd.passnumber.data.service.PassNumberRepo
import ru.smd.passnumber.data.tools.PreferencesHelper
import ru.smd.passnumber.ui.activities.main.MainActivity
import ru.smd.passnumber.ui.activities.main.MainActivity.Companion.mainCompositeDisposable
import java.lang.Exception
import javax.inject.Inject

class RegistrationPresenter @Inject constructor(
    val repo: PassNumberRepo,
    val preferencesHelper: PreferencesHelper
) :
    RegistrationContract.Presenter {

    private var view: RegistrationContract.View? = null

    private var phone: String? = null
//    lateinit var compositeDisposable: CompositeDisposable

    override fun onStart(view: RegistrationContract.View) {
        this.view = view
//        compositeDisposable = CompositeDisposable()
    }

    override fun onStop() {
        view = null
        timer.onFinish()
//        compositeDisposable.dispose()
    }


    override fun startTimer() {
        timer.start()
    }

    override fun onClickEnter(
        androidId: String,
        code: String,
        userNameFromCheckPass: String,
        context: Context
    ) {
        MainActivity.handleLoad.postValue(true)
        repo.registration(
            mutableMapOf<String, String>().apply {
                if (userNameFromCheckPass.isNotEmpty())
                    this["name"] = userNameFromCheckPass
                this["phone"] = phone.toString()
                this["code"] = code
                this["device_name"] = androidId
            }
        ).compose(applySchedulers())
            .subscribe { response, error ->
                MainActivity.handleLoad.value = false
                when {
                    error == null -> {
                        preferencesHelper.storeToken(response.api_token)
                        preferencesHelper.storePhone(response.data.phone)
                        preferencesHelper.storeFio(response.data.name)
                        preferencesHelper.storeEmail(response.data.email)
                        preferencesHelper.storeCompany(response.data.client.name)
                        view?.showAccountFragment()
                        view?.exit()
                    }
                    else -> {
                        if (error.toString()
                                .contains("Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 1 path")
                        ) {
                            view?.showErrorMessage(context.getString(R.string.fail_code))
                        }
                    }
                }
            }.also(mainCompositeDisposable::add)

    }

    override fun sendSms(phone: String) {
        if (this.phone == null) {
            val newPhone = phone.replace(regex = Regex("\\D"), "")
            this.phone = newPhone
        }
        repo.getCode(this.phone!!).compose(applySchedulers())
            .subscribe { response, error ->
                when {
                    error == null -> {
                        view?.showBlock2()
                    }
                    error is HttpException -> {
                        MainActivity.handleError.value = error.toString()
                    }
                    else -> {
                        MainActivity.handleError.value = error.toString()
                    }
                }
            }.also(mainCompositeDisposable::add)
    }

    fun <T> applySchedulers(): SingleTransformer<T, T> {
        return SingleTransformer {
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun handleResponseError(error: HttpException, block: () -> Unit) {
        try {
            val errorBody = error.response().errorBody()
            val jObjError = JSONObject(errorBody?.string())
            val ks = jObjError.getString("message")
            view?.showErrorMessage(ks)
        } catch (e: Exception) {
            block()
        }
    }


    private val timer = object : CountDownTimer(300000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            view?.showTimer(
                millisUntilFinished / 1000
            )
        }

        override fun onFinish() {
            view?.activateButtons()
        }
    }

}