# adb (legacy reference)

This file documented `adb shell` wrappers used by the original `e2e/`
TypeScript suite. With `velocity-test-mobile >= 0.3.0` every helper here has a
first-class MCP equivalent — these runbooks no longer need shell escapes.

Kept for reference and 1:1 traceability against `e2e/fixtures/adb.ts`.

## Mapping

| `adb.ts` helper | MCP replacement |
| --- | --- |
| `parseLastLocation(stdout)` | (obsolete) — `location_get_last_known()` returns `{ provider, lat, lng, ... }` directly. |
| `isForegroundServiceRunning(pkg)` | `service_get_state(bundle_id = pkg)` → `.foreground` |
| `lastMockedLocation()` | `location_get_last_known()` |
| `notificationVisible(channelId)` | `notification_list(channel_id = ...)`.length > 0 |
| `killApp(pkg)` | `app_terminate(bundle_id = pkg)` (default `kind = "force_stop"`) |
| `swipeFromRecents(pkg)` | `app_terminate(bundle_id = pkg, kind = "kill")` |
| `broadcastStopAction()` | `intent_send(action = "dev.randheer094.dev.location.action.STOP", component = { bundle_id = "...", class = "...presentation.service.MockLocationService" }, delivery = "service")` |
| `appopsAllowMockLocation()` | `appops_set(bundle_id, op = "android:mock_location", mode = "allow")` |
| `appopsResetMockLocation()` | `appops_set(bundle_id, op = "android:mock_location", mode = "default")` |
| `grantPostNotifications()` | `permission_grant(bundle_id, permission = "android.permission.POST_NOTIFICATIONS")` |
| `revokePostNotifications()` | `permission_revoke(bundle_id, permission = "android.permission.POST_NOTIFICATIONS")` |
| `topResumedActivity()` | `activity_get_top()` |

## Soft kill (`am kill`) — resolved

`am kill <pkg>` is materially different from `force-stop`. The current
`app_terminate` exposes a `kind` parameter:

- `kind = "force_stop"` (default) — issues `am force-stop`. Prevents the
  service from restarting until the user re-launches.
- `kind = "kill"` — issues `am kill`. Simulates a task swipe; `START_STICKY`
  services come back.

`tests/05-foreground-service.md` test 3 uses `kind = "kill"`.
