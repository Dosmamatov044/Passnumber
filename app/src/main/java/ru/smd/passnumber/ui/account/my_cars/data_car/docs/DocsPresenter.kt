package ru.smd.passnumber.ui.account.my_cars.data_car.docs

import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.smd.passnumber.data.service.PassNumberRepo
import ru.smd.passnumber.ui.activities.main.MainActivity
import javax.inject.Inject

class DocsPresenter @Inject constructor(val repo: PassNumberRepo) : DocsContract.Presenter {

    private var view: DocsContract.View? = null

    lateinit var compositeDisposable: CompositeDisposable

    override fun onStart(view: DocsContract.View) {
        this.view = view
        compositeDisposable = CompositeDisposable()
    }

    override fun onStop() {
        view = null
        compositeDisposable.dispose()
    }

    override fun onClickBack() {
        view?.toBack()
    }

   override fun getDocs(idVehicle: Int) {
        repo.getDocs(idVehicle).compose(applySchedulers()).subscribe { response, error ->
            MainActivity.handleLoad.value = false
            when {
                error == null -> {
                    response.data?.let { view?.showDocs(it) }
                }
                else -> {
                    MainActivity.handleError.value = error.toString()
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

}