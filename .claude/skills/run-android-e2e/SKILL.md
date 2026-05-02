---
name: run-android-e2e
description: Use when the user asks to run the Android end-to-end suite for the mock-location project — phrases like "run android-e2e", "run the e2e tests", "smoke test the app", "run tests/02-preset-mocking", or asks to verify a specific UI / foreground-service / mock-location flow against a booted emulator.
---

# Run Android E2E (mock-location)

Black-box tests for the mock-location Android app, expressed as Markdown
runbooks executed via the `velocity-test-mobile` MCP server
(`velocity-test-mobile >= 0.3.0`). 17 tests across 7 files in
[`tests/`](tests/), with shared helpers in [`fixtures/`](fixtures/).

## Preflight — run these checks before walking any test

Run all three. Any failure: stop, report, do **not** start tests. The skill
does not boot the emulator or build the APK on the user's behalf.

1. **MCP connected.** `ToolSearch select:mcp__velocity-test-mobile__app_launch`
   returns a schema. Failure: `velocity-test-mobile` is not reachable — ask
   the user to check `claude mcp list`.
2. **Emulator booted.** Bash `adb devices -l` shows at least one
   `emulator-*` line in state `device`. Failure: ask the user to run
   `android emulator start <avd>`.
3. **Debug APK installed.** `mcp__velocity-test-mobile__app_list` contains
   `dev.randheer094.dev.location.debug`. Failure: ask the user to run
   ```bash
   ./gradlew :app:assembleDebug
   adb install -r -t -g app/build/outputs/apk/debug/app-debug.apk
   ```

## Mapping user requests → what to run

| User asks | What to do |
| --- | --- |
| "Run all e2e tests" / "run the suite" | Walk `tests/01..07.md` in order. Continue past failures; report a results table at the end. |
| "Run `tests/03-custom-coordinates.md`" | Walk every test in that file; report per-test pass/fail. |
| "Run test 2 of 02-preset-mocking" | Walk just that one test. |
| "Smoke test" | Run only `tests/06-permissions.md` (cheapest signal). |
| "Verify the foreground service" | Run `tests/05-foreground-service.md`. |
| "Verify mocking actually delivers coords" | Run `tests/02-preset-mocking.md` test 2 + `tests/03-custom-coordinates.md` test 2. |

## Per-test execution loop

For every test in a file:

1. Read the test file end-to-end, plus every fixture it references
   (`fixtures/device-setup.md`, `fixtures/home.md`,
   `fixtures/mock-app.md`).
2. Resolve schemas for unfamiliar MCP tools the first time you hit
   them via `ToolSearch select:mcp__velocity-test-mobile__<name>`.
3. Apply **Pre-conditions** in order. Most files reference
   `fixtures/device-setup.md → Standard pre-conditions`, which expands
   to steps 1–8 plus `home.md → goToHome`.
4. Walk **Steps** top-to-bottom. Each step is either an MCP tool call
   or a runbook helper — expand the helper inline when it appears.
5. On any failed assertion: capture `screen_capture` and `print_tree`
   for evidence, mark the test FAIL, **continue to the next test**.
6. Apply per-test **Cleanup** if listed.

## Standard timeouts

Picked once and reused across the suite. Raise locally if your device
flakes; do not lower without a reason.

| Timeout | Used for |
| --- | --- |
| `1000` ms | Negative assertions ("X must not appear"). |
| `3000` ms | Sheet dismiss, activity-top fast checks. |
| `5000` ms | Default `wait_until_visible`, `service_wait_for_state`, location/notification polls. |
| `10000` ms | Sheet-driven flows (custom-coordinate save → home transition). |
| `15000` ms | Settings app navigation (slow on cold start). |

## Failure-handling rules

- **Apparent transient** (RPC drop, single-step race, sheet half-open):
  re-run the failing test once. If it still fails, mark FAIL — do not
  retry a third time.
- **Setup screen unexpectedly visible on a non-01 test:** the appops
  grant didn't stick. Run `fixtures/mock-app.md → selectMockApp`'s UI
  fallback once (Settings → Developer options → Select mock location
  app → Mock Location for Developers (Debug)), then resume.
- **`Stop broadcasting` never appears after starting:** mock-app
  appops likely no-op'd on this device. Same fix as above.
- **`Mock location off` never appears after `goToHome`:** the
  rationale screen (POST_NOTIFICATIONS) is showing. Either the
  permission grant in `device-setup.md` step 3 silently failed, or
  the device requires runtime prompt. Grant manually and re-run.

## Reporting format

Always end the run with a results table the user can scan:

```
Test                                                 Result
tests/01-setup-screen.md test 1                      PASS
tests/01-setup-screen.md test 2                      PASS
tests/02-preset-mocking.md test 1                    PASS
tests/02-preset-mocking.md test 2                    PASS (lat=59.34 lng=18.05)
tests/02-preset-mocking.md test 3                    PASS
...
tests/05-foreground-service.md test 3                FAIL — step 5: service_wait_for_state foreground=true never reached
```

For each FAIL include: failing step number, the assertion that fired,
and a one-line excerpt from `screen_capture` / `print_tree` if it
clarifies the cause.

A full pass is ~10 minutes against a single emulator.

## Index of fixtures

- [`fixtures/constants.md`](fixtures/constants.md) — package id, default
  city (Stockholm), notification channel id, SF coordinates.
- [`fixtures/selectors.md`](fixtures/selectors.md) — every user-visible
  string a test asserts on, including the Unicode characters
  (curly apostrophe, middle dot, en-dash) that must match exactly.
- [`fixtures/device-setup.md`](fixtures/device-setup.md) — per-test
  reset (steps 1–8) + `ensureForegroundIsOurApp` retry helper.
- [`fixtures/mock-app.md`](fixtures/mock-app.md) — `selectMockApp` /
  `resetMockApp` (appops grant + UI fallback).
- [`fixtures/home.md`](fixtures/home.md) — `goToHome`, `scrollToTop`,
  `stopIfMocking`, `startMockingDefaultCity`.

## Source provenance

Each runbook documents its `e2e/tests/*.test.ts` source. When the
TypeScript suite under `e2e/` changes, update the matching runbook
here in the same change.

## What this skill does NOT do

- It does **not** build the APK or boot the emulator. Those are
  prerequisites — fail fast with a clear instruction if they're
  missing rather than running them silently.
- It does **not** modify the runbooks themselves. If a runbook is
  wrong, surface it to the user; don't patch tests to make them
  pass.
- It does **not** retry beyond once per test. Repeated failures
  signal a real defect or a stale device — escalate.
