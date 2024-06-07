package com.uragiristereo.mikansei.core.model.dagger.component

import android.content.Context
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
        fun applicationContext(context: Context): Builder

        fun build(): AppComponent
    }
}
