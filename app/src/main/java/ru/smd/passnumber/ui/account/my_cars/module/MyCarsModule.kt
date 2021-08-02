package ru.smd.passnumber.ui.account.my_cars.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ru.smd.passnumber.ui.account.my_cars.MyCarsContract
import ru.smd.passnumber.ui.account.my_cars.MyCarsPresenter

@InstallIn(ActivityComponent::class)
@Module
abstract class MyCarsModule {
    @Binds
    abstract fun presenter(presenter: MyCarsPresenter): MyCarsContract.Presenter
}