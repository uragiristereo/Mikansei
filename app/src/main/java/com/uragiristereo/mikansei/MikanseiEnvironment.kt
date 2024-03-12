package com.uragiristereo.mikansei

import com.uragiristereo.mikansei.core.model.Environment

class MikanseiEnvironment : Environment {
    private val safeFlavors = listOf(
        Environment.Flavor.PLAY,
    )

    override val debug = BuildConfig.DEBUG
    override val flavor = Environment.Flavor.getByName(BuildConfig.FLAVOR)
    override val safeMode = flavor in safeFlavors
}
