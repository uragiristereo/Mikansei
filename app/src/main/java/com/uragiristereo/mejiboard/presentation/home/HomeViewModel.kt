package com.uragiristereo.mejiboard.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    var currentRoute by mutableStateOf(HomeRoute.Posts.route)
    var currentTags by mutableStateOf("")
}
