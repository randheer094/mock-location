# Handoff: Mock Location — UI Redesign

## Overview

A complete visual redesign of **Mock Location for Developers** (Android app, package `dev.randheer094.dev.location`), covering the main screen (both broadcasting and idle states), the custom-coordinates bottom sheet, and the first-run setup instructions. Light and dark modes are both specified.

The redesign moves the app from a generic Material 3 list layout to a more distinctive developer-tool aesthetic — a "Linear × MapKit for developers" direction — while keeping every feature and data point from the existing ViewModel/`UiStateMapper` intact. No new state fields are introduced; the redesign is purely presentational.

## About the Design Files

The files in this bundle are **design references created in HTML/React (JSX via Babel in-browser)**. They are prototypes showing intended look and behavior, **not production code to copy directly**.

The app is built with **Jetpack Compose + Material 3**. The task is to recreate these HTML designs in the existing Compose codebase, following the conventions already established in `app/src/main/java/dev/randheer094/dev/location/presentation/`:

- Keep `MockLocationViewModel` / `UiState` / `UiStateMapper` exactly as they are.
- Rebuild the composables under `presentation/mocklocation/composable/` to match the new visuals.
- Extend `presentation/theme/` with the new color tokens listed below.
- Do **not** port the JSX files into the Android project verbatim — they exist only to communicate the design.

## Fidelity

**High-fidelity (hifi).** All colors, type, spacing, radii, and micro-interactions are pinned. Implement pixel-close to the mockups. The one liberty open to the implementer is the "mini world map" decoration (see Component § `MiniMap`) — if a Compose equivalent of the stylized SVG map is disproportionate work, a simpler Canvas-drawn lat/lng grid + pin is acceptable.

### Revisions applied after initial review

- **No search bar.** The preset list is short (16 items); search removed from the Home screen entirely.
- **No top-bar history / settings buttons.** Usage was ambiguous; removed. The top bar is now only the wordmark on the left and a floating **+** (add custom location) button on the right.
- **No Pinned / Recent quick-action chips.** Dropped — they implied data surfaces the app doesn't have.
- **Custom coordinates** is entered via the top-right **+** accent button; opens the bottom sheet.

## Screens / Views

There are **three screens**, each rendered in light and dark mode inside a 380×800 phone shell on the design canvas.

---

### 1. Home — Broadcasting (ON state)

**Purpose:** Show the user that a mock location is actively being pushed to the system, with a single obvious "Stop" control and the full preset list still accessible below.

**Route:** `MockLocationScreen` when `UiState.status == true`.

**Layout (top → bottom):**

1. **Top bar** (`padding: 18px 20px 8px`)
   - Left: wordmark (`mock` + dim `location`) with a 30×30 accent-filled rounded square containing a pin glyph.
   - Right: **single 40×40 floating `+` button**, radius 12, `accent` fill, `accentInk` icon, soft accent shadow `0 8px 20px -8px liveGlow`. Taps open the Custom coordinates bottom sheet.

2. **Hero card** (`margin: 6px 16px 0; border-radius: 28; padding: 18px 18px 16px; 1px border`)
   - Subtle 165° linear gradient from `bgElev2` → `bgElev` (dark) or `#FFFFFF` → `#F8F6F1` (light).
   - **Status pill** (top-left): rounded 100px, `accentSoft` fill, `accentGhost` border, 7px dot in `live` color with a 3px glow, label `LIVE · GPS + NET` in 12/600/0.2.
   - **Timer readout** (top-right): JetBrains Mono 11px/600/1.2 tracking, `textDim`, uppercase — format `T+ 00:04:12`.
   - **Radar + caption row** (marginTop 14, gap 18):
     - `Radar` component (120×120): concentric rings, 90° sweep gradient rotating at 3.4s linear, pulsing center ring at 2.2s. Colors from `live` / `liveGlow`.
     - Caption block:
       - Eyebrow: `CURRENTLY SPOOFING` 11/600/1.6 tracking, `textDim`.
       - City name: Inter 26/600/-0.5 tracking (strip country from `name`).
       - Country: Inter 14/500, `textDim`.
       - Coords: JetBrains Mono 11, `textMute`, formatted as `1.2806° N   103.8501° E` (see `fmtCoord()` in `theme.jsx`).
   - **Stop button** (marginTop 16, 100% × 56, radius 16): `background: text`, `color: bg` — i.e. inverse of screen. Icon `stop` 18px + label `Stop broadcasting` 16/600/-0.2.

