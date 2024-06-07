package com.uragiristereo.mikansei.feature.about.di

import android.content.Context
import com.uragiristereo.mikansei.core.model.dagger.component.AppComponent
import com.uragiristereo.mikansei.core.model.dagger.feature.FeatureContainer
import com.uragiristereo.mikansei.feature.about.AboutViewModel
import javax.inject.Inject

class AboutContainer(context: Context) : FeatureContainer(context) {
    @Inject
    lateinit var viewModelFactory: AboutViewModel.Factory

    override fun initDagger(appComponent: AppComponent) {
        DaggerAboutComponent.factory()
            .create(appComponent)
            .inject(this)
    }
}

