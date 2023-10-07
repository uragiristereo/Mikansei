# 🍊 Mikansei

[![Discord](https://img.shields.io/discord/1084015982198145065?logo=discord&logoColor=white&label=discord)](https://discord.gg/YMyVNsFvpC)

**Mikansei 未完成** _(lit. unfinished)_ is a [Danbooru](https://danbooru.donmai.us/posts?tags=rating:general) client for Android. Made with Jetpack Compose 🚀.

The name is inspired from my favorite [vocaloid](https://vocaloid.fandom.com/wiki/Vocaloid_Wiki) producer, [Orangestar](https://twitter.com/mikanseip)'s Japanese name version (蜜柑星, which is read _mikansei_).

This is a complete rewrite version of my old project [Mejiboard](https://github.com/uragiristereo/Mejiboard), that aims on delivering a great Danbooru browsing experience on Android.

#### What is Danbooru?

> Danbooru [dahn-boh-ruh] (noun):
> 1. (on donmai.us) A repository of high-quality anime-style art and doujinshi.
> 2. A taggable imageboard, with sophisticated features for keeping, organizing and finding pictures.
> 3. (Japanese) Corrugated cardboard box.

_(taken from the website's [home wiki](https://danbooru.donmai.us/wiki_pages/help:home))_

## Screenshots

<p align="center">
  <img src="./.github/assets/home.jpg?raw=true" alt="Home screen" width="30%" />
  <img src="./.github/assets/viewer.jpg?raw=true" alt="Viewer screen" width="30%" />
  <img src="./.github/assets/more_info.jpg?raw=true" alt="More info on viewer screen" width="30%" />

  <img src="./.github/assets/search.jpg?raw=true" alt="Search screen" width="30%" />
  <img src="./.github/assets/favorites.jpg?raw=true" alt="Favorites screen" width="30%" />
  <img src="./.github/assets/manage_accounts.jpg?raw=true" alt="Manage accounts screen" width="30%" />
</p>

> _Note: the screenshots don't indicate the final product._

## Features

- Sync and switch multiple Danbooru accounts with ease
- System-based theme with dynamic colors support and 3 themes available (light, dark & black)
- Optimized for one-handed operation
- Image viewer with zoom, pan & gestures support
- Video player with controls & gestures support
- Download and share posts
- DNS over HTTPS enabled by default
- Using latest Android tech stacks

**Features that are currently supported and synced with Danbooru:**

- View, favorite and vote posts
- Searching with autocomplete
- Favorites and favorite groups
- Tags filtering/blacklisting
- Safe mode toggle
- Show deleted posts toggle
- Image detail size switch

## Download

**🚧 PROJECT IS UNDER DEVELOPMENT (~90% finished) 🚧**

The signed APK is currently not yet available, please [build](#building) it yourself to test.

## Tech Stacks

This project is trying to use the latest Android tech stacks.

- Language: [Kotlin](https://kotlinlang.org)
- Architecture: [Android App Architecture](https://developer.android.com/topic/architecture)
- User Interface: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- Concurrency: [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
- Dependency Injection: [Koin](https://insert-koin.io)
- Local Database: [Room](https://developer.android.com/training/data-storage/room)
- Networking: [OkHttp](https://square.github.io/okhttp) & [Retrofit](https://square.github.io/retrofit)
- Image Loading: [Coil](https://coil-kt.github.io) & [TouchImageView](https://github.com/MikeOrtiz/TouchImageView)
- Others:
  - [Kotlinx.Serialization](https://github.com/Kotlin/kotlinx.serialization) (Serializer)
  - [Accompanist](https://google.github.io/accompanist) (Jetpack Compose Extensions)
  - [Safer Navigation Compose](https://github.com/uragiristereo/safer-navigation-compose) (Navigation)
  - [Timber](https://github.com/JakeWharton/timber) (Logger)
  - [Media3 ExoPlayer](https://github.com/androidx/media) (Video Loading)

## Resources Used

- API: [Danbooru API](https://danbooru.donmai.us/wiki_pages/help:api)
- Design Specs: [Figma](https://www.figma.com/file/R0d905PNBuPabrtoXwYxoC/Mikansei?t=jIm0pHq82qmC7S5z-1)
- Design System: [Noctiluca](https://github.com/uragiristereo/noctiluca)
- Icons: [Material Symbols](https://fonts.google.com/icons)

## Building

To build this project, you need the latest stable
of [Android Studio](https://developer.android.com/studio) and a signing key `*.jks`.

1. Clone the project and open in Android Studio.
2. Create a file named `signing.properties` inside the root directory, then add this signing
   information:

```properties
STORE_FILE=<PATH TO STORE FILE>
STORE_PASSWORD=<STORE PASSWORD>
KEY_ALIAS=<KEY ALIAS>
KEY_PASSWORD=<KEY PASSWORD>
```

3. Sync project with Gradle then Run 'app'.

## License

    Copyright 2023 Agung Watanabe

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
