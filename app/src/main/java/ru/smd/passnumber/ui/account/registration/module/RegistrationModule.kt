package ru.smd.passnumber.ui.account.registration.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ru.smd.passnumber.ui.account.registration.RegistrationContract
import ru.smd.passnumber.ui.account.registration.RegistrationPresenter


@InstallIn(ActivityComponent::class)
@Module
abstract class RegistrationModule {
    @Binds
    abstract fun presenter(presenter: RegistrationPresenter): RegistrationContract.Presenter
}