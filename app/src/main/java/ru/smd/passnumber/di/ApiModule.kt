package ru.smd.passnumber.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import ru.smd.passnumber.data.service.ApiService
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun getPostApi(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

}