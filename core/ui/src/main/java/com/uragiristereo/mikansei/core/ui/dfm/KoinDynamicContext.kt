package com.uragiristereo.mikansei.core.ui.dfm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.koin.compose.KoinContext
import org.koin.core.error.DefinitionOverrideException
import org.koin.core.module.Module
import org.koin.mp.KoinPlatformTools
import timber.log.Timber

@Composable
fun KoinDynamicContext(
    module: Module,
    content: @Composable () -> Unit,
) {
    KoinContext(
        context = remember(module) {
            Timber.d("KoinDynamicContext composed")

            KoinPlatformTools.defaultContext().get().apply {
                try {
                    loadModules(
                        modules = listOf(module),
                        allowOverride = false,
                        createEagerInstances = false,
                    )

                    Timber.d("KoinDynamicContext module loaded")
                } catch (_: DefinitionOverrideException) {
                }
            }
        },
        content = content,
    )
}
