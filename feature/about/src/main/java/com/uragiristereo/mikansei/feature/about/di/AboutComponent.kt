package com.uragiristereo.mikansei.feature.about.di

import com.uragiristereo.mikansei.core.model.dagger.component.AppComponent
import com.uragiristereo.mikansei.core.model.dagger.feature.FeatureScope
import dagger.Component

@FeatureScope
@Component(
    dependencies = [AppComponent::class],
    modules = [AboutModule::class],
)
interface AboutComponent {
    fun inject(container: AboutContainer)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): AboutComponent
    }
}
