package ru.smd.passnumber.data.service

import androidx.lifecycle.MutableLiveData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import ru.smd.passnumber.ui.MainActivity.Companion.handleError
import ru.smd.passnumber.ui.MainActivity.Companion.handleLoad
import java.lang.Exception

/**
 * Created by Siddikov Mukhriddin on 12/29/20
 */


//sealed class Result<T> {
//    data class Success<T>(val call: Call<T>, val response: Response<T>) : Result<T>()
//    data class Failure<T>(val call: Call<T>, val error: Throwable) : Result<T>()
//}

inline fun <reified T> Call<ResponseData<T>>.enqueue(crossinline block: (ResponseData<T>) -> Unit) {
    handleLoad.postValue(true)
    enqueue(object : Callback<ResponseData<T>> {
        override fun onFailure(call: Call<ResponseData<T>>, error: Throwable) {
            handleLoad.value=false
            if (error is HttpException)
                handleResponseError(error, {
                    handleError.value = it
                }) {
                    handleError.value = error.toString()
                }
            else
                handleError.value = error.toString()
//            block(Result.Failure(call,error))
        }

        override fun onResponse(call: Call<ResponseData<T>>, response: Response<ResponseData<T>>) {
            handleLoad.value=false
//            block( Result.Success(call,response))
            if (response.body()==null)
                handleError.value="Empty"
            else
            block(response.body()!!)

        }
    })
}

//inline fun <reified T> Call<ResponseData<T>>.enqueueNew(crossinline success: (Result.Success<ResponseData<T>>) -> Unit,crossinline failure: (Result.Failure<ResponseData<T>>) -> Unit) {
//    enqueue(object : Callback<ResponseData<T>> {
//        override fun onFailure(call: Call<ResponseData<T>>, error: Throwable) {
//
//            failure(Result.Failure(call,error))
//        }
//
//        override fun onResponse(call: Call<ResponseData<T>>, response: Response<ResponseData<T>>) {
//            success( Result.Success(call,response))
//
//        }
//    })
//}

fun handleResponseError(error: HttpException, handledError: (String) -> Unit, block: () -> Unit) {
    try {
        val errorBody = error.response()?.errorBody()
        if (errorBody!=null){
        val jObjError = JSONObject(errorBody.string())
        val ks = jObjError.getString("message")
            handledError(ks)
        }
    } catch (e: Exception) {
        block()
    }
}

fun MutableLiveData<String>.errorHandle(it: Throwable){
    if (it is HttpException)
        handleResponseError(it, {
            value=it
        }) {
            value=it.toString()
        }
}