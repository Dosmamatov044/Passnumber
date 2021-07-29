package ru.smd.passnumber.ui.notification.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ru.smd.passnumber.ui.notification.NotificationContract
import ru.smd.passnumber.ui.notification.NotificationPresenter

@InstallIn(ActivityComponent::class)
@Module
abstract class NotificationModule {
    @Binds
    abstract fun presenter(presenter: NotificationPresenter): NotificationContract.Presenter
}