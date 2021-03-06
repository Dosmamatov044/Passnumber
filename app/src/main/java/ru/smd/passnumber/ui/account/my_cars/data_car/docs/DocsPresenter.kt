package ru.smd.passnumber.ui.account.my_cars.data_car.docs

import android.content.Context
import id.zelory.compressor.Compressor
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.smd.passnumber.data.entities.Docs
import ru.smd.passnumber.data.service.PassNumberRepo
import ru.smd.passnumber.ui.activities.main.MainActivity
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DocsPresenter @Inject constructor(
    val repo: PassNumberRepo,
) : DocsContract.Presenter {

    private var view: DocsContract.View? = null

    override var file: File? = null
    override lateinit var context: Context

    var type = 0

    var idVehicle = 0

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
        getDocs(idVehicle)
        file?.let { addPhoto(it, context) }
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
        MainActivity.handleLoad.postValue(true)
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

    fun toRequestBody(value: String): RequestBody {
        val body = RequestBody.create("text/plain".toMediaTypeOrNull(), value)
        return body
    }


    override fun addPhoto(file: File, context: Context) {
        val reqFile: RequestBody
        val part: MultipartBody.Part
        if (file.toURI().toString().contains(".jpg") || file.toURI().toString().contains(".png")) {
            compressedFile = Compressor(context).compressToFile(file)
            reqFile = compressedFile.asRequestBody("image/*".toMediaTypeOrNull())
            part = MultipartBody.Part.createFormData("file", "file.png", reqFile)
        } else {
            reqFile = file.asRequestBody("application/json".toMediaTypeOrNull())
            part = MultipartBody.Part.createFormData("file", "file", reqFile)
        }
        val map = mutableMapOf<String, RequestBody>()
        map.put("type_id", toRequestBody(type.toString()))
        MainActivity.handleLoad.postValue(true)
        repo.storeDocs(idVehicle, part, map).compose(applySchedulers())
            .subscribe { response, error ->
                MainActivity.handleLoad.value = false
                when {
                    error == null -> {
                        getDocs(idVehicle)
                    }
                    else -> {
                        MainActivity.handleError.value = error.toString()
                    }
                }
            }.also(compositeDisposable::add)
        this.file = null
    }

    override fun storeType(type: Int) {
        this.type = type
    }

    override fun storeId(idVehicle: Int) {
        this.idVehicle = idVehicle
    }

    override fun deleteDoc(id: Int) {
        MainActivity.handleLoad.postValue(true)
        repo.deleteDoc(id).compose(applySchedulers())
            .subscribe { response, error ->
                MainActivity.handleLoad.value = false
                when {
                    error == null -> {
                        getDocs(idVehicle)
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