package com.uragiristereo.mikansei.core.model.danbooru

enum class UserLevel(val id: Int) {
    Lurker(id = 0),
    Restricted(id = 10),
    Member(id = 20),
    Gold(id = 30),
    Platinum(id = 31),
    Builder(id = 32),
    Contributor(id = 35),
    Approver(id = 37),
    Moderator(id = 40),
    Admin(id = 50),
    Owner(id = 60),
}

fun Array<UserLevel>.getUserLevelById(id: Int): UserLevel {
    return firstOrNull { it.id == id } ?: UserLevel.Lurker
}
