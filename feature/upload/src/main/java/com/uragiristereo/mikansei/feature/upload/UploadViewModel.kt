package com.uragiristereo.mikansei.feature.upload

import androidx.lifecycle.ViewModel
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.feature.upload.data.UploadRepository
import org.koin.core.context.unloadKoinModules
import timber.log.Timber

class UploadViewModel(
    private val uploadRepository: UploadRepository,
    userRepository: UserRepository,
) : ViewModel() {
    val id: String
        get() = uploadRepository.id

    init {
        Timber.d(userRepository.toString())
    }

    override fun onCleared() {
        super.onCleared()
        unloadKoinModules(uploadModule)
        Timber.d("UploadViewModel cleared")
    }
}
