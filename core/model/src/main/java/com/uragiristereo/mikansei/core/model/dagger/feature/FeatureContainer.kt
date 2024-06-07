package com.uragiristereo.mikansei.core.model.dagger.feature

import android.content.Context
import com.uragiristereo.mikansei.core.model.dagger.component.AppComponent
import com.uragiristereo.mikansei.core.model.dagger.component.AppComponentProvider

abstract class FeatureContainer(private val context: Context) {
    abstract fun initDagger(appComponent: AppComponent)

    private fun getAppComponent(): AppComponent {
        return (context.applicationContext as AppComponentProvider).getComponent()
    }

    init {
        this.initDagger(getAppComponent())
    }
}
