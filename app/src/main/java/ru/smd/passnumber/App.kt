package ru.smd.passnumber

import android.app.Application
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import dagger.hilt.android.HiltAndroidApp
import ru.smd.passnumber.data.core.Constants.API_KEY_YANDEX

/**
 * Created by Siddikov Mukhriddin on 2/13/21
 */
@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

val config=YandexMetricaConfig.newConfigBuilder(API_KEY_YANDEX).build()
        YandexMetrica.activate(getApplicationContext(), config)
        YandexMetrica.enableActivityAutoTracking(this)
//        AppDatabase.init(this)
    }
}