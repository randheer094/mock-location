# Claude Code — project context

This file primes Claude Code with the information it needs to be productive in this repo.
Update it as conventions or infrastructure change. The README is for humans; this file is
for the assistant.

**Binding rules**: see [.claude/CLAUDE.md](.claude/CLAUDE.md) and the topic files it
links to under `.claude/rules/`. Treat those as authoritative — this file is background,
those are policy.

## What this app is

A single-activity Android app that mocks device location for developers and QA. Users pick
a city from a bundled list or enter custom coordinates; a foreground service keeps pushing
mocked `Location` fixes to `LocationManager.GPS_PROVIDER` and `NETWORK_PROVIDER` until
toggled off.

Play Store application id: `dev.randheer094.dev.location`. Debug builds install side-by-side
as `dev.randheer094.dev.location.debug` with a `-debug` version suffix.

## Source layout

```
app/src/main/java/dev/randheer094/dev/location
├── app/                            MockLocationApp (Koin + PermissionFlow bootstrap)
├── di/MockLocationModules.kt       Koin module (single source of wiring)
├── domain/                         Use cases, MockLocation model, DataStore keys
├── presentation/
│   ├── main/MainActivity.kt        Sets Compose content, enables edge-to-edge
│   ├── mocklocation/
│   │   ├── MockLocationViewModel.kt      combines 5 flows → UiState
│   │   ├── composable/                   Compose screens + UI primitives
│   │   └── state/                        UiState + UiStateMapper (pure fn)
│   ├── service/
│   │   ├── MockLocationService.kt        Bound + started foreground service
│   │   └── MockLocationServiceStarter.kt Wrapper around startForegroundService/stopService
│   ├── theme/                             Material 3 dynamic colour theme
│   └── utils/                             LocationUtils, NotificationUtils, PermissionUtils
└── (tests)
app/src/test/.../                    JVM unit tests (no Android deps)
app/src/androidTest/.../             Compose UI + Android framework instrumentation tests
```

Bundled city list: `app/src/main/assets/m_l.json`.

## Architecture invariants (do not break)

- **DI**: every injectable is declared in `MockLocationModules.kt`. Prefer `single {}` for
  stateless helpers, `factory {}` for use cases, `viewModel {}` for the ViewModel.
- **State**: `MockLocationViewModel` is the only thing that combines flows. UI layer reads
  `UiState` and calls ViewModel functions; it does not call use cases directly.
- **`UiStateMapper` stays Android-free** so it can be unit-tested as a pure function.
- **Service contract**:
  - `MockLocationService` is *both* bound (`onBind`) and started (`onStartCommand` promotes
    to foreground). The bound binder surface is `IMockLocationService`.
  - `MockLocationServiceStarter` is the *only* caller of `startForegroundService` /
    `stopService` from outside the service.
  - `ACTION_START` with valid `EXTRA_LAT`/`EXTRA_LONG` starts mocking. Without them the
    service briefly promotes to foreground (to satisfy the 5-sec contract after
    `startForegroundService`) and then self-stops.
  - `ACTION_STOP` stops the loop and calls `stopSelf()`.
  - `android:stopWithTask="false"` + `START_STICKY` keep the service alive across task
    swipes; `onTaskRemoved` does not need to re-launch anything.
- **Mock providers**: `LocationUtils.addMockProvider` / `removeMockProvider` return `false`
  on failure (SecurityException wrapped) — callers use this to show the setup-instructions
  screen. Do not rethrow.
- **Permissions**:
  - `ACCESS_MOCK_LOCATION` (manifest only; system-level).
  - `POST_NOTIFICATIONS` required on API 33+; pre-33 treated as always granted.
  - `FOREGROUND_SERVICE` + `FOREGROUND_SERVICE_DATA_SYNC` for the foreground service type.

## Build, test, deploy

| Goal | Command |
| --- | --- |
| Compile main | `./gradlew :app:compileDebugKotlin` |
| JVM unit tests | `./gradlew :app:testDebugUnitTest` |
| Instrumentation tests | `./gradlew :app:connectedDebugAndroidTest` (needs emulator/device) |
| Debug APK | `./gradlew :app:assembleDebug` |
| Install debug | `./gradlew :app:installDebug` |
| Release APK | `./gradlew :app:assembleRelease` (needs `keystore.properties`, falls back to debug key) |
| Release bundle | `./gradlew :app:bundleRelease` |

Instrumentation test report lands at `app/build/reports/androidTests/connected/index.html`.

## Test conventions

- Unit tests for pure Kotlin use JUnit 4 + `kotlinx-coroutines-test`.
- Compose UI tests use `createComposeRule()` — no `createAndroidComposeRule`, no activity
  launch, so tests stay fast and deterministic.
- Android framework tests use `InstrumentationRegistry.getInstrumentation().targetContext`.
- `animationsDisabled = true` is set under `testOptions` to reduce flake.
- `LocationUtilsTest` intentionally asserts the *safe-false* path because the instrumentation
  runner is not the system mock-location app. If the test is ever updated to cover the
  success path, the device must have the debug build pre-registered.

## android-cli quick reference (macOS dev machine)

These are verified on this workstation (SDK at `/Users/rks/dev/android-sdk`):

```bash
android info                                 # SDK path + versions
android emulator list                        # Available AVDs
android emulator start <avd-name>            # Boot an emulator; returns when ready
android screen capture -o /tmp/out.png       # PNG screenshot of current device
android layout --pretty                      # Compose/View layout tree as JSON
/Users/rks/dev/android-sdk/platform-tools/adb devices -l
```

## Manual QA cheatsheet

Pre-grant notification permission so the app skips the rationale screen:

```bash
adb shell pm grant dev.randheer094.dev.location.debug android.permission.POST_NOTIFICATIONS
```

Select this app as the system mock-location provider (cannot be scripted on stock Android —
requires Settings UI):

```
Settings → Developer options → Select mock location app → Mock Location for Developers (Debug)
```

Verify the foreground service and its notification while mocking is on:

```bash
adb shell dumpsys activity services dev.randheer094.dev.location.debug
adb shell dumpsys notification --noredact | grep -A5 mock_location_channel
```

Pull back the currently mocked location (any app with `ACCESS_COARSE_LOCATION` will do):

```bash
adb shell dumpsys location | grep -A2 "last location" | head
```

## Gotchas

- `permission-flow-android` requires `PermissionFlow.init(this)` in `Application.onCreate`
  before anything collects its flows. `MockLocationApp` does this.
- Tapping the ongoing notification opens `MainActivity` via a `PendingIntent` built with
  `FLAG_IMMUTABLE` + `FLAG_UPDATE_CURRENT`; keep those flags when editing
  `NotificationUtils.buildContentIntent`.
- The bundled `m_l.json` is cached in-memory inside `GetMockLocationsUseCase`. Adding a test
  hook to invalidate the cache isn't wired up — tests that need a fresh parse should
  instantiate a new `GetMockLocationsUseCase`.
- Foreground service type is `dataSync` (not `location`) to avoid the extra `ACCESS_FINE_LOCATION`
  permission requirement; see `AndroidManifest.xml`.
