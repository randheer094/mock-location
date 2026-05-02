# 04 — sort-order

**Source:** `e2e/tests/04-sort-order.test.ts` (2 tests)

Covers ARC-156 / ARC-157: default A–Z label and the toggle behaviour.

## File-level pre-conditions (run before every test in this file)

Run `fixtures/device-setup.md` → **Standard pre-conditions**.

---

## Test 1: default order shows A–Z sort label

### Steps

1. **MCP:** `assert_visible(text = "Sort · A–Z")`.

   > Note the middle dot (U+00B7) and en-dash (U+2013). Match exactly.

---

## Test 2: toggling sort flips the label to Z–A

### Steps

1. **MCP:** `assert_visible(text = "Sort · A–Z")`.
2. **MCP:** `find_node(text = "Sort · A–Z")` → `click`.
3. **MCP:** `assert_visible(text = "Sort · Z–A")`.
