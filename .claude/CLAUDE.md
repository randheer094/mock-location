# Mock Location for Developers

A single-activity Android app that mocks device location for
developers and QA. Users pick a city from a bundled list
(`assets/m_l.json`) or enter custom coordinates; a foreground
service pushes mocked `Location` fixes to `GPS_PROVIDER` and
`NETWORK_PROVIDER` until toggled off.

Play Store id: `dev.randheer094.dev.location` (debug variant
installs side-by-side as `...location.debug`).

## Build, test, run

- `./gradlew assembleDebug` — build a debug APK for every variant.
- `./gradlew test` — run unit tests across modules.
- `./gradlew connectedAndroidTest` — run instrumented / E2E tests
  on a connected device or emulator.
- `./gradlew lint` — Android lint.
- `./gradlew detekt` — Kotlin static analysis (if configured).
- `./gradlew ktlintCheck` — Kotlin style (if configured).

## Before a PR

Run the pre-PR gates documented in the project skill:
[skills/prepare-for-pr/SKILL.md](./skills/prepare-for-pr/SKILL.md).

## Conventions

Architecture (MVI + Hilt), test requirements, code style, and
module layout live in
[rules/conventions.md](./rules/conventions.md).
Read and follow them for every change.
