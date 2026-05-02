# device-setup

Per-test reset and launch. Run before **every** test in `tests/`. Mirrors the
`device` fixture in `e2e/fixtures/device.ts` plus its post-launch
"force MainActivity to the front" defence against LeakCanary.

## Why this exists

`app_clear_data` wipes DataStore + runtime perms; granting POST_NOTIFICATIONS
and mock-app appops _before_ launch lets the app's setup-instruction gate
(`SetupInstructionStatusUseCase`) read `MODE_ALLOWED` on the first DataStore
emission and skip the wizard. The gate combines a stored "force-show" flag
(default `false`) with a live `AppOpsManager` check, so the wizard is hidden
on cold launch when appops is allowed and shown otherwise — no "check again"
tap required for the happy path.

`activity_start MainActivity` after `app_launch` defeats LeakCanary's
`CATEGORY_LAUNCHER` activity, which can win launcher resolution after a leaky
earlier test.

## Steps (run before every test)

1. **MCP:** `notification_shade_set(state = "collapsed")` — close any leftover shade.
2. **MCP:** `app_clear_data(bundle_id = "dev.randheer094.dev.location.debug")`.
3. **MCP:** `permission_grant(bundle_id = "dev.randheer094.dev.location.debug", permission = "android.permission.POST_NOTIFICATIONS")`.
4. **MCP:** `appops_set(bundle_id = "dev.randheer094.dev.location.debug", op = "android:mock_location", mode = "allow")`.
5. **MCP:** `app_terminate(bundle_id = "dev.randheer094.dev.location.debug")`.
6. **MCP:** `app_launch(bundle_id = "dev.randheer094.dev.location.debug")`.
7. **MCP:** `activity_start(bundle_id = "dev.randheer094.dev.location.debug", activity = "dev.randheer094.dev.location.presentation.main.MainActivity")` — pin our activity foreground.
8. **MCP:** `wait_for_idle` — let the activity transition settle.

## Standard pre-conditions

Most test files (`tests/02..05`, `tests/07`) reference this block instead of
spelling the pair out:

1. Run **Steps** above (1–8).
2. Run `fixtures/home.md` → `goToHome`.

`tests/01` overrides this (it wants the wizard visible) and `tests/06` uses
**Steps** alone (it exercises `goToHome` inside the test). Both spell out
their own pre-conditions.

## Cleanup (after every test)

There is no per-test teardown beyond what step 1–6 of the next test will do. For
the **last** test in a run, also run
`appops_set(bundle_id = "dev.randheer094.dev.location.debug", op = "android:mock_location", mode = "default")`
to mirror `e2e/globalTeardown.ts`.

## ensureForegroundIsOurApp (helper, used by `home.md`)

Up to 3 attempts:

1. **MCP:** `activity_get_top()` → returns `{ bundle_id, activity }` or `null`.
2. If `bundle_id == "dev.randheer094.dev.location.debug"` AND
   `activity == "dev.randheer094.dev.location.presentation.main.MainActivity"`
   → done.
3. Otherwise: **MCP:** `activity_start(bundle_id = "dev.randheer094.dev.location.debug", activity = "dev.randheer094.dev.location.presentation.main.MainActivity")`, then `wait_for_idle`.
4. After 3 unsuccessful attempts, fail loudly — the device is in a bad state.

> Equivalent one-shot: `activity_wait_for_top(bundle_id = "...", activity = "...presentation.main.MainActivity", timeout_ms = 3000)`.
> The retry loop above is preserved because it also re-invokes `activity_start`
> on miss, which the `wait_for_top` poll does not.
