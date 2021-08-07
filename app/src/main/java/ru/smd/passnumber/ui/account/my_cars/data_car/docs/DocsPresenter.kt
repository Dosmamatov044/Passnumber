package ru.smd.passnumber.ui.account.my_cars.data_car.docs

import android.content.Context
import android.graphics.Bitmap
import id.zelory.compressor.Compressor
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.smd.passnumber.data.entities.Docs
import ru.smd.passnumber.data.service.PassNumberRepo
import ru.smd.passnumber.ui.activities.main.MainActivity
import ru.smd.passnumber.ui.activities.main.MainActivity.Companion.compositeDisposableMain
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DocsPresenter @Inject constructor(val repo: PassNumberRepo) : DocsContract.Presenter {

    private var view: DocsContract.View? = null

    var type=0
    var idVehicle=0
    lateinit var compositeDisposable: CompositeDisposable
    lateinit var compressedFile: File
    val docsSts = mutableListOf<Docs>()
    val docsPts = mutableListOf<Docs>()
    val docsDk = mutableListOf<Docs>()
    val docsDriverCard = mutableListOf<Docs>()
    val docsPassport = mutableListOf<Docs>()
    val docsCardCompany = mutableListOf<Docs>()
    val docsContractCredit = mutableListOf<Docs>()
    val docsContractCarriage = mutableListOf<Docs>()
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

    fun getDocsType(docs: List<Docs>) {
        docsSts.clear()
        docsPts.clear()
        docsDk.clear()
        docsDriverCard.clear()
        docsPassport.clear()
        docsCardCompany.clear()
        docsContractCredit.clear()
        docsContractCarriage.clear()
        docs.forEach {
            when (it.type.toInt()) {
                1 -> docsSts.add(it)
                2 -> docsPts.add(it)
                3 -> docsDk.add(it)
                4 -> docsDriverCard.add(it)
                5 -> docsPassport.add(it)
                6 -> docsCardCompany.add(it)
                7 -> docsContractCredit.add(it)
                8 -> docsContractCarriage.add(it)
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
        }.also(compositeDisposableMain::add)
    }

    fun toRequestBody(value: String): RequestBody {
        val body=RequestBody.create("text/plain".toMediaTypeOrNull(), value)
        return body
    }

    override fun addPhoto(bitmap: Bitmap, file: File, context: Context) {
        compressedFile = Compressor(context).compressToFile(file)
        val reqFile = compressedFile.asRequestBody("image/*".toMediaTypeOrNull())
        val part: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", "file.png", reqFile)
        val map= mutableMapOf<String,RequestBody>()
        map.put("type_id",toRequestBody(type.toString()))
        repo.storeDocs(idVehicle, part, map).compose(applySchedulers())
            .subscribe { response, error ->
                MainActivity.handleLoad.value = false
                when {
                    error == null -> {
                        getDocs(idVehicle)
                       toString()
                    }
                    else -> {
                        MainActivity.handleError.value = error.toString()
                    }
                }
            }.also(compositeDisposableMain::add)
    }

    override fun storeType(type: Int) {
       this.type=type
    }

    override fun storeId(idVehicle: Int) {
        if (this.idVehicle==0) this.idVehicle=idVehicle
    }

    fun <T> applySchedulers(): SingleTransformer<T, T> {
        return SingleTransformer {
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

}