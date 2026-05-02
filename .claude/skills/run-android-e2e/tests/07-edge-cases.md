# 07 — edge-cases

**Source:** `e2e/tests/07-edge-cases.test.ts` (1 test)

## File-level pre-conditions

Run `fixtures/device-setup.md` → **Standard pre-conditions**.

---

## Test 1: user can stop mocking after mock-app appops is revoked mid-run

Verifies that revoking `android:mock_location` _while_ mocking is active does
not break the Stop button. Service should still tear down cleanly.

### Steps

1. Run `fixtures/home.md` → `startMockingDefaultCity`.
2. **MCP:** `assert_visible(text = "Stop broadcasting")`.

3. **MCP:** `appops_set(bundle_id = "dev.randheer094.dev.location.debug", op = "android:mock_location", mode = "default")`.

   > Revokes the mock-app grant. The currently-running service is unaffected
   > by appops at this layer; the test guards against a future regression
   > where the service might assume the grant is still in place.

4. **MCP:** `find_node(text = "Stop broadcasting")` → `click`.

5. **MCP:** `service_wait_for_state(bundle_id = "dev.randheer094.dev.location.debug", expected = { foreground: false }, timeout_ms = 5000)`.

### Cleanup

The next test's pre-conditions re-grant `android:mock_location` via the
device-setup steps. If this is the last test in the run, leave it as the
session teardown.
