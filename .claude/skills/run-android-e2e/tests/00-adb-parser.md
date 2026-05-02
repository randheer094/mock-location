# 00 — adb-parser (legacy reference)

The original `e2e/` suite shipped a regex parser over `dumpsys location`
output. That parser is now **obsolete in this suite** — `location_get_last_known`
returns a structured `{ provider, lat, lng, ... }` directly.

This file is kept solely for 1:1 traceability with `e2e/tests/00-adb-parser.test.ts`.

**Source:** `e2e/tests/00-adb-parser.test.ts`

The parser (legacy):

```
LAST_LOCATION_RE = /last location=Location\[\w+ (-?\d+\.\d+),(-?\d+\.\d+)/
parseLastLocation(stdout) → match ? { lat: float, lng: float } : null
```

## Test 1: extracts lat/lng from a typical dumpsys block

**Input:**

```
gps provider:
  last location=Location[gps 59.338300,18.054970 hAcc=5.0 et=...]
```

**Expected:** `{ lat: 59.3383, lng: 18.05497 }`

## Test 2: returns null when no last location

**Input:** `gps provider: enabled`

**Expected:** `null`

## How an MCP-driven agent should treat this

Skip. The parser is exercised by `e2e/tests/00-adb-parser.test.ts` (Node, not
device-driven). MCP runbooks call `location_get_last_known()` directly — there
is no string to parse here.

If you want to verify the legacy parser by hand without the TS suite:

```bash
node -e '
const RE = /last location=Location\[\w+ (-?\d+\.\d+),(-?\d+\.\d+)/;
const parse = s => { const m = RE.exec(s); return m ? { lat: parseFloat(m[1]), lng: parseFloat(m[2]) } : null; };
console.log(parse("last location=Location[gps 59.338300,18.054970 hAcc=5.0]"));
console.log(parse("gps provider: enabled"));
'
```
