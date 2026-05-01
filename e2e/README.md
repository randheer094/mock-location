# E2E Tests (mobilewright)

Black-box tests for the `mock-location` Android app. Drives a booted
emulator through every user-visible flow and asserts outcomes via the
UI plus `adb shell` introspection.

## Prerequisites

- Node ≥ 18
- Android SDK + ADB on PATH
- A booted Android emulator (API 34+ tested)
- Debug APK at `app/build/outputs/apk/debug/app-debug.apk` (or run via the Makefile target which builds it)

Boot an emulator from this project's `android-cli`:

```bash
android emulator list
android emulator start <avd-name>
```

## Run

From the repo root:

```bash
make e2e
```

This builds the debug APK and runs the full mobilewright suite.

To run a single test file:

```bash
cd e2e && npx mobilewright test tests/02-preset-mocking.test.ts
```

## How per-test isolation works

The custom test fixture in `fixtures/device.ts` runs before every test:

1. `adb shell pm clear <pkg>` — wipes runtime perms + DataStore
2. `adb shell pm grant <pkg> POST_NOTIFICATIONS` — re-grants notif perm
3. `adb shell appops set <pkg> android:mock_location allow` — re-grants mock-app
4. `adb shell am force-stop <pkg>` — clean process
5. `android.launch({ bundleId })` — fresh app launch

Tests that need the home screen call `goToHome(device)` in `beforeEach`
to dismiss the one-time setup wizard. Tests that need the wizard
visible (`01-setup-screen.test.ts`) revert the mock-app appops grant
in their own `beforeEach`.

## Troubleshooting

- **`mobilewright doctor` fails:** verify `adb devices -l` shows a
  booted emulator and `ANDROID_HOME` / `ANDROID_SDK_ROOT` are set.
  Note: the iOS section of doctor will report errors; ignore them on
  Android-only setups.
- **Selector mismatch:** every user-visible string is in
  `fixtures/selectors.ts`. Update there only — never hardcode strings
  in test bodies.
- **Tests pass but mocking doesn't actually work:** check
  `adb logcat | grep MockLocationService` while the test runs.
- **Flake:** `playwright.config.ts` already sets `retries: 1`. If a
  test consistently flakes, dump `adb shell dumpsys activity` /
  `dumpsys location` output around the failing assertion to see the
  raw state.
