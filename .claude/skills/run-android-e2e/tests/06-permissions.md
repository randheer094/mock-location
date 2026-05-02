# 06 — permissions

**Source:** `e2e/tests/06-permissions.test.ts` (1 test)

The standard `device-setup` already grants `POST_NOTIFICATIONS` _before_ launch,
so this test is implicitly covered every time `goToHome` lands on the home
screen instead of a rationale screen. The explicit assertion proves it.

## File-level pre-conditions

1. Run `fixtures/device-setup.md` steps 1–8.

---

## Test 1: pre-grant skips the rationale screen

### Steps

1. Run `fixtures/home.md` → `goToHome`. (No rationale screen should appear; if
   it does, `goToHome` will hang on `wait_until_visible("Mock location off")`
   and time out.)
2. **MCP:** `assert_visible(text = "Sort · A–Z")`.

   > The home screen rendered means we cleared notifications-rationale-without-prompt.
