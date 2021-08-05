package ru.smd.passnumber.ui.account.my_cars.data_car.docs.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ru.smd.passnumber.ui.account.my_cars.data_car.docs.DocsContract
import ru.smd.passnumber.ui.account.my_cars.data_car.docs.DocsPresenter

@InstallIn(ActivityComponent::class)
@Module
abstract class DocsModule {
    @Binds
    abstract fun presenter(presenter: DocsPresenter): DocsContract.Presenter
}