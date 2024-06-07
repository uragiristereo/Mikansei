package com.uragiristereo.mikansei.core.model.dagger.viewmodel

import androidx.lifecycle.ViewModel

interface ViewModelFactory<T : ViewModel> {
    fun create(): T
}
