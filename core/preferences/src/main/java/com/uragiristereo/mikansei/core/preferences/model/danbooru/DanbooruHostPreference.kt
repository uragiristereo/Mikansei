package com.uragiristereo.mikansei.core.preferences.model.danbooru

import com.uragiristereo.mikansei.core.model.danbooru.DanbooruHost

enum class DanbooruHostPreference : DanbooruHostPreferenceItem {
    Danbooru {
        override val host = DanbooruHost.Danbooru
    },
    Safebooru {
        override val host = DanbooruHost.Safebooru
    },
    Testbooru {
        override val host = DanbooruHost.Testbooru
    },
    DonmaiMoe {
        override val host = DanbooruHost.DonmaiMoe
    },
}
