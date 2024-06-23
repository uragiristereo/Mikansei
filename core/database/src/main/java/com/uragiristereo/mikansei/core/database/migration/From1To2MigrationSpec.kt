package com.uragiristereo.mikansei.core.database.migration

import androidx.room.DeleteTable
import androidx.room.migration.AutoMigrationSpec

@DeleteTable(tableName = "sessions")
class From1To2MigrationSpec : AutoMigrationSpec
