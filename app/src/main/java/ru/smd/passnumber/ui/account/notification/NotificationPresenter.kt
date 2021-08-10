package ru.smd.passnumber.ui.account.notification

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import ru.smd.passnumber.data.entities.Notification
import ru.smd.passnumber.data.service.PassNumberRepo
import ru.smd.passnumber.ui.activities.main.MainActivity
import java.lang.Exception
import javax.inject.Inject

class NotificationPresenter @Inject constructor(val repo: PassNumberRepo) :
    NotificationContract.Presenter {

    private var view: NotificationContract.View? = null

    lateinit var compositeDisposable: CompositeDisposable

    override fun onStart(view: NotificationContract.View) {
        this.view = view
        compositeDisposable = CompositeDisposable()
        getNotifications()
    }

    override fun onStop() {
        view = null
        compositeDisposable.dispose()
    }

    override fun onClickBack() {
        view?.onBack()
    }

    override fun sendReadNotifications(notifications: Notification) {
        repo.readNotification(notifications.id).compose(applySchedulers()).subscribe {
                response, error ->
            when {
                error == null -> {
                 toString()
                }
                else -> {
                    MainActivity.handleError.value = error.toString()
                }
            }
        }.also(compositeDisposable::add)
    }

    fun getNotifications() {
        repo.getNotifications().compose(applySchedulers()).subscribe { response, error ->
            when {
                error == null -> {
                    view?.showNotifications(response.data)
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