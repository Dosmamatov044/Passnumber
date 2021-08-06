package ru.smd.passnumber.ui.account.my_cars.data_car.docs

import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.smd.passnumber.data.entities.Docs
import ru.smd.passnumber.data.service.PassNumberRepo
import ru.smd.passnumber.ui.activities.main.MainActivity
import javax.inject.Inject

class DocsPresenter @Inject constructor(val repo: PassNumberRepo) : DocsContract.Presenter {

    private var view: DocsContract.View? = null

    lateinit var compositeDisposable: CompositeDisposable
    val docsSts= mutableListOf<Docs>()
    val docsPts= mutableListOf<Docs>()
    val docsDk= mutableListOf<Docs>()
    val docsDriverCard= mutableListOf<Docs>()
    val docsPassport= mutableListOf<Docs>()
    val docsCardCompany= mutableListOf<Docs>()
    val docsContractCredit= mutableListOf<Docs>()
    val docsContractCarriage= mutableListOf<Docs>()
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

    fun getDocsType(docs:List<Docs>){
        docsSts.clear()
        docsPts.clear()
        docsDk.clear()
        docsDriverCard.clear()
        docsPassport.clear()
        docsCardCompany.clear()
        docsContractCredit.clear()
        docsContractCarriage.clear()
        docs.forEach {
            when(it.type.toInt()){
                1->docsSts.add(it)
                2->docsPts.add(it)
                3->docsDk.add(it)
                4->docsDriverCard.add(it)
                5->docsPassport.add(it)
                6->docsCardCompany.add(it)
                7->docsContractCredit.add(it)
                8->docsContractCarriage.add(it)
            }
        }
        view?.showDocsSts(docsSts)
        view?.showDocsPts(docsPts)
        view?.showDocsDk(docsDk)
        view?.showDocsDriverCard(docsDriverCard)
        view?.showDocsPassport(docsPassport)
        view?.showDocsCardCompany(docsCardCompany)
        view?.showDocsContractCredit(docsContractCredit)
        view?.showDocsContractCarriage(docsContractCarriage)
    }

   override fun getDocs(idVehicle: Int) {
        repo.getDocs(idVehicle).compose(applySchedulers()).subscribe { response, error ->
            MainActivity.handleLoad.value = false
            when {
                error == null -> {
                    response.data?.let { getDocsType(it) }
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