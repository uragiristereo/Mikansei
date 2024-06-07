package com.uragiristereo.mikansei.core.model.dagger.component

import android.app.Application
import com.uragiristereo.mikansei.core.model.dagger.module.AppModule
import com.uragiristereo.mikansei.core.model.dagger.module.SingletonProvider
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent : SingletonProvider {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
