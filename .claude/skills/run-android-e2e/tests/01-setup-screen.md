# 01 — setup-screen

**Source:** `e2e/tests/01-setup-screen.test.ts` (2 tests)

## File-level pre-conditions (run before every test in this file)

The standard reset (`fixtures/device-setup.md`) leaves mock-app **granted**, so
the wizard would not be visible. Override per-test:

1. Run `fixtures/device-setup.md` steps 1–8.
2. Run `fixtures/mock-app.md` → `resetMockApp` (revoke `android:mock_location`).
3. **MCP:** `app_terminate(bundle_id = "dev.randheer094.dev.location.debug")`.
4. **MCP:** `activity_start(bundle_id = "dev.randheer094.dev.location.debug", activity = "dev.randheer094.dev.location.presentation.main.MainActivity")`.
5. **MCP:** `wait_for_idle`.

---

## Test 1: shows setup wizard when mock-app is not selected

### Steps

1. **MCP:** `assert_visible(text = "Let Android know")`.
2. **MCP:** `assert_visible(text = "Open Developer Options")`.
3. **MCP:** `assert_visible(text = "Find \"Select mock location app\"")`.
4. **MCP:** `assert_visible(text = "Pick Mock Location")`.

If any assertion fails the test fails.

---

## Test 2: Open Settings CTA dispatches developer options intent

### Steps

1. **MCP:** `wait_until_visible(content_description = "Open developer options", timeout_ms = 15000)`.

   > The card title `"Open Developer Options"` collides with the CTA label
   > under case-insensitive prefix matching. Always disambiguate via
   > `content_description` (`Modifier.semantics` on the button).

2. **MCP:** `find_node(content_description = "Open developer options")` → `click`.
3. **MCP:** `activity_wait_for_top(bundle_id = "com.android.settings", timeout_ms = 5000)`.

### Cleanup

The next test's pre-conditions reset everything, so no explicit cleanup. If this
is the last test in the run, navigate back with `press_key(key = "HOME")` so the
next session does not start in Settings.
