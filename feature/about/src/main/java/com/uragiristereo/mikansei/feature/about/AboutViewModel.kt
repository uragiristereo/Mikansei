package com.uragiristereo.mikansei.feature.about

import androidx.lifecycle.ViewModel
import com.uragiristereo.mikansei.core.model.dagger.viewmodel.ViewModelFactory
import com.uragiristereo.mikansei.feature.about.di.Random
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import okhttp3.OkHttpClient
import timber.log.Timber

class AboutViewModel @AssistedInject constructor(
    okHttpClient: OkHttpClient,
    val random: Random,
) : ViewModel() {
    init {
        Timber.d(okHttpClient.toString())
    }

    @AssistedFactory
    interface Factory : ViewModelFactory<AboutViewModel>
}