3. **Section header** `PRESET LOCATIONS` + sort affordance (padding `22px 20px 10px`).

4. **Preset list** (padding `0 16px`): `LocationRow` components. The active one (matches `selected`) uses `card` background + border. Each row:
   - 44×44 country-code tile (JetBrains Mono 13/700/0.5), `chipBg` or `accentSoft` when active.
   - City name + country (Inter 15/600/-0.2), with a small `LIVE` inline chip when active.
   - Monospace coords line.
   - 64×44 compact mini-map thumbnail on the right (zoomed to the coord).

---

### 2. Home — Idle (OFF state)

Same shell as above, but the hero card swaps:

- Status pill → neutral `Mock location off` with muted dot.
- Timer readout → `Ready` (muted).
- No radar. Instead: a **full-width stylized world map** (140–150px tall, radius 14) with a crosshair pin at the last-selected city's coords.
- Bottom row: `LAST USED` eyebrow + city name "<City>, <dim country>" on the left; a pill **Start** button on the right (accent background, accent-ink text, play icon, `0 10px 24px -8px liveGlow` shadow).

Everything below the hero (quick actions, search, list) is identical to the ON state.

---

### 3. Custom coordinates — Bottom sheet

**Purpose:** Add a custom lat/lng to the library.

**Route:** Overlaid on top of the idle Home screen when the user taps "Custom coords" / the pencil in the live selection.

**Layout:**

- Underlying screen is blurred (`filter: blur(2px); opacity: 0.35`) and a dark scrim sits between it and the sheet.
- Sheet docks to bottom: radius 28 on top corners, `bgElev` fill, `-20px` top shadow.
- 40×4 drag handle centered, then:
  - **Title row**: "Custom location" 22/700/-0.5 + 32×32 close button.
  - Subtitle: "Enter a label and coordinates. Saved to your library." 13px `textDim`.
  - **Name field**: label `NAME` 10/700/1.4 uppercase, 54-high input (radius 14, `chipBg` fill, 1px `borderStrong`), hint `A friendly label for your library.` 11px `textMute`.
  - **Latitude / Longitude row**: two equal-flex `Field` instances in monospace, with hints `−90 to 90` and `−180 to 180`.
  - **Validation strip** (radius 12): 1px `accentGhost` border, `accentSoft` fill, check icon, `accent` text — `Valid coordinates · Closest city: San Francisco, US`.
  - **CTA**: full-width 56-high accent button, `Set mock location`, pin icon.
  - **Tip line**: 12px `textMute` — `Tip: paste 37.7749, −122.4194 into any field`.

**Validation rules** (unchanged from existing code):
- Lat in `[-90, 90]`.
- Long in `[-180, 180]`.
- Dot decimal separator only.
- Disable CTA until both numeric fields parse; strip background/border switches to `danger` tone on error (design token below).

---

### 4. Setup instructions — First run

**Route:** Shown when `UiState.showInstructions == true`.

**Layout:**

- Top bar: left back icon button (40×40), center label `Setup · step 1 of 3` 13/600 `textDim`, right spacer for balance.
- Hero block (padding `24px 22px 10px`):
  - Accent pill `ONE-TIME SETUP` with info icon.
  - Headline: 34/700/-1.2 tracking, balanced wrap — **"Let Android know / we're the boss of GPS."** (second line in `textDim`).
  - Sub: 14px `textDim`, 1.5 line-height — "Android requires you to manually authorize a mock-location provider. Takes about 20 seconds."
