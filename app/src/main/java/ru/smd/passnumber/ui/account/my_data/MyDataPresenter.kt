package ru.smd.passnumber.ui.account.my_data

import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import ru.smd.passnumber.R
import ru.smd.passnumber.data.service.PassNumberRepo
import ru.smd.passnumber.data.tools.PreferencesHelper
import ru.smd.passnumber.ui.activities.main.MainActivity
import java.lang.Exception
import javax.inject.Inject

class MyDataPresenter @Inject constructor(
    val repo: PassNumberRepo,
    val preferencesHelper: PreferencesHelper
) : MyDataContract.Presenter {

    private var view: MyDataContract.View? = null

    lateinit var compositeDisposable: CompositeDisposable

    override fun onStart(view: MyDataContract.View) {
        this.view = view
        compositeDisposable = CompositeDisposable()
        restoreData()
    }

    override fun onStop() {
        view = null
        compositeDisposable.dispose()
    }

    override fun onClickBack() {
        view?.toBack()
    }

    fun restoreData(){
        MainActivity.handleLoad.postValue(true)
        repo.getUser().compose(applySchedulers())
            .subscribe { response, error ->
                MainActivity.handleLoad.value = false
                when {
                    error == null -> {
                        preferencesHelper.storePhone(response.data.phone)
                        preferencesHelper.storeFio(response.data.name)
                        preferencesHelper.storeEmail(response.data.email)
                        preferencesHelper.storeCompany(response.data.client.name)
                        getData()
                    }
                    error is HttpException -> {
                        handleResponseError(error) {
                        }
                    }
                    else -> {

                        view?.showErrorInternet()
                    }
                }
            }.also(compositeDisposable::add)
    }

    fun getData(){
        view?.showMyData(preferencesHelper.restoreFio(),preferencesHelper.restorePhone(),preferencesHelper.restoreEmail(),preferencesHelper.restoreCompany())
    }

    override fun onClickSave(fio: String, text: String, email: String, company: String,context: Context) {
        val phone = text.replace(regex = Regex("\\D"), "")
        MainActivity.handleLoad.postValue(true)
        repo.saveDataUser(
            mutableMapOf<String, String>().apply {
                this["name"] = fio
                this["phone"] = phone
                this["email"] = email
                this["client_name"] = company
            }
        ).compose(applySchedulers())
            .subscribe { response, error ->
                MainActivity.handleLoad.value=false
                when {
                    error == null -> {
                        preferencesHelper.storePhone(text)
                        preferencesHelper.storeFio(fio)
                        preferencesHelper.storeEmail(email)
                        preferencesHelper.storeCompany(company)
                        view?.exit()
                        view?.showAcountFragment()
                    }
                    else -> {
                       if (error.toString().contains("to accept malformed JSON at line 1 column 1 path $")){
                           view?.showErrorMessage(context.getString(R.string.email_allready_registration))
                       }else MainActivity.handleError.value = error.toString()
                    }
                }
            }.also(compositeDisposable::add)
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