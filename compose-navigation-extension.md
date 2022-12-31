## Compose Navigation Extension

**[BETA]** A type-safe extension wrapper to simplify navigating experience with data classes support
in Jetpack Compose.

### How it works:

It simply converts the data to JSON then encode to base64 when navigating, and decode to base64 then
converts the JSON to object back when getting the data.

### Requirements

- [Compose Navigation](https://developer.android.com/jetpack/compose/navigation#setup)
- [Gson](https://github.com/google/gson)

### Setup

You can clone the repository or copy the files manually from below.

1. Create a JSON extension functions:

<details>
    <summary>JsonExtension.kt</summary>

```kotlin
import android.net.Uri
import android.util.Base64
import com.google.gson.Gson

fun Any.toJsonBase64Encoded(gson: Gson = Gson()): String {
    val out = gson.toJson(this)
        .toByteArray()

    return Uri.encode(Base64.encodeToString(out, Base64.DEFAULT))
}

inline fun <reified T> String.fromJsonBase64Encoded(gson: Gson = Gson()): T? {
    return try {
        val decoded = Base64.decode(this, Base64.DEFAULT)
            .toString(charset("UTF-8"))

        gson.fromJson(decoded, T::class.java)
    } catch (e: IllegalArgumentException) {
        null
    }
}
```

</details>

2. Create a navigation debug level enum class:

<details>
    <summary>NavigationDebugLevel.kt</summary>

```kotlin
enum class NavigationDebugLevel {
    STACKTRACE,
    MESSAGE,
    DISABLED,
}
```

</details>

3. Create a navigation extension functions:

<details>
    <summary>NavigationExtension.kt</summary>

```kotlin
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import androidx.navigation.navOptions

fun NavHostController.navigate(
    route: NavigationRoute,
    data: Map<String, Any> = mapOf(),
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    val uri = route.parseData(data)

    try {
        navigate(
            request = NavDeepLinkRequest.Builder
                .fromUri(NavDestination.createRoute(uri).toUri())
                .build(),
            navOptions = navOptions,
            navigatorExtras = navigatorExtras,
        )
    } catch (e: IllegalArgumentException) {
        // When the data is too large it usually throws IllegalArgumentException "Navigation destination that matches request cannot be found"
        // So we're printing the error instead

        route.printNavigationError(e)
    }
}

fun NavHostController.navigate(
    route: NavigationRoute,
    data: Map<String, Any> = mapOf(),
    builder: NavOptionsBuilder.() -> Unit,
) {
    navigate(
        route = route,
        data = data,
        navOptions = navOptions(builder),
    )
}

fun NavGraphBuilder.composable(
    route: NavigationRoute,
    deepLinks: List<NavDeepLink> = listOf(),
    content: @Composable NavigationRoute.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = route.route,
        arguments = route.getNamedNavArgs(),
        deepLinks = deepLinks,
        content = { content(route, it) },
    )
}
```

</details>

4. Create a class to store the navigation information:

<details>
    <summary>NavigationRoute.kt</summary>

```kotlin
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument

open class NavigationRoute(
    route: String,
    private val argsKeys: List<String> = listOf(),
) {
    val route = parseRoute(route, argsKeys)
    private val navigationDebugLevel = NavigationDebugLevel.MESSAGE

    override fun toString(): String {
        return route
    }

    private fun parseRoute(route: String, keys: List<String>): String {
        var args = ""

        keys.forEach { key ->
            args += "&$key={$key}"
        }

        if (args.take(n = 1) == "&") {
            args = args.replaceFirst(oldChar = '&', newChar = '?')
        }

        return route + args
    }

    fun getNamedNavArgs(): List<NamedNavArgument> {
        return argsKeys.map { key ->
            navArgument(name = key) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        }
    }

    fun parseData(params: Map<String, Any>): String {
        var result = route

        params.forEach { (key, value) ->
            val arg = value.toJsonBase64Encoded()

            result = result.replace(oldValue = "{$key}", newValue = arg)
        }

        return result
    }

    fun printNavigationError(e: Throwable) {
        when (navigationDebugLevel) {
            NavigationDebugLevel.STACKTRACE -> e.printStackTrace()
            NavigationDebugLevel.MESSAGE -> Log.e("NavigationError", "${e.message}")
            NavigationDebugLevel.DISABLED -> { }
        }
    }

    inline fun <reified T> getData(entry: NavBackStackEntry, key: String): T? {
        return when (val dataStr = entry.arguments?.getString(key)) {
            null -> {
                val e = IllegalArgumentException("Navigation route \"$route\" data with arg key \"$key\" cannot be found.")

                printNavigationError(e)

                null
            }
            else -> dataStr.fromJsonBase64Encoded()
        }
    }

    @Composable
    inline fun <reified T> rememberGetData(entry: NavBackStackEntry, key: String): T? {
        return remember(entry) { getData(entry, key) }
    }

    @Composable
    inline fun <reified T> rememberGetData(entry: NavBackStackEntry, key: String, defaultValue: T): T {
        return remember(entry) { getData(entry, key) ?: defaultValue }
    }
}
```

</details>

### Usage example

1. Create a sealed class that contains the routes:

```kotlin
sealed class MainRoute(
    route: String,
    argsKeys: List<String> = listOf(),
) : NavigationRoute(route, argsKeys) {
    object Home : MainRoute(route = "home")

    object Profile : MainRoute(
        route = "profile",
        argsKeys = listOf(
            "id",
            "profile",
            "show_details",
        ),
    )
}
```

2. Create a data class for Profile:

```kotlin
data class Profile(
    val firstName: String,
    val lastName: String,
)
```

2. Setup the NavHost:

```kotlin
val mainNavController = rememberNavController()

NavHost(
    navController = mainNavController,
    startDestination = MainRoute.Home.route,
) {
    composable(
        route = MainRoute.Home,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(text = "Home Screen")
        }
    }
    
    composable(
        route = MainRoute.Profile,
    ) { entry ->
        // set a default value to make it not nullable
        val showDetails = rememberGetData(entry = entry, key = "show_details", defaultValue = false)
        
        // cast type to make it nullable
        val id = rememberGetData<Int>(entry = entry, key = "id")
        val profile = rememberGetData<Profile>(entry = entry, key = "profile")
        
        Column(
            verticalArrangement = Arrangement.CenterVertically,
            horizontalAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(text = "Profile Screen\n")
            
            Text(text = "id = $id")
            
            Text(text = "show_details = $showDetails")
            
            Text(text = "profile = $profile")
        }
    }
}
```

3. Navigating example:

```kotlin
val john = Profile(firstName = "John", lastName = "Doe")

mainNavController.navigate(
    route = MainRoute.Profile,
    data = mapOf(
        "id" to 1000,
        "profile" to john,
        "show_details" to true,
    ),
)
```

### Notes

- You also able use this wrapper in another navigation library that based
  on `androidx.navigation:navigation-compose`
  like [Accompanist Navigation](https://google.github.io/accompanist/navigation-animation/)
  or [Material Motion for Jetpack Compose](https://github.com/fornewid/material-motion-compose),
  just extend the `NavGraphBuilder.composable` and match the parameters requirement for the library.
- If you forget to include some arguments when navigating, it will automatically returns null when
  you get the data and prints the error in logcat without crashing the app.
- You may get a destination not found error when navigating with a large data that totals about more
  than 100 KB from encoded route, consider navigate with a small data first (like ids) then get the
  large data from a local database.
- If you have a question, feedback, or improvements get in touch with me
  on [Telegram](https://t.me/uragiristereo) or Discord `uragiristereo#2791`