- Three numbered **step cards** (radius 18, 1px border, `card` fill, padding 14, gap 10 between):
  - 32×32 tile with `01` / `02` / `03` in JetBrains Mono 14/700. The active (first) card uses `accent` fill, `accentInk` text; others use `chipBg`, `textDim`.
  - Title: 15/600/-0.2.
  - Body: 13px `textDim`, 1.5 line-height.
  - Optional monospace code block (radius 8, 1px dashed border, 11px JetBrains Mono) — only present on step 1 with the `adb` command.
- CTA cluster (padding `18px 16px 0`):
  - Primary: 54-high accent button — `Open developer options`, arrow-up-right icon.
  - Secondary: 48-high ghost button with 1px border — `I've done this — check again` (calls existing status recheck).

Map copy to existing strings:
- Headline → replace "Setup Instructions".
- Step titles match existing bullets in the live app (Settings → Developer options, Select mock location app, Choose this app).
- Primary CTA replaces "Got it!" but should still trigger the existing re-check path.

---

## Interactions & Behavior

| Interaction | Behavior |
|---|---|
| Tap **Stop broadcasting** | Call existing `toggleMocking(false)`; hero morphs from ON → OFF layout. Animate radar → map via cross-fade (250ms, standard easing). |
| Tap **Start** (idle hero) | `toggleMocking(true)` on currently-selected city. Hero transitions to ON layout. |
| Tap a **LocationRow** | Selects that row (existing `selectMockLocation`). If mocking is ON, coords swap live. Tap animates a 120ms scale-down to 0.98. |
| Tap **+** button (top right) | Open the Custom coordinates bottom sheet (spring: stiffness 400, damping 32). |
| Tap **✕** on sheet / backdrop | Dismiss sheet. |
| Tap **Set mock location** on sheet | Validate → save → dismiss → show toast `Added "<name>" to your library`. |
| Radar sweep | 3.4s linear infinite rotation. Only runs while mocking is ON. Pause when app is backgrounded. |
| Live center pulse | Ring grows r: 3 → 24, opacity 0.8 → 0, 2.2s infinite. |
| Active list row dot | Same 3px glow pulse as status pill. |
| ⌘K (search) keycap | Decorative only on Android; omit on the real implementation or keep as a visual cue. |

## State Management

**No new state fields.** All surfaces map 1:1 to the existing `UiState`:

- `status: Boolean` → ON vs OFF hero.
- `selected: MockLocation?` → powers hero caption + which list row is highlighted.
- `locations: List<MockLocation>` → preset list.
- `showInstructions: Boolean` → swap whole screen to Setup.
- `hasNotificationPermission: Boolean` → (existing rationale screen, not redesigned here — keep as-is or adapt the Setup look).

The "T+ 00:04:12" timer is a new display value. Derive it from a start timestamp saved when `toggleMocking(true)` is called (persist in DataStore under a new `mock_started_at: Long` key so it survives process death). Tick a `Flow<String>` every second in the ViewModel; bind to `UiState.elapsedLabel`.

## Design Tokens

All tokens in `components/theme.jsx` → `buildTheme({ dark, accentHue })`. Add to `presentation/theme/Color.kt` as `MockLightColors` / `MockDarkColors`.

### Colors — Dark mode

| Token | Value |
|---|---|
| `bg` | `#0A0E1A` |
| `bgElev` | `#121829` |
| `bgElev2` | `#1A2236` |
| `card` | `#141B2C` |
| `border` | `rgba(255,255,255,0.06)` |
| `borderStrong` | `rgba(255,255,255,0.12)` |
| `text` | `#EDF1F7` |
| `textDim` | `#9AA5BD` |
| `textMute` | `#5E6A83` |
| `accent` | `oklch(0.78 0.14 <hue>)` — default hue **220** (blue) |
| `accentInk` | `#04131A` |
| `accentSoft` | `oklch(0.78 0.14 <hue> / 0.14)` |
| `accentGhost` | `oklch(0.78 0.14 <hue> / 0.24)` |
| `live` | `oklch(0.78 0.16 <hue>)` |
| `liveGlow` | `oklch(0.78 0.16 <hue> / 0.35)` |
| `mapBg` | `#0B1322` |
| `mapLine` | `rgba(140,180,220,0.08)` |
| `mapLineStrong` | `rgba(140,180,220,0.14)` |
| `danger` | `#FF7A85` |
| `chipBg` | `rgba(255,255,255,0.04)` |

