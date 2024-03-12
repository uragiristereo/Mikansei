package com.uragiristereo.mikansei.core.model

interface Environment {
    val debug: Boolean
    val flavor: Flavor
    val safeMode: Boolean

    enum class Flavor {
        OSS,
        PLAY,
        ;

        companion object {
            fun getByName(target: String): Flavor {
                return try {
                    valueOf(target.uppercase())
                } catch (e: IllegalArgumentException) {
                    PLAY
                }
            }
        }
    }
}
