# E2E Tests (mobilewright)

Black-box tests for the `mock-location` Android app. Drives a booted
emulator through every user-visible flow and asserts outcomes via the
UI plus `adb shell` introspection.

## What's covered

| File | Tests | Focus |
| --- | --- | --- |
| `00-adb-parser.test.ts` | 2 | Pure-fn unit tests for `dumpsys location` parsing |
| `01-setup-screen.test.ts` | 2 | Setup wizard rendering, "Open developer options" CTA dispatches the correct system intent |
| `02-preset-mocking.test.ts` | 3 | Tap a preset → mocking active (UI + ADB), `dumpsys location` shows mocked coords, stop tears the service down |
| `03-custom-coordinates.test.ts` | 4 | Open the bottom sheet, valid input starts mocking, invalid input doesn't, ARC-155 close-button regression |
| `04-sort-order.test.ts` | 2 | Default A–Z label, toggle flips to Z–A (ARC-156/157) |
| `05-foreground-service.test.ts` | 3 | Notification posted while mocking, tap returns to MainActivity, service survives task removal |
| `06-permissions.test.ts` | 1 | Pre-granted POST_NOTIFICATIONS skips the rationale screen |
| `07-edge-cases.test.ts` | 1 | User can stop mocking after mock-app appops is revoked mid-run |
| **Total** | **18** | |

A clean run is ~10 minutes against a single emulator (`workers: 1`). All tests pass on first try in steady state; `retries: 2` in `playwright.config.ts` absorbs the occasional mobilewright RPC WebSocket disconnect (no built-in reconnect in 0.0.29).

## Prerequisites

- Node **≥ 18**
- Android SDK + `adb` on PATH (`ANDROID_HOME` / `ANDROID_SDK_ROOT` set)
- A booted Android emulator (API **34+** tested; lower APIs may work but aren't verified)
- Debug APK at `app/build/outputs/apk/debug/app-debug.apk` — `make e2e` builds it; otherwise build with `./gradlew :app:assembleDebug` first

Boot an emulator using the project's `android-cli` helper:

```bash
android emulator list
android emulator start <avd-name>
```

Verify the device shows up:

```bash
adb devices -l
```

Install Node deps once (or whenever `e2e/package.json` changes):

```bash
make e2e-install   # equivalent to: cd e2e && npm install
```

## Run

From the repo root:

```bash
make e2e
```

This builds the debug APK and runs the full mobilewright suite.

From `e2e/` for finer control:

```bash
# Whole suite
npx mobilewright test

# Single file
npx mobilewright test tests/02-preset-mocking.test.ts

# Filter by test title (Playwright `--grep`)
npx mobilewright test --grep "Open Settings"

# Disable retries to surface real flakes during debugging
npx mobilewright test --retries=0

# Open the HTML report after a run
npx playwright show-report
```

The HTML report lives at `e2e/playwright-report/index.html`; per-test artifacts (traces, screenshots, ADB logcat) at `e2e/test-results/` — both git-ignored.

## How per-test isolation works

The custom test fixture in `fixtures/device.ts` runs before every test:

1. `adb shell cmd statusbar collapse` — close any leftover notification shade
2. `adb shell pm clear <pkg>` — wipe runtime perms + DataStore
3. `adb shell pm grant <pkg> POST_NOTIFICATIONS` — re-grant notif perm
4. `adb shell appops set <pkg> android:mock_location allow` — re-grant mock-app
5. `adb shell am force-stop <pkg>` — clean process
6. `android.launch({ bundleId })` — establish the mobilewright driver session
7. `adb shell am start -n <pkg>/.../MainActivity` — explicitly bring our activity to the front (avoids LeakCanary's CATEGORY_LAUNCHER hijack)

Tests that need the home screen call `goToHome(device)` in `beforeEach`
to dismiss the one-time setup wizard. The wizard-specific test
(`01-setup-screen.test.ts`) reverts the mock-app appops grant in its
own `beforeEach` so the wizard is visible.

## Fixtures and helpers

```
e2e/fixtures/
├── constants.ts   PKG, APP_LABEL, CHANNEL_ID, DEFAULT_CITY, APK_PATH
├── selectors.ts   Pinned user-visible strings (single source of truth)
├── adb.ts         Typed wrappers around `adb shell` (foreground service
│                  check, last-mocked-location parser, notification
│                  visibility, permission ops, ACTION_STOP broadcast)
├── mockApp.ts     selectMockApp / resetMockApp (appops + UI fallback)
├── device.ts      Custom test fixture — pm clear + grants + launch
└── home.ts        goToHome, scrollToTop, stopIfMocking,
                   startMockingDefaultCity
```

A few rules worth keeping when adding tests:

- **All ADB calls go through `adb.ts`.** No raw `device.shell()` in tests
  (mobilewright 0.0.29's `Device` has no `shell()` method anyway —
  `adb.ts` shells out via Node's `child_process`).
- **Every visible string lives in `selectors.ts`.** When `strings.xml`
  changes, the diff should touch one TS file.
- **Use `getByLabel(<contentDescription>)` for buttons** wherever the
  Compose layer has been augmented with `Modifier.semantics`. The plain
  `getByText` flake is real (cards titled the same as buttons collide).
- **Use `expect(...).toBeVisible()`, not `locator.isVisible()`,** for
  waits — the former polls, the latter snapshots once.

## App-side accessibility additions

Three Compose composables expose explicit `Modifier.semantics { contentDescription = ... }` so mobilewright (and TalkBack) can target them by accessible name:

- `AddMockLocationBottomSheet.kt` — lat / lng `OutlinedTextField`s
- `MockLocationScreen.kt` — the add-location FAB
- `SetupInstruction.kt` — "Open developer options" and "I've done this — check again" buttons

Without these, mobilewright's accessibility tree either doesn't surface the input fields at all or matches the wrong nodes (button labels colliding with card titles).

## Troubleshooting

**`mobilewright doctor` fails**

Verify `adb devices -l` shows a booted emulator and `ANDROID_HOME` / `ANDROID_SDK_ROOT` are set. The iOS section of doctor reports errors on Android-only setups; ignore them.

**Test passes UI assertions but mocking isn't actually delivering coordinates**

Check `adb shell dumpsys location | grep "last location"` while the test runs, and `adb logcat | grep MockLocationService` for service-side errors.

**Tests fail at `goToHome` or land on a "0 Distinct Leaks" screen**

LeakCanary's launcher activity is winning launcher resolution. The fixture already does `am start MainActivity` after `android.launch` to defeat this; if you're seeing it elsewhere, follow the same pattern in your test's `beforeEach`.

**`getByText` finds the wrong element**

Look at the screen tree:

```bash
adb shell uiautomator dump /sdcard/ui.xml && adb shell cat /sdcard/ui.xml
```

If two nodes match (e.g. a card title and a button label), add a `Modifier.semantics { contentDescription = ... }` to the target on the app side and select via `getByLabel`. Don't try to disambiguate via text alone; case-insensitive prefix matching can collide.

**Suite suddenly all-fails on a fresh AVD**

The first run of any test reinstalls the APK (`adb install -r -t -g`). If install fails (signature mismatch from a previous build, version downgrade, etc.), every subsequent test will fail. Run `adb uninstall dev.randheer094.dev.location.debug` and retry.

## Scripts (package.json)

```bash
npm test           # mobilewright test
npm run test:headed  # mobilewright test --reporter=list
npm run doctor     # mobilewright doctor
```
