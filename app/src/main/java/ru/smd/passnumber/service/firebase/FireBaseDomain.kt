package ru.smd.passnumber.service.firebase

//import android.util.Log
//import ru.smd.passnumber.service.firebase.FirebaseApi
//import su.inert.app.base.network.BaseResponseHandler
//
///**
// * Created by Siddikov Mukhriddin on 3/22/21
// */
//class FireBaseDomain  (val api: FirebaseApi)  {
//
//    fun sendPushToken(push_token: String, uid: String){
//            api.sendPushToken(push_token, uid).enqueue( BaseResponseHandler{
//
//            })
//        Log.e("TTT","Device Id: $uid")
//    }
//    fun sendFeedback(name: String, email: String, text: String, grade: Int=2,block:()->Unit){
//        api.sendFeedback(name, email,text,grade).enqueue( BaseResponseHandler{
//            block()
//        })
//    }
//}