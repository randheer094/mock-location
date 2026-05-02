# home

Common navigation primitives shared across `tests/02..07`. Mirrors
`e2e/fixtures/home.ts`.

## goToHome

Dismiss the one-time setup wizard if visible, then wait for a stable home-screen
marker. No-op when already dismissed.

Up to 3 attempts:

1. Run `ensureForegroundIsOurApp` (see `device-setup.md`).
2. **MCP:** `wait_for_idle` (give recomposition a beat to settle).
3. **MCP:** check `find_node(text = "I've done this — check again")` with a
   short timeout — if visible, `click` it.
4. **MCP:** `wait_until_visible(text = "Mock location off", timeout_ms = 5000)`.
   If satisfied → return.
5. After 3 unsuccessful attempts, **assert** `assert_visible(text = "Mock location off")` — fail with a clear error.

## scrollToTop

Two `swipe('down')` gestures on the home list. The home screen scrolls; the
easiest way to reach the top is to fling-down twice.

1. **MCP:** `swipe_node` on the root scrollable, direction `down`. Twice.

> If the screen has a single scrollable, `scroll_to(node = ..., to = "top")` is
> equivalent.

## stopIfMocking

If "Stop broadcasting" is visible, tap it and wait for "Start" to appear.

1. **MCP:** `find_node(text = "Stop broadcasting")` with a 1s timeout — if
   not found, return early.
2. If found: `click` it.
3. **MCP:** `wait_until_visible(text = "Start", timeout_ms = 5000)`.

## startMockingDefaultCity

Tap the Stockholm preset and start mocking.

1. Run `ensureForegroundIsOurApp`.
2. Run `scrollToTop`.
3. Run `stopIfMocking`.
4. **MCP:** `scroll_to(text = "Stockholm")` — bring the preset into view.
5. **MCP:** `find_node(text = "Stockholm")` → `click`.
6. Run `scrollToTop`.
7. **MCP:** `find_node(text = "Start")` → `click`.

> The default city label is `"Stockholm"` (the preset list strips the country
> suffix from `"Stockholm, Sweden"`).
