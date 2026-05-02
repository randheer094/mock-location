# 03 — custom-coordinates

**Source:** `e2e/tests/03-custom-coordinates.test.ts` (4 tests)

## File-level pre-conditions (run before every test in this file)

1. Run `fixtures/device-setup.md` steps 1–8.
2. Run `fixtures/home.md` → `goToHome`.

## Helper: `dismissSheetIfOpen`

1. **MCP:** check `find_node(content_description = "Close")` with a 1s timeout — if not visible, return.
2. If visible: `click`.
3. **MCP:** `wait_until_not_visible(text = "Custom location", timeout_ms = 3000)`.

## Helper: `openCustomSheet`

1. Run `dismissSheetIfOpen`.
2. Run `fixtures/home.md` → `scrollToTop`.
3. Run `fixtures/home.md` → `stopIfMocking`.
4. **MCP:** `wait_until_visible(content_description = "New location", timeout_ms = 5000)`.
5. **MCP:** `find_node(content_description = "New location")` → `click`.
6. **MCP:** `assert_visible(text = "Custom location")`.

---

## Test 1: opens the bottom sheet

### Steps

1. Run `openCustomSheet` helper. (The helper's final `assert_visible` is the test.)

---

## Test 2: valid input starts mocking at the entered coordinates

Coordinates: SF — `lat = 37.7749`, `lng = -122.4194`.

### Steps

1. Run `openCustomSheet`.
2. **MCP:** `find_node(content_description = "Latitude input")` → `replace_text(text = "37.7749")`.
3. **MCP:** `find_node(content_description = "Longitude input")` → `replace_text(text = "-122.4194")`.
4. **MCP:** `find_node(text = "Set mock location")` → `click`.
5. **MCP:** `wait_until_not_visible(text = "Custom location", timeout_ms = 5000)`.
6. **MCP:** `find_node(text = "Start")` → `click`.
7. **MCP:** `wait_until_visible(text = "Stop broadcasting", timeout_ms = 10000)`.

8. **Poll until coords match** (5s timeout, ±0.01° tolerance):
   - **MCP:** `location_get_last_known()` → `{ lat, lng, ... }`.
   - **Assert:** `lat ≈ 37.7749` (±0.01) AND `lng ≈ -122.4194` (±0.01).

---

## Test 3: out-of-range latitude does not start mocking

### Steps

1. Run `openCustomSheet`.
2. **MCP:** `find_node(content_description = "Latitude input")` → `replace_text(text = "200")`.
3. **MCP:** `find_node(content_description = "Longitude input")` → `replace_text(text = "0")`.
4. **MCP:** `find_node(text = "Set mock location")` → `click`.
5. **MCP:** `wait_until_not_visible(text = "Stop broadcasting", timeout_ms = 2000)`.

   > Equivalent: the screen should **not** transition to the mocking-active
   > state. If `Stop broadcasting` ever appears, the test fails.

---

## Test 4: close button dismisses without starting mocking (ARC-155)

### Steps

1. Run `openCustomSheet`.
2. **MCP:** `find_node(content_description = "Close")` → `click`.
3. **MCP:** `wait_until_not_visible(text = "Custom location", timeout_ms = 2000)`.
4. **MCP:** `wait_until_not_visible(text = "Stop broadcasting", timeout_ms = 1000)`.
