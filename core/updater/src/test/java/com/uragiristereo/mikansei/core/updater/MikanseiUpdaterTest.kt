package com.uragiristereo.mikansei.core.updater

import app.cash.turbine.test
import com.uragiristereo.mikansei.core.updater.config.UpdaterConfig
import com.uragiristereo.mikansei.core.updater.model.Release
import com.uragiristereo.mikansei.core.updater.model.UpdaterResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertIs

class MikanseiUpdaterTest {
    private val initialRelease = Release(
        version = 1000,
        name = "1.0.0",
        url = "https://",
        forceUpdate = false,
        description = "Initial release",
    )

    @Test
    fun `app is on latest version`() {
        val currentVersionCode = 1000

        val releases = listOf(initialRelease)

        val initialConfig = UpdaterConfig(lastVersionInstalled = 1000)

        val updater = MockMikanseiUpdater(currentVersionCode, releases, MockUpdaterConfigRepository(initialConfig))

        runBlocking {
            updater.result.test {
                assertEquals(awaitItem(), UpdaterResult.NoUpdates)
                awaitComplete()
            }
        }
    }

    @Test
    fun `app is updated and showing changelogs`() {
        val currentVersionCode = 1001

        val releases = listOf(initialRelease) + listOf(
            Release(
                version = currentVersionCode,
                name = "1.0.1",
                url = "https://",
                forceUpdate = false,
                description = "Small bug fix",
            )
        )

        val initialConfig = UpdaterConfig(lastVersionInstalled = 1000)

        val updater = MockMikanseiUpdater(currentVersionCode, releases, MockUpdaterConfigRepository(initialConfig))

        runBlocking {
            updater.result.test {
                assertIs<UpdaterResult.ShowChangelog>(awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun `app was opened with update enforced & show forced releases`() {
        val currentVersionCode = 1000

        val releases = listOf(initialRelease) + listOf(
            Release(
                version = 1001,
                name = "1.1.0",
                url = "https://",
                forceUpdate = true,
                description = "Fatal bug fixed",
            )
        )

        val initialConfig = UpdaterConfig(
            lastVersionInstalled = 1000,
            updateEnforced = true,
        )

        val updater = MockMikanseiUpdater(currentVersionCode, releases, MockUpdaterConfigRepository(initialConfig))

        runBlocking {
            updater.result.test {
                assertIs<UpdaterResult.UpdateEnforced>(awaitItem())
                assertIs<UpdaterResult.UpdatesAvailable>(awaitItem())
                awaitComplete()
            }

            assertEquals(updater.readConfig().updateEnforced, true)
        }
    }

    @Test
    fun `app was opened with update not enforced & show forced releases`() {
        val currentVersionCode = 1000

        val releases = listOf(initialRelease) + listOf(
            Release(
                version = 1001,
                name = "1.1.0",
                url = "https://",
                forceUpdate = true,
                description = "Fatal bug fixed",
            )
        )

        val initialConfig = UpdaterConfig(
            lastVersionInstalled = 1000,
            updateEnforced = false,
        )

        val updater = MockMikanseiUpdater(currentVersionCode, releases, MockUpdaterConfigRepository(initialConfig))

        runBlocking {
            updater.result.test {
                assertIs<UpdaterResult.UpdatesAvailable>(awaitItem())
                awaitComplete()
            }

            assertEquals(updater.readConfig().updateEnforced, true)
        }
    }

    @Test
    fun `app has updates with no forced releases`() {
        val currentVersionCode = 1000

        val releases = listOf(initialRelease) + listOf(
            Release(
                version = 1001,
                name = "1.0.1",
                url = "https://",
                forceUpdate = false,
                description = "Small bug fix",
            )
        )

        val initialConfig = UpdaterConfig(lastVersionInstalled = 1000)

        val updater = MockMikanseiUpdater(currentVersionCode, releases, MockUpdaterConfigRepository(initialConfig))

        runBlocking {
            updater.result.test {
                assertIs<UpdaterResult.UpdatesAvailable>(awaitItem())
                awaitComplete()
            }
        }
    }
}
