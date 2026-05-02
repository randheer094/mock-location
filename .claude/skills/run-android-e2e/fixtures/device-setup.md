# device-setup

Per-test reset and launch. Run before **every** test in `tests/`. Mirrors the
`device` fixture in `e2e/fixtures/device.ts` plus its post-launch
"force MainActivity to the front" defence against LeakCanary.

## Why this exists

`app_clear_data` wipes DataStore + runtime perms; granting POST_NOTIFICATIONS
and mock-app appops _before_ launch ensures the app's startup checks see the
mock-app authorised, so we never see a transient wizard frame.

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
