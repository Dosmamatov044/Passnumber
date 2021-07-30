package ru.smd.passnumber.ui.account.my_data.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ru.smd.passnumber.ui.account.my_data.MyDataContract
import ru.smd.passnumber.ui.account.my_data.MyDataPresenter

@InstallIn(ActivityComponent::class)
@Module
abstract class MyDataModule {
    @Binds
    abstract fun presenter(presenter: MyDataPresenter): MyDataContract.Presenter
}