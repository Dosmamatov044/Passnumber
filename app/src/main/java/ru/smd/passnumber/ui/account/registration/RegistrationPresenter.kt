package ru.smd.passnumber.ui.account.registration

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import ru.smd.passnumber.data.service.PassNumberRepo
import ru.smd.passnumber.data.tools.PreferencesHelper
import ru.smd.passnumber.ui.activities.main.MainActivity.Companion.mainCompositeDisposable
import java.lang.Exception
import javax.inject.Inject

class RegistrationPresenter @Inject constructor(
    val repo:PassNumberRepo,
    val preferencesHelper: PreferencesHelper
) :
    RegistrationContract.Presenter {

    private var view: RegistrationContract.View? = null

//    lateinit var compositeDisposable: CompositeDisposable

    override fun onStart(view: RegistrationContract.View) {
        this.view = view
//        compositeDisposable = CompositeDisposable()
    }

    override fun onStop() {
        view = null
//        compositeDisposable.dispose()
    }

    override fun onClickEnter(
        androidId: String,
        code: String,
        text: String,
        userNameFromCheckPass: String
    ) {
        val phone = text.replace(regex = Regex("\\D"), "")
        repo.registration(
            mutableMapOf<String, String>().apply {
                if (userNameFromCheckPass.isNotEmpty())
                this["name"] = userNameFromCheckPass
                this["phone"] = phone
                this["code"] = code
                this["device_name"] = androidId
            }
        ).compose(applySchedulers())
            .subscribe { response, error ->
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
                    error is HttpException -> {
                        handleResponseError(error) {
                        }
                    }
                    else -> {
                        view?.showErrorInternet()
                    }
                }
            }.also(mainCompositeDisposable::add)

    }

    override fun sendSms(text: String) {
        val phone = text.replace(regex = Regex("\\D"), "")
        repo.getCode(phone).compose(applySchedulers())
            .subscribe { response, error ->
                when {
                    error == null -> {

                    }
                    error is HttpException -> {
                        handleResponseError(error) {
                        }
                    }
                    else -> {
                        view?.showErrorInternet()
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

}