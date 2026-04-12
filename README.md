# Mock Location for Developers

An Android app that mocks device location for development and QA. Pick from a curated list of
world cities or enter custom coordinates — the app pushes mocked fixes to the system location
providers while a foreground service keeps the loop alive in the background.

> Play Store: `dev.randheer094.dev.location`

## Features

- Mock GPS + Network providers via `LocationManager.addTestProvider`.
- Persistent selection through Jetpack DataStore.
- Foreground service with a silent ongoing notification (`POST_NOTIFICATIONS` + `FOREGROUND_SERVICE_DATA_SYNC`).
- Custom coordinates with latitude/longitude validation.
- Material 3 Compose UI, DayNight theme.

## Project layout

```
app/src/main/java/dev/randheer094/dev/location
├── app/                    # Application + Koin bootstrap
├── di/                     # Koin modules
├── domain/                 # Use cases, models, DataStore keys
└── presentation/
    ├── main/               # MainActivity
    ├── mocklocation/       # ViewModel + state + Composables
    ├── service/            # Foreground service + starter wrapper
    ├── theme/              # Material 3 theme
    └── utils/              # LocationUtils, NotificationUtils, PermissionUtils
```

## Prerequisites

- JDK **17**
- Android SDK with **API 36** (compile/target) and platform tools
- Android Studio (Narwhal or newer) or Gradle **9.1+**

## Build

```bash
./gradlew assembleDebug          # debug APK (installs as .debug side-by-side)
./gradlew assembleRelease        # release APK (requires keystore.properties, see below)
./gradlew bundleRelease          # release AAB for Play upload
```

Debug builds are installed with application ID suffix `.debug` and version name suffix
`-debug` so you can run them alongside the Play Store release.

## Release signing

Release builds are signed from a `keystore.properties` file at the repository root (this
file is gitignored). Create it once on your build machine:

```properties
storeFile=/absolute/path/to/release.keystore
storePassword=changeit
keyAlias=upload
keyPassword=changeit
```

If `keystore.properties` is missing, `assembleRelease` falls back to the debug keystore so
local QA builds keep working; CI must always provide the real credentials.

## Running the app

1. Install a debug build: `./gradlew installDebug`
2. On the device: **Settings → Developer options → Select mock location app → Mock Location for Developers (Debug)**
3. Grant the notification permission when prompted.
4. Pick a preset city or tap the edit button to enter custom coordinates.

Mocking continues in the background via a foreground service until you stop it.

## Architecture notes

- **DI**: Koin, wired in [`MockLocationModules.kt`](app/src/main/java/dev/randheer094/dev/location/di/MockLocationModules.kt).
- **State**: `MockLocationViewModel` combines five flows into a single `UiState` via `UiStateMapper`.
- **Service**: `MockLocationService` is *both* bound (so the UI can call it directly) and
  started (so it survives the UI unbinding). `MockLocationServiceStarter` is the only
  component that talks to `startForegroundService` / `stopService` — the ViewModel calls it
  whenever mocking is toggled on/off.
- **Storage**: `DataStore<Preferences>` under file name `m_l`, keys defined in
  [`MockLocation.kt`](app/src/main/java/dev/randheer094/dev/location/domain/MockLocation.kt).

## Privacy

The app does not declare the `INTERNET` permission and never transmits user data off-device.
See [`playstore/privacy-policy.md`](playstore/privacy-policy.md).

## License

Copyright © randheer094. All rights reserved.
