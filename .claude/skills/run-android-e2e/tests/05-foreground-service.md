# 05 — foreground-service

**Source:** `e2e/tests/05-foreground-service.test.ts` (3 tests)

## File-level pre-conditions (run before every test in this file)

1. Run `fixtures/device-setup.md` steps 1–8.
2. Run `fixtures/home.md` → `goToHome`.

---

## Test 1: ongoing notification is posted while mocking

### Steps

1. Run `fixtures/home.md` → `startMockingDefaultCity`.
2. **MCP:** `assert_visible(text = "Stop broadcasting")`.

3. **Poll until non-empty** (5s timeout):
   - **MCP:** `notification_list(bundle_id = "dev.randheer094.dev.location.debug", channel_id = "mock_location_channel")`.
   - **Assert:** result length ≥ 1.

---

## Test 2: tapping notification returns to MainActivity

### Steps

1. Run `fixtures/home.md` → `startMockingDefaultCity`.
2. **MCP:** `assert_visible(text = "Stop broadcasting")`.

3. **MCP:** `notification_tap(bundle_id = "dev.randheer094.dev.location.debug", channel_id = "mock_location_channel", title_match = "Mocking Location")`.

   > Equivalent expansion (kept for reference):
   > 1. `notification_shade_set(state = "expanded")`
   > 2. `find_node(text = "Mocking Location")` → `click`

4. **MCP:** `activity_wait_for_top(bundle_id = "dev.randheer094.dev.location.debug", activity = "dev.randheer094.dev.location.presentation.main.MainActivity", timeout_ms = 5000)`.

---

## Test 3: service survives task removal (`START_STICKY` + `stopWithTask=false`)

### Steps

1. Run `fixtures/home.md` → `startMockingDefaultCity`.
2. **MCP:** `assert_visible(text = "Stop broadcasting")`.

3. **MCP:** `app_terminate(bundle_id = "dev.randheer094.dev.location.debug", kind = "kill")`.

   > `kind = "kill"` issues `am kill` — a soft kill that simulates a
   > user-initiated task swipe. The default `kind = "force_stop"` would
   > prevent service restart and defeat the point of this test
   > (`START_STICKY` recovery).

4. **MCP:** `app_launch(bundle_id = "dev.randheer094.dev.location.debug")`.

5. **MCP:** `service_wait_for_state(bundle_id = "dev.randheer094.dev.location.debug", expected = { foreground: true }, timeout_ms = 5000)`.