### Colors — Light mode

| Token | Value |
|---|---|
| `bg` | `#F3F1EC` |
| `bgElev` | `#FFFFFF` |
| `bgElev2` | `#F8F6F1` |
| `card` | `#FFFFFF` |
| `border` | `rgba(11,16,32,0.07)` |
| `borderStrong` | `rgba(11,16,32,0.13)` |
| `text` | `#0B1020` |
| `textDim` | `#55607A` |
| `textMute` | `#8A93A8` |
| `accent` | `oklch(0.55 0.14 <hue>)` |
| `accentInk` | `#FFFFFF` |
| `accentSoft` | `oklch(0.55 0.14 <hue> / 0.10)` |
| `accentGhost` | `oklch(0.55 0.14 <hue> / 0.18)` |
| `live` | `oklch(0.55 0.16 <hue>)` |
| `liveGlow` | `oklch(0.55 0.16 <hue> / 0.28)` |
| `mapBg` | `#EAE7DE` |
| `mapLine` | `rgba(11,16,32,0.05)` |
| `mapLineStrong` | `rgba(11,16,32,0.10)` |
| `danger` | `#C64454` |
| `chipBg` | `rgba(11,16,32,0.04)` |

> Kotlin note: Compose 1.7 does not ship OKLCH. Either use `androidx.compose.ui.graphics.Color.hsl()` with the closest HSL approximation, or precompute the hex values for the app's chosen accent hue(s) and register them as constants. Defaulting to hue **220** yields approximately `#6AB8FF` (dark mode) / `#1B7CC9` (light mode) — use a tool like [oklch.com](https://oklch.com/) to verify.

### Typography

Two families only:

- **Inter** (400, 500, 600, 700, 800). All UI.
- **JetBrains Mono** (400, 500, 600, 700). Coordinates, timers, keycap labels, code blocks.

Type scale (size / weight / tracking):

| Role | Spec |
|---|---|
| Display (setup headline) | 34 / 700 / -1.2 / lh 1.04 |
| H1 (hero city) | 26 / 600 / -0.5 / lh 1.08 |
| H2 (sheet title, card titles) | 22 / 700 / -0.5 |
| Body-emph | 16 / 600 / -0.2 |
| Body | 14 / 500 / 0 / lh 1.5 |
| Label | 13 / 600 / -0.1 |
| Small | 12 / 500 |
| Overline | 11 / 700 / 1.6 uppercase |
| Micro (tips) | 11 / 500 |
| Mono readout | JetBrains Mono 11 / 500 / 0 |

### Spacing

Base unit **4px**. Standard increments: 4, 6, 8, 10, 12, 14, 16, 18, 20, 24, 28.

### Radii

| Role | Value |
|---|---|
| Input field | 14 |
| Button, card inner tiles | 16 |
| Primary card / hero | 28 |
| Pill / status | 100 |
| Small chip (validation, keycap) | 5–12 |

### Shadows

- Hero stop button: none — relies on contrast.
- Primary accent button: `0 12px 30px -10px liveGlow` (light mode) / `0 10px 24px -8px liveGlow` (dark).
- Phone device frame (design canvas only, not the app): `0 40px 100px -20px rgba(...)`.

### Easing / motion

- Sheet spring: stiffness 400, damping 32.
- Row tap: 120ms ease-out, scale 0.98.
- Hero state crossfade: 250ms.
- Radar sweep: 3.4s linear infinite.
- Live pulse: 2.2s ease-in-out infinite.

## Components

