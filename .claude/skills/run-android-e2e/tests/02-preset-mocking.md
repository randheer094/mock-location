# 02 — preset-mocking

**Source:** `e2e/tests/02-preset-mocking.test.ts` (3 tests)

## File-level pre-conditions (run before every test in this file)

1. Run `fixtures/device-setup.md` steps 1–8.
2. Run `fixtures/home.md` → `goToHome`.

---

## Test 1: tap preset transitions home to mocking-active state

### Steps

1. Run `fixtures/home.md` → `startMockingDefaultCity`.
2. **MCP:** `assert_visible(text = "Stop broadcasting")`.

---

## Test 2: mocked location is delivered to LocationManager

### Steps

1. Run `fixtures/home.md` → `startMockingDefaultCity`.
2. **MCP:** `assert_visible(text = "Stop broadcasting")`.

3. **MCP:** `service_wait_for_state(bundle_id = "dev.randheer094.dev.location.debug", expected = { foreground: true }, timeout_ms = 5000)`.

4. **Poll until coords match** (5s timeout, ±0.01° tolerance per axis):
   - **MCP:** `location_get_last_known()` → `{ provider, lat, lng, ... }`.
   - **Assert:** `lat ≈ 59.3383223` (±0.01) AND `lng ≈ 18.0549621` (±0.01).

   > The `±0.01` tolerance matches Playwright's `closeTo(..., 2)` (decimal
   > places). The mock provider rounds; exact equality fails.

---

## Test 3: stop returns to idle and tears down the service

### Steps

1. Run `fixtures/home.md` → `startMockingDefaultCity`.
2. **MCP:** `find_node(text = "Stop broadcasting")` → `click`.
3. **MCP:** `service_wait_for_state(bundle_id = "dev.randheer094.dev.location.debug", expected = { foreground: false }, timeout_ms = 5000)`.

   > The package may still appear in `service_get_state` for a beat after
   > `stopSelf()`; the absence of `foreground: true` is the contract.
