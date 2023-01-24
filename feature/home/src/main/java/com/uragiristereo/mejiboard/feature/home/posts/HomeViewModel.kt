package com.uragiristereo.mejiboard.feature.home.posts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.uragiristereo.mejiboard.core.ui.navigation.HomeRoute
import com.uragiristereo.mejiboard.lib.navigation_extension.core.route

class HomeViewModel : ViewModel() {
    var currentRoute by mutableStateOf(HomeRoute.Posts::class.route)
    var currentTags by mutableStateOf("")
}
