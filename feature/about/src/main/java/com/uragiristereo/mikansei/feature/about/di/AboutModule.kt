package com.uragiristereo.mikansei.feature.about.di

import com.uragiristereo.mikansei.core.model.dagger.feature.FeatureScope
import dagger.Module
import dagger.Provides
import timber.log.Timber
import java.util.UUID

@Module
object AboutModule {
    @FeatureScope
    @Provides
    fun provideRandom(): Random {
        return Random(value = UUID.randomUUID().toString()).also {
            Timber.d("Random value: ${it.value}")
        }
    }
}
