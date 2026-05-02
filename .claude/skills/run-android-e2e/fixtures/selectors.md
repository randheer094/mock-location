# selectors

Every user-visible string a test references, kept in one place so a `strings.xml`
diff lands here in one place. Mirrors `e2e/fixtures/selectors.ts`.

When `strings.xml` changes, update this file _and_ the corresponding entry in
`e2e/fixtures/selectors.ts`.

## Setup screen

| Key | Value |
| --- | --- |
| `headlineLine1` | `Let Android know` |
| `headlineLine2` | `we're the boss of GPS.` (curly apostrophe — U+2019) |
| `step1Title` | `Open Developer Options` |
| `step2Title` | `Find "Select mock location app"` |
| `step3Title` | `Pick Mock Location` |
| `openDevOptionsCta` | `Open developer options` (use as `content_description` — see note) |
| `checkAgainCta` | `I've done this — check again` (curly apostrophe — U+2019) |

> **Note on `Open Developer Options` vs `Open developer options`** — the step-1
> card title and the CTA label collide under case-insensitive prefix matching
> in mobilewright. Resolve by content_description (`find_node(content_description = "Open developer options")`).

## Home

| Key | Value |
| --- | --- |
| `wordmarkMock` | `mock` |
| `wordmarkLocation` | `location` |
| `statusReady` | `Ready` |
| `statusMockOff` | `Mock location off` |
| `statusLive` | `LIVE · GPS + NET` (middle dot — U+00B7) |
| `presetSection` | `PRESET LOCATIONS` |
| `sortToggleAZ` | `Sort · A–Z` (middle dot + en-dash) |
| `sortToggleZA` | `Sort · Z–A` |
| `addLocationFab` | `New location` (use as `content_description` on the FAB) |
| `stopBroadcastingCta` | `Stop broadcasting` |
| `startCta` | `Start` |

## Custom-coordinates bottom sheet

| Key | Value |
| --- | --- |
| `title` | `Custom location` |
| `nameLabel` | `NAME` |
| `latitudeLabel` | `LATITUDE` |
| `longitudeLabel` | `LONGITUDE` |
| `latitudeInputCd` | `Latitude input` (content_description on the OutlinedTextField) |
| `longitudeInputCd` | `Longitude input` (content_description on the OutlinedTextField) |
| `saveCta` | `Set mock location` |
| `closeCd` | `Close` (content_description on the close icon) |
| `validationLatOutOfRange` | `Latitude out of range` |
| `validationLngOutOfRange` | `Longitude out of range` |

> **Why content_description for the lat/lng fields?** EditText nodes are not
> surfaced reliably in the accessibility tree; the OutlinedTextField wrappers
> carry an explicit `Modifier.semantics { contentDescription = ... }`.

## Notification

| Key | Value |
| --- | --- |
| `title` | `Mocking Location` |

## Permission rationale

| Key | Value |
| --- | --- |
| `title` | `Notification Permission required` |
| `cta` | `Allow Notifications!` |
