package com.uragiristereo.mikansei.di

import com.uragiristereo.mikansei.MikanseiApp
import com.uragiristereo.mikansei.core.model.dagger.component.AppComponent
import dagger.Component

@ApplicationScope
@Component(dependencies = [AppComponent::class])
interface MikanseiAppComponent {
    fun inject(app: MikanseiApp)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): MikanseiAppComponent
    }
}
