package com.uragiristereo.mikansei.core.preferences.model.danbooru

import com.uragiristereo.mikansei.core.model.danbooru.DanbooruHost
import com.uragiristereo.mikansei.core.preferences.model.base.PreferenceString

interface DanbooruHostPreferenceItem : PreferenceString {
    val host: DanbooruHost

    override val title: String
        get() = host.name

    override val subtitle: String
        get() = host.getBaseUrl()
}
