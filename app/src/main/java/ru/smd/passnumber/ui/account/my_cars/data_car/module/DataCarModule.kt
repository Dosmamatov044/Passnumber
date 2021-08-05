package ru.smd.passnumber.ui.account.my_cars.data_car.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ru.smd.passnumber.ui.account.my_cars.data_car.DataCarContract
import ru.smd.passnumber.ui.account.my_cars.data_car.DataCarPresenter

@InstallIn(ActivityComponent::class)
@Module
abstract class DataCarModule {
    @Binds
    abstract fun presenter(presenter: DataCarPresenter): DataCarContract.Presenter
}