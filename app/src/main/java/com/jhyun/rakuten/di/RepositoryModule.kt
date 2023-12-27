package com.jhyun.rakuten.di

import com.jhyun.rakuten.data.LocalRepositoryImpl
import com.jhyun.rakuten.domain.LocalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindLocalRepository(repository: LocalRepositoryImpl) : LocalRepository

}