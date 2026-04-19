# Project rules

Binding rules for anyone (human or agent) making changes in this repo. CLAUDE.md at the
project root imports this file, so these rules apply to every Claude Code session here.

## Code & architecture

1. **DI goes through Koin.** Do not introduce `@Inject`, `@HiltAndroidApp`, or service
   locators. Every injectable must be declared in `di/MockLocationModules.kt`.
2. **`UiStateMapper` stays pure Kotlin.** No Android types, no `Context`, no `Resources`.
   Strings are resolved in composables via `stringResource`, not in the mapper.
3. **ViewModel is the only combiner of flows.** Composables read `UiState` and invoke
   ViewModel methods. They never call use cases or DataStore directly.
4. **Use cases stay thin.** Each file = one class = one responsibility. Add a new use case
   before extending an existing one to do a second job.
5. **Service lifecycle is load-bearing — do not edit `MockLocationService` without reading
   the "Architecture invariants" section of `CLAUDE.md` first.** Particularly:
   - `MockLocationServiceStarter` is the only external caller of `startForegroundService` /
     `stopService`.
   - `ACTION_START` without coordinates must foreground-then-stopSelf to honour the 5-sec
     `startForegroundService` contract.
   - `onTaskRemoved` must not relaunch the service (foreground state + `stopWithTask="false"`
     already keep it alive).
6. **Never rethrow from `LocationUtils.addMockProvider` / `removeMockProvider`.** Return
   `false` on failure so the setup-instructions flow can kick in.

## Permissions & manifest

7. **Do not add runtime permissions without an explicit ask from the user.** The current
   set is the minimum: `ACCESS_MOCK_LOCATION`, `POST_NOTIFICATIONS`, `FOREGROUND_SERVICE`,
   `FOREGROUND_SERVICE_DATA_SYNC`. Do not add `INTERNET`, `ACCESS_FINE_LOCATION`, or
   analytics SDKs.
8. **Do not change `foregroundServiceType`** from `dataSync` without discussing — switching
   to `location` would require `ACCESS_FINE_LOCATION` and change the Play listing.
9. **PendingIntent flags must stay `FLAG_IMMUTABLE` + `FLAG_UPDATE_CURRENT`** (security
   requirement on API 31+).

## Dependencies

10. **No new third-party libraries without discussion.** If a new dep is unavoidable, add it
    to `gradle/libs.versions.toml` with a pinned version, and explain the choice in the PR
    description.
11. **No analytics, crash reporting, or remote config libraries ever.** The app ships
    without `INTERNET` permission for a reason — see `playstore/privacy-policy.md`.

## Testing

12. **Every behavioural change gets a test.** Pure logic → JVM unit test under `app/src/test`.
    Composable or framework behaviour → instrumentation test under `app/src/androidTest`.
13. **Prefer `createComposeRule()` over `createAndroidComposeRule()`** — no activity launch
    keeps tests fast and lets them run without extra permissions.
14. **`./gradlew testDebugUnitTest` must be green before any commit.** Instrumentation tests
    (`connectedDebugAndroidTest`) must be green before any release build.
15. **Do not delete or neuter existing tests to make a change pass.** If a test becomes
    wrong, update it to encode the new expected behaviour and note why in the test body.

## Release & signing

16. **Never commit `keystore.properties`** or any keystore file. The release signing config
    falls back to the debug keystore when these are missing so local builds work; CI must
    supply the real values.
17. **Bump `versionCode` monotonically** in `app/build.gradle.kts` for every Play upload.
    Do not reuse a previously uploaded value.

## Tone for this codebase

18. Keep comments sparse. Only write a comment when the *why* is non-obvious — a hidden
    Android quirk, a service-lifetime invariant, an intentional no-op. Do not narrate code.
19. Prefer editing existing files over creating new ones. No helper files, no
    `utils/MiscExtensions.kt`, no speculative abstractions.
20. UI strings live in `res/values/strings.xml`. Do not inline user-visible text in
    composables.

## When in doubt

Ask the user before:
- introducing a new dependency,
- changing a permission or foreground-service type,
- altering the service lifecycle or DI wiring,
- bumping `compileSdk` / `targetSdk` / `minSdk`,
- regenerating the signing config or changing the Play application id.
