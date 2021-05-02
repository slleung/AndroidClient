package com.vmiforall.client.di

import com.vmiforall.client.data.RemoteControlRepositoryImpl
import com.vmiforall.client.data.RemoteControlRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindRemoteControlRepository(impl : RemoteControlRepositoryImpl) : RemoteControlRepository

}
