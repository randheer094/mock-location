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

Mocking continues in the background via a foreground service until you stop it. Tapping the
ongoing notification brings the app back to the foreground.

## Troubleshooting

- **"Invalid latitude or longitude"** when saving a custom location — latitude must be in
  `[-90, 90]` and longitude in `[-180, 180]`. Use a dot as the decimal separator.
- **Setup instructions re-appear after tapping "Got it!"** — the app couldn't register mock
  providers, usually because it isn't the selected mock location app yet. Recheck step 2 under
  *Running the app*.
- **Foreground notification does not appear on Android 13+** — POST_NOTIFICATIONS was denied.
  Re-enable it from system notification settings or uninstall/reinstall.
- **Mocking stops after the app is swiped away** — confirm the service is still running via
  `adb shell dumpsys activity services dev.randheer094.dev.location.debug`. If it isn't, the
  OEM may apply aggressive battery optimisation; add the app to the *unrestricted* list.

## Architecture notes

- **DI**: Koin, wired in [`MockLocationModules.kt`](app/src/main/java/dev/randheer094/dev/location/di/MockLocationModules.kt).
- **State**: `MockLocationViewModel` combines five flows into a single `UiState` via `UiStateMapper`.
- **Service**: `MockLocationService` is *both* bound (so the UI can call it directly) and
  started (so it survives the UI unbinding). `MockLocationServiceStarter` is the only
  component that talks to `startForegroundService` / `stopService` — the ViewModel calls it
  whenever mocking is toggled on/off.
- **Storage**: `DataStore<Preferences>` under file name `m_l`, keys defined in
  [`MockLocation.kt`](app/src/main/java/dev/randheer094/dev/location/domain/MockLocation.kt).

## Testing

The project ships with two test tiers:

| Tier | Source set | Runner | Command |
| --- | --- | --- | --- |
| JVM unit tests | `app/src/test` | JUnit 4 | `./gradlew testDebugUnitTest` |
| Instrumentation tests | `app/src/androidTest` | AndroidJUnitRunner + Compose UI test | `./gradlew connectedDebugAndroidTest` |

### JVM unit tests

Pure-Kotlin coverage of the `MockLocation` serialization contract and `UiStateMapper`
branching. No emulator needed.

### Instrumentation tests

- **Compose UI tests** (`AddMockLocationBottomSheetTest`, `MockLocationNStatusTest`,
  `SectionHeaderTest`, `LocationTest`, `SetupInstructionTest`) verify composable behaviour
  with `createComposeRule` — no activity launch required.
- **Android unit tests** (`NotificationUtilsTest`, `LocationUtilsTest`,
  `GetMockLocationsUseCaseTest`, `PackageContextTest`) exercise the Android framework code
  that a plain JVM test can't reach (notification channels, `LocationManager`, asset IO).

Run them on a connected emulator or device:

```bash
./gradlew connectedDebugAndroidTest
```

A test report is produced at `app/build/reports/androidTests/connected/index.html`.

> `LocationUtilsTest` asserts the *safe-false* return path because the runner process is not
> registered as the system mock-location app. Granting that manually before the run (Settings
> → Developer options → Select mock location app → **Mock Location for Developers (Debug)**)
> exercises the success path too.

## Continuous integration

- Always run `./gradlew testDebugUnitTest` before pushing — it is fast and catches
  serialization or state-mapping regressions.
- `./gradlew connectedDebugAndroidTest` requires an emulator/device. Start one via Android
  Studio or `emulator -avd <name>`; Compose tests have `animationsDisabled = true` so flakes
  from the system animator are already suppressed.
- For a smoke build: `./gradlew :app:assembleDebug`. For release verification:
  `./gradlew :app:bundleRelease` (requires `keystore.properties`, see above).

## Privacy

The app does not declare the `INTERNET` permission and never transmits user data off-device.
See [`playstore/privacy-policy.md`](playstore/privacy-policy.md).

## License

Copyright © randheer094. All rights reserved.
