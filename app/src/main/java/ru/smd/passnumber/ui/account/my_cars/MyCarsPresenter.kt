package ru.smd.passnumber.ui.account.my_cars

import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.smd.passnumber.data.service.PassNumberRepo
import javax.inject.Inject

class MyCarsPresenter @Inject constructor(val repo: PassNumberRepo):MyCarsContract.Presenter {

    private var view: MyCarsContract.View?=null

    lateinit var compositeDisposable: CompositeDisposable

    override fun onStart(view: MyCarsContract.View) {
        this.view=view
        compositeDisposable = CompositeDisposable()
        getMyCars()
    }

    override fun onStop() {
        view=null
        compositeDisposable.dispose()
    }

    override fun onClickBack() {
        view?.toBack()
    }

    override fun onClickAdd() {
      view?.showAddCarBlock()
    }

    override fun getMyCars() {
        repo.getCarList().compose(applySchedulers())
            .subscribe { response, error ->
                when {
                    error == null -> {
                     if (!response.data.isEmpty()){
                         view?.showEmptyList()
                     }else{
                         view?.showCarList()
                     }
                    }
                    else -> {
                        view?.showErrorInternet()
                    }
                }
            }.also(compositeDisposable::add)
    }

    override fun addCar(regNumber: String, labelModel: String, nameDriver: String) {
        TODO("Not yet implemented")
    }

    fun <T> applySchedulers(): SingleTransformer<T, T> {
        return SingleTransformer {
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}