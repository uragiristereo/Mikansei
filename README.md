# 🍊 Mikansei

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

here

## Features

- Sync and switch multiple Danbooru accounts with ease
- System-based theme with dynamic colors support and 3 themes available (light, dark & black)
- Optimized for one-handed operation
- Landscape/wide screen friendly navigation
- Image viewer with zoom, pan & gesture support
- Download and share image
- DNS over HTTPS enabled by default
- Using latest Android tech stacks

**Features that are currently supported and synced with Danbooru:**

- View posts
- Searching with autocomplete
- Favorites and favorite groups
- Tags blacklisting
- Safe mode toggle
- Show deleted posts toggle
- Image detail size switch

## Download

Available on: [Github Releases](https://) or [Direct APK](https://).

## Tech Stacks

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
  - [ExoPlayer](https://github.com/google/ExoPlayer) (Video Loading)

## Resources Used

- API: [Danbooru API](https://danbooru.donmai.us/wiki_pages/help:api)
- Design Specs: [Figma](https://www.figma.com/file/R0d905PNBuPabrtoXwYxoC/Mikansei?t=jIm0pHq82qmC7S5z-1)
- Design System: [Material Design 2](https://m2.material.io)
- Icons: [Material Symbols](https://fonts.google.com/icons)

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