| Component | File | Responsibility |
|---|---|---|
| `buildTheme` | `components/theme.jsx` | Produces a token object for light/dark + accent hue. |
| `fmtCoord` | `components/theme.jsx` | Formats a signed decimal into `1.2806° N`. |
| `LOCATIONS` | `components/theme.jsx` | Reference copy of the bundled city list (sourced from `app/src/main/assets/m_l.json`). |
| `Icon` | `components/icons.jsx` | Stroke-based icon set. Port to Compose `ImageVector`s. |
| `MiniMap` | `components/map.jsx` | Stylized world map SVG with grid + continent blobs + crosshair pin. Has a `compact` mode for list thumbnails. |
| `Radar` | `components/map.jsx` | Concentric rings + sweeping needle + pulsing center. ON-state hero only. |
| `HomeScreen` | `components/home.jsx` | Full screen composition for ON/OFF. |
| `Wordmark`, `IconButton`, `StatusPill`, `QuickChip`, `LocationRow` | `components/home.jsx` | Sub-components. |
| `CustomSheetScreen`, `Field` | `components/screens.jsx` | Bottom sheet and form field primitive. |
| `SetupScreen` | `components/screens.jsx` | First-run setup. |

## Assets

- **Icons**: all vector-defined in `components/icons.jsx` (pin, play, stop, pencil, plus, search, chevron, check, x, settings, star, compass, history, bolt, arrow-up-right, info, layers, gauge). No raster assets.
- **Map silhouettes**: SVG paths embedded in `components/map.jsx` (variable `CONTINENT_BLOBS`). Stylized, not geographically accurate — intentionally so.
- **Fonts**: Inter and JetBrains Mono — both on Google Fonts. Add Gradle dependencies `androidx.compose.ui:ui-text-google-fonts` or ship as bundled assets in `app/src/main/res/font/`.
- No changes to `ic_launcher`, `ic_location`, or any existing drawable.

## Files in this bundle

```
design_handoff_mock_location_redesign/
├── README.md                       ← this file
├── Mock Location.html              ← entry point; open in a browser to see the canvas
├── components/
│   ├── theme.jsx                   ← all color / spacing tokens + data
│   ├── icons.jsx                   ← icon set
│   ├── map.jsx                     ← MiniMap + Radar
│   ├── home.jsx                    ← Home screen (ON + OFF) + primitives
│   └── screens.jsx                 ← Custom sheet + Setup
└── frames/
    ├── design-canvas.jsx           ← canvas shell (presentation harness, not app UI)
    └── android-frame.jsx           ← unused reference Android frame
```

## Where to plug this in, in the Android project

| Old composable / file | New equivalent |
|---|---|
| `presentation/theme/Color.kt` + `Theme.kt` | Add `MockLightColors`, `MockDarkColors` constants from this doc. Keep `MaterialTheme` but override via `CompositionLocal` providing the token object. |
| `presentation/mocklocation/composable/MockLocationScreen` (main list) | Rebuild as `HomeScreen` with `state` derived from `uiState.status`. |
| `presentation/mocklocation/composable/MockLocationNStatus` | Replaced by the Hero card (ON + OFF variants). |
| `presentation/mocklocation/composable/AddMockLocationBottomSheet` | Replaced by Custom sheet layout. |
| `presentation/mocklocation/composable/SetupInstruction` | Replaced by `SetupScreen`. |
| `presentation/mocklocation/composable/Location` (row) | Replaced by `LocationRow`. Country code derived from last comma-split token via a small `COUNTRY_CODE` map (see `theme.jsx`). |
| `presentation/mocklocation/composable/SectionHeader` | Retained; restyle to `OVERLINE` 11/700/1.6 spec. |

## Open questions for the implementer

1. **MiniMap fidelity**: is a stylized SVG-equivalent on Compose `Canvas` worth the effort, or use a flat accent-tinted rectangle as a placeholder thumbnail? (Recommendation: ship the Canvas version; it's the most distinctive brand moment.)
2. **Timer persistence**: confirm that tracking "time since mocking started" is fine to add to DataStore — otherwise drop it and the timer slot in the hero.
3. **Pinned / Recent quick actions**: these are new surfaces. Ship them as disabled placeholders in v1, or de-scope until the data model supports them.
4. **⌘K search**: keyboard-only affordance on Android doesn't make sense; propose replacing the keycap with a tiny `Filter` icon, or leaving the search row non-interactive in v1.
