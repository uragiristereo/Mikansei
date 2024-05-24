package com.uragiristereo.mikansei.feature.user.delegation.list

data class UserDelegation(
    val userId: Int,
    val name: String? = null,
    val selected: Boolean = false,
)
