# mock-app

`appops`-based grant for `android:mock_location` plus a UI fallback. Mirrors
`e2e/fixtures/mockApp.ts`.

## selectMockApp

1. **MCP:** `appops_set(bundle_id = "dev.randheer094.dev.location.debug", op = "android:mock_location", mode = "allow")`.
2. **MCP:** `appops_get(bundle_id = "dev.randheer094.dev.location.debug", op = "android:mock_location")` — verify `mode == "allow"`.
3. If verification fails, fall back to UI:
   - **MCP:** `intent_send(action = "android.settings.APPLICATION_DEVELOPMENT_SETTINGS", delivery = "activity")`.
   - **MCP:** `find_node(text = "Select mock location app")` → `click`.
   - **MCP:** `find_node(text = "Mock Location for Developers (Debug)")` → `click`.
   - **MCP:** `press_key(key = "HOME")`.

> The UI fallback is rarely needed on stock AOSP emulators — `appops_set`
> works reliably. Tests that genuinely need the wizard visible call
> `resetMockApp` below instead.

## resetMockApp

Used by `tests/01-setup-screen.md` to revert appops back to default so the
wizard reappears.

1. **MCP:** `appops_set(bundle_id = "dev.randheer094.dev.location.debug", op = "android:mock_location", mode = "default")` (errors ignored).
