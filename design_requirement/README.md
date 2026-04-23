# Handoff: Mock Location — Android UI Redesign

## Overview

A visual redesign of **Mock Location for Developers** — an **Android app** (package `dev.randheer094.dev.location`, min SDK per `app/build.gradle.kts`). Covers the three user-facing surfaces: Home (broadcasting + idle), the Custom coordinates bottom sheet, and the first-run Setup instructions. Light (**DayNight**) and dark mode both specified.

The redesign keeps every feature and every data point of the existing `MockLocationViewModel` / `UiState` / `UiStateMapper` intact. **Presentation layer only** — no ViewModel, domain, DI, or service changes required.

## Platform

- **Android only.** Min SDK and target SDK follow the existing project (API 36 target).
- **Jetpack Compose + Material 3** (`androidx.compose.material3`). The app already uses Compose with a DayNight Material theme — this redesign extends that theme, it does not replace the framework.
- **Single activity** (`MainActivity`), Compose as the content root, edge-to-edge enabled.

## About the Design Files

The files in this bundle are **HTML/React prototypes**. They exist purely to communicate the visual target; they are **not production code** and should not be ported verbatim.

Implementation target is the existing Compose codebase under:

```
app/src/main/java/dev/randheer094/dev/location/presentation/
├── mocklocation/composable/   ← rebuild composables here
├── theme/                     ← extend tokens here
└── ...
```

Recreate the designs using **standard Compose + Material 3** primitives, Compose animation APIs, and `androidx.compose.foundation.Canvas` for custom drawing (radar, stylized map). Do not add a web view, a different UI toolkit, or any cross-platform layer.

## Fidelity

**High-fidelity.** All colors, typography, spacing, radii, and motion are pinned to the values in this document. Implement pixel-close. The one liberty open to the implementer: the stylized world-map illustration (see `MiniMap` below) — a simpler Canvas-drawn lat/lng grid with a pin is acceptable if the continent blob fidelity is disproportionate work.

### Revisions applied after initial team review

- **No search bar** — preset list is short (16 entries).
- **No top-bar icon buttons** (history, settings) — usage was ambiguous.
- **No Pinned / Recent quick-action chips** — implied data surfaces the app doesn't have.
- **Add custom location** happens via a single floating **+** button at the top right of Home, which opens the bottom sheet.

---

## Material 3 mapping

This redesign extends Material 3, it does not fight it. Anything not explicitly listed as custom below should use the M3 default from `MaterialTheme`.

| Design piece | Material 3 counterpart | Notes |
|---|---|---|
| Hero card | `Surface` / `Card` | Custom 28dp radius (bigger than `Shapes.extraLarge` default). |
| Stop / Start buttons | `Button` (filled) | 56dp height instead of the 40dp default. Custom shape 16dp. |
| Floating **+** top-right | `FilledIconButton` | Tinted with custom `accent`. 40dp × 40dp, 12dp radius. |
| Status pill | `AssistChip` / custom `Surface` | Rounded 100.dp, custom fill/border. M3 chip defaults are acceptable if themed to match tokens. |
| Bottom sheet | `ModalBottomSheet` (M3) | Drag handle on, scrim on, top corners 28dp. |
| Text fields on sheet | `OutlinedTextField` | Border matches `borderStrong`, 14dp shape. Use `KeyboardOptions(keyboardType = KeyboardType.Number)` on lat/lng. |
| List rows | `ListItem` (M3) or custom `Row` | Custom layout (country-code tile + mini map thumbnail) doesn't fit `ListItem` cleanly — prefer a custom `Row` inside a `Surface`. |
| Section header | Custom `Text` | 11sp / `FontWeight.Bold` / `letterSpacing = 1.6.sp` / uppercase. |
| Primary CTA on Setup | `Button` | Same custom 16dp shape, 54dp height. |
| Secondary CTA on Setup | `OutlinedButton` | 48dp height, 14dp shape. |

**Shapes override**: add a custom `Shapes` to `MaterialTheme`:

```kotlin
val MockShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small      = RoundedCornerShape(12.dp),
    medium     = RoundedCornerShape(14.dp),   // inputs
    large      = RoundedCornerShape(16.dp),   // buttons, card inner tiles
    extraLarge = RoundedCornerShape(28.dp),   // hero card, sheet
)
```

---

## Theme tokens

Add these to `presentation/theme/` as `MockLightColors` / `MockDarkColors` data holders, and expose through a `LocalMockColors` `CompositionLocal` so composables can read them without passing props. `MaterialTheme.colorScheme` still drives M3 defaults; `LocalMockColors.current` drives the accents, dim text levels, and map colors that M3's `ColorScheme` doesn't cover.

### Dark mode

| Token | Value | Kotlin |
|---|---|---|
| `bg` | `#0A0E1A` | `Color(0xFF0A0E1A)` |
| `bgElev` | `#121829` | `Color(0xFF121829)` |
| `bgElev2` | `#1A2236` | `Color(0xFF1A2236)` |
| `card` | `#141B2C` | `Color(0xFF141B2C)` |
| `border` | `rgba(255,255,255,0.06)` | `Color.White.copy(alpha = 0.06f)` |
| `borderStrong` | `rgba(255,255,255,0.12)` | `Color.White.copy(alpha = 0.12f)` |
| `text` | `#EDF1F7` | `Color(0xFFEDF1F7)` |
| `textDim` | `#9AA5BD` | `Color(0xFF9AA5BD)` |
| `textMute` | `#5E6A83` | `Color(0xFF5E6A83)` |
| `accent` | `oklch(0.78 0.14 220)` ≈ `#6AB8FF` | `Color(0xFF6AB8FF)` |
| `accentInk` | `#04131A` | `Color(0xFF04131A)` |
| `accentSoft` | `accent @ 14%` | `accent.copy(alpha = 0.14f)` |
| `accentGhost` | `accent @ 24%` | `accent.copy(alpha = 0.24f)` |
| `live` | same hue, chroma 0.16 | `Color(0xFF5CB8FF)` (approx) |
| `liveGlow` | `live @ 35%` | `live.copy(alpha = 0.35f)` |
| `mapBg` | `#0B1322` | `Color(0xFF0B1322)` |
| `mapLine` | `rgba(140,180,220,0.08)` | — |
| `mapLineStrong` | `rgba(140,180,220,0.14)` | — |
| `danger` | `#FF7A85` | `Color(0xFFFF7A85)` |
| `chipBg` | `rgba(255,255,255,0.04)` | `Color.White.copy(alpha = 0.04f)` |

### Light mode

| Token | Value | Kotlin |
|---|---|---|
| `bg` | `#F3F1EC` | `Color(0xFFF3F1EC)` |
| `bgElev` | `#FFFFFF` | `Color.White` |
| `bgElev2` | `#F8F6F1` | `Color(0xFFF8F6F1)` |
| `card` | `#FFFFFF` | `Color.White` |
| `border` | `rgba(11,16,32,0.07)` | `Color(0xFF0B1020).copy(alpha = 0.07f)` |
| `borderStrong` | `rgba(11,16,32,0.13)` | `Color(0xFF0B1020).copy(alpha = 0.13f)` |
| `text` | `#0B1020` | `Color(0xFF0B1020)` |
| `textDim` | `#55607A` | `Color(0xFF55607A)` |
| `textMute` | `#8A93A8` | `Color(0xFF8A93A8)` |
| `accent` | `oklch(0.55 0.14 220)` ≈ `#1B7CC9` | `Color(0xFF1B7CC9)` |
| `accentInk` | `#FFFFFF` | `Color.White` |
| `accentSoft` | `accent @ 10%` | `accent.copy(alpha = 0.10f)` |
| `accentGhost` | `accent @ 18%` | `accent.copy(alpha = 0.18f)` |
| `live` | same hue, chroma 0.16 | `Color(0xFF0E77CC)` (approx) |
| `liveGlow` | `live @ 28%` | `live.copy(alpha = 0.28f)` |
| `mapBg` | `#EAE7DE` | `Color(0xFFEAE7DE)` |
| `mapLine` | `rgba(11,16,32,0.05)` | — |
| `mapLineStrong` | `rgba(11,16,32,0.10)` | — |
| `danger` | `#C64454` | `Color(0xFFC64454)` |
| `chipBg` | `rgba(11,16,32,0.04)` | — |

> Compose 1.7 does not ship OKLCH. The hex values above are pre-converted approximations for hue 220 (blue). If you choose a different accent hue, use [oklch.com](https://oklch.com/) to convert.

**Dynamic color** (Material You): keep disabled on Android 12+ for this app. The brand identity depends on the fixed navy/blue palette — do not pipe `dynamicDarkColorScheme(context)` into `MaterialTheme`.

---

## Typography

Two fonts only. Add to `app/src/main/res/font/` or use `androidx.compose.ui:ui-text-google-fonts`.

- **Inter** (400, 500, 600, 700, 800) — all UI.
- **JetBrains Mono** (400, 500, 600, 700) — coordinates, timers, code blocks.

Compose `Typography` overrides (sizes are **sp**; use the scale below, not `MaterialTheme.typography.bodyMedium`'s defaults):

| Role | Size / Weight / Letter spacing / Line height |
|---|---|
| Display (Setup headline) | 34sp / 700 / -1.2 / 1.04 |
| Headline (hero city) | 26sp / 600 / -0.5 / 1.08 |
| Title (sheet title, card titles) | 22sp / 700 / -0.5 |
| Body-emphasis | 16sp / 600 / -0.2 |
| Body | 14sp / 500 / 0 / 1.5 |
| Label | 13sp / 600 / -0.1 |
| Small | 12sp / 500 |
| Overline (section headers) | 11sp / 700 / 1.6 UPPERCASE |
| Micro (tips) | 11sp / 500 |
| Mono readout (coords/timer) | JetBrains Mono 11sp / 500 |

---

## Spacing, radii, elevation

- **Base unit**: 4.dp. Standard increments: 4, 6, 8, 10, 12, 14, 16, 18, 20, 24, 28.
- **Radii**: input 14, button 16, card 28, pill 100, small chip 5–12.
- **Elevation**: prefer **shadow with custom color** (`Modifier.shadow(elevationDp, shape, ambientColor, spotColor)`) over M3 `tonalElevation` for the accent-colored button glows. Map:
  - Primary accent buttons: `shadow(elevation = 14.dp, shape = RoundedCornerShape(16.dp), spotColor = liveGlow)`.
  - Hero stop button: no shadow.
  - Cards, chips: 0.dp elevation + 1.dp border (matches the design).

---

## Screens

Three screens. Details of each follow; earlier revision of this doc (pre-review) contained a search row, two icon buttons, and three quick-action chips — **all removed**. The spec below is the current, final layout.

### 1. Home — Broadcasting (ON state)

**Trigger:** `UiState.status == true`.

**Top → bottom:**

1. **Top bar** (`Row`, horizontal padding 20dp, top 18dp, bottom 8dp)
   - Left: wordmark. 30dp × 30dp accent-filled rounded-square (`RoundedCornerShape(10.dp)`) with the `pin` vector; next to it the text `mock` (Inter 18sp 700 -0.6) in `text`, followed by `location` (18sp 600) in `textDim`.
   - Right: **single 40dp × 40dp `FilledIconButton`**, 12dp radius, `accent` container, `accentInk` icon (`Icons.Outlined.Add`). Accent glow shadow.

2. **Hero card** — `Card`/`Surface`, `RoundedCornerShape(28.dp)`, 1dp border `border`, margin 16dp horizontal + 6dp top.
   - Background: 165° `Brush.linearGradient` from `bgElev2` to `bgElev` (dark) or `#FFFFFF` → `#F8F6F1` (light).
   - Inner padding 18dp.
   - Row 1 (space-between):
     - **Status pill** — `Surface` with `CircleShape`, `accentSoft` fill, 1dp `accentGhost` border, padded `9dp 6dp 11dp 6dp`. Inside: 7dp dot (`live` fill + 3dp glow via `drawBehind`), label `LIVE · GPS + NET` in 12sp/600/0.2.
     - **Timer readout** — JetBrains Mono 11sp/600/1.2, `textDim`, uppercase, format `T+ 00:04:12`.
   - Row 2 (marginTop 14dp, horizontal gap 18dp):
     - `Radar` composable (120dp × 120dp, drawn on `Canvas`). Concentric rings, 90° sweep gradient rotating at 3.4s linear (`rememberInfiniteTransition`). Pulsing center ring at 2.2s.
     - Caption column:
       - Overline: `CURRENTLY SPOOFING`, `textDim`.
       - City name: headline 26sp, parsed as `selected.name.substringBefore(',')`.
       - Country: 14sp 500 `textDim`, parsed as `substringAfter(',').trim()`.
       - Coordinates: mono 11sp `textMute`, formatted `1.2806° N   103.8501° E` (see `fmtCoord` helper).
   - **Stop button** (marginTop 16dp, `fillMaxWidth`, height 56dp, shape 16dp): `Button` with `containerColor = text`, `contentColor = bg` — i.e. inverse of screen. Leading `Icons.Filled.Stop` 18dp, label `Stop broadcasting` 16sp/600/-0.2.

3. **Section header**: `Row` with `PRESET LOCATIONS` overline on the left, `Sort · A–Z` + chevron on the right. Padding `22dp 20dp 10dp`.

4. **Preset list**: `LazyColumn`, horizontal padding 16dp. Each item is a `LocationRow`:
   - 44dp × 44dp country-code tile. Font JetBrains Mono 13sp/700/0.5, content `loc.code` (2-letter). Background `chipBg` or `accentSoft` when active, 1dp border.
   - Column: city name (15sp/600/-0.2); mono coord line (11sp `textDim`, `%.4f, %.4f`). When active, show inline `LIVE` overline badge with pulsing dot.
   - Trailing: 64dp × 44dp compact `MiniMap` thumbnail zoomed to the row's coord.
   - Selected row (matches `uiState.selected`) has a `card` background + 1dp border; others are transparent.

### 2. Home — Idle (OFF state)

Same top bar + same list. The hero card differs:

- Status pill → neutral `Mock location off` with muted dot.
- Timer readout → `Ready` in `textMute`.
- No radar. Instead: full-width `MiniMap` at 150dp tall, `RoundedCornerShape(14.dp)`, pinned at last-selected coord.
- Bottom row: left column with `LAST USED` overline + city name; right a **Start** pill button (accent fill, 52dp height, `Icons.Filled.PlayArrow`, accent glow shadow).

### 3. Custom coordinates — Bottom sheet

**Component:** `ModalBottomSheet` (M3). Scrim on, drag handle on (theme to `textMute @ 50%`, 40dp × 4dp, 12dp bottom margin). Top corners 28dp.

Content (padding 20dp horizontal, 10dp top, 28dp bottom):

- Title row: `Custom location` 22sp/700/-0.5 on the left; 32dp close button (`Icons.Outlined.Close`) on the right.
- Subtitle: `Enter a label and coordinates. Saved to your library.` 13sp `textDim`.
- **Name** `OutlinedTextField`: label `NAME` overline, 54dp height, 14dp shape, fill `chipBg`, border `borderStrong`. Helper `A friendly label for your library.`
- **Latitude** / **Longitude** row (`Row` with `weight(1f)` each, gap 10dp). Same styling, monospace input, `KeyboardType.Number`. Helpers `−90 to 90` / `−180 to 180`.
- **Validation strip** (`Surface`, 12dp shape, `accentSoft` fill, 1dp `accentGhost` border): `Icons.Outlined.Check` + `Valid coordinates · Closest city: <name>` in 12sp `accent` bold. On error, swap to `danger` equivalents and list specific issue (`Latitude out of range`, etc.).
- **CTA**: `Button`, `fillMaxWidth`, 56dp, accent shadow, leading `pin` icon, label `Set mock location`.
- **Tip** (centered, 12sp `textMute`): `Tip: paste 37.7749, −122.4194 into any field`. The tip implies each field accepts paste with a comma-separated pair — split on `,` and populate both.

**Validation** (unchanged from existing `LocationUtils`):
- Latitude in `[-90, 90]`.
- Longitude in `[-180, 180]`.
- Dot decimal separator only.
- Disable CTA until both numeric values parse and fall in range.

### 4. Setup instructions — First run

**Trigger:** `UiState.showInstructions == true` (i.e. the existing `SetupInstruction` composable path).

Layout:

- **Top bar**: 40dp back icon button (left), `Setup · step 1 of 3` label centered, 40dp spacer right.
- **Hero**:
  - `ONE-TIME SETUP` overline pill (`accentSoft` / `accentGhost`, info icon).
  - Display headline 34sp/700/-1.2, balanced wrap (use `Text(..., style = ..., overflow = TextOverflow.Visible)` with `modifier.widthIn(max = ...)` since Compose has no native `text-wrap: pretty`): **"Let Android know / we're the boss of GPS."** (second line in `textDim`).
  - Supporting 14sp `textDim` 1.5 line-height: "Android requires you to manually authorize a mock-location provider. Takes about 20 seconds."
- **Three step cards** (`Column`, `Arrangement.spacedBy(10.dp)`, horizontal padding 16dp):
  - `Surface`, 18dp shape, `card` fill, 1dp border, internal padding 14dp.
  - Leading 32dp × 32dp tile (10dp shape): step 1 uses `accent`/`accentInk`; 2 & 3 use `chipBg`/`textDim`. Text `01`, `02`, `03` in JetBrains Mono 14sp/700.
  - Title 15sp/600/-0.2. Body 13sp `textDim` 1.5 line-height.
  - Step 1 additionally shows a code block: 11sp JetBrains Mono, 1dp dashed border (draw via `drawBehind` — Compose lacks a first-class dashed-border modifier; use `PathEffect.dashPathEffect(floatArrayOf(4f, 4f))`), 8dp shape, faint fill.

  Step copy:
  1. *Open Developer Options* — "On your device, go to Settings → System → Developer options." Code: `adb shell am start -a android.settings.APPLICATION_DEVELOPMENT_SETTINGS`.
  2. *Find "Select mock location app"* — "Scroll to the Debugging section. Tap the row."
  3. *Pick Mock Location* — "Select this app from the list. You'll see it appear in your debug surface."

- **CTA cluster** (padding 16dp horizontal, 18dp top):
  - Primary `Button` 54dp, `Open developer options`, `Icons.Filled.ArrowOutward`. Tapping this should `startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))` — then on return call the existing `SetupInstructionStatusUseCase` re-check path.
  - Secondary `OutlinedButton` 48dp, `I've done this — check again` — calls the same re-check use case directly.

---

## Interactions & motion

Use `androidx.compose.animation` APIs. Respect `AccessibilityManager.getRecommendedTimeoutMillis` and honor "Remove animations" setting where relevant.

| Interaction | Implementation |
|---|---|
| **Stop broadcasting** | Call existing `toggleMocking(false)`. Hero crossfades ON → OFF via `AnimatedContent` (250ms `tween`). |
| **Start (idle)** | `toggleMocking(true)` on `uiState.selected`. |
| **Tap LocationRow** | Existing `selectMockLocation`. Visual: `Modifier.animateItemPlacement()` for reorder, plus press-ripple (M3 default) + `Modifier.scale(0.98f)` spring on press. |
| **Tap +** | `showBottomSheet = true`. M3 `ModalBottomSheet` provides spring-in by default. |
| **Tap ✕ / backdrop / drag-dismiss** | `hide()` on the sheet state. |
| **Set mock location** | Validate → add to library (existing DataStore path) → `sheetState.hide()` → `Snackbar` with `Added "<name>" to your library`. |
| **Radar sweep** | `rememberInfiniteTransition()` rotating 0°→360° over 3400ms `LinearEasing`. Pause when composition leaves foreground (`DisposableEffect` ties to `Lifecycle.Event.ON_PAUSE`). |
| **Live center pulse** | `animateFloat` on radius (3dp → 24dp) + alpha (0.8f → 0f) over 2200ms, infinite. |
| **Active list-row dot** | Same 2200ms pulse as the status pill. |

Sheet spring: Compose default (`SpringSpec(dampingRatio = 0.9f, stiffness = Spring.StiffnessMediumLow)`). Row press scale: `spring(dampingRatio = 0.7f, stiffness = 400f)`.

---

## State

**No new state fields** except one:

- `elapsedLabel: String` on `UiState`, derived from a new DataStore key `mock_started_at: Long`. Store the timestamp when `toggleMocking(true)` fires; clear on stop. In the ViewModel, emit a `Flow<String>` every second (`tickerFlow` + `map { formatElapsed(System.currentTimeMillis() - startedAt) }`) that combines into `UiState`. Format `HH:MM:SS`.

The country code for `LocationRow` is derived client-side in the Compose layer (`loc.name.substringAfter(',').trim()` → lookup in a static `mapOf<String, String>`). No ViewModel change.

---

## System chrome

- **Edge-to-edge** is already enabled. Ensure:
  - Top bar content is padded for `WindowInsets.systemBars.asPaddingValues()`.
  - `Modifier.statusBarsPadding()` on the top-level `Scaffold`.
  - `Modifier.navigationBarsPadding()` on sticky bottom buttons.
- **Status bar icon tint**: set via `WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isSystemInDarkTheme()`.
- **Notification foreground service icon**: keep `ic_location`, but update `NotificationUtils` channel title/description if the brand direction is adopted (`Mock Location · broadcasting`).

---

## Assets

- **Icons**: use `androidx.compose.material.icons.Icons.Outlined` / `Icons.Filled` where a match exists (`Add`, `Close`, `Check`, `PlayArrow`, `Stop`, `Edit`, `KeyboardArrowRight`, `ArrowOutward`, `Info`, `Star`). For icons not in the Material set (map pin with inner dot, compass with needle), author a small `ImageVector` constant in a new `presentation/ui/icons/MockIcons.kt`.
- **Fonts**: Inter + JetBrains Mono. Either bundle in `app/src/main/res/font/` or use `GoogleFont.Provider` — the latter requires no app update when Google ships a new cut.
- No raster asset changes. `ic_launcher` and `ic_location` stay as-is.

---

## File → composable map

| New composable (create/replace under `presentation/mocklocation/composable/`) | Replaces / pairs with |
|---|---|
| `HomeScreen.kt` — root; branches on `uiState.status` | `MockLocationScreen.kt` |
| `BroadcastingHero.kt` — ON-state hero card | part of `MockLocationNStatus.kt` |
| `IdleHero.kt` — OFF-state hero card | part of `MockLocationNStatus.kt` |
| `Radar.kt` — `Canvas`-drawn radar with animated sweep | new |
| `MiniMap.kt` — `Canvas`-drawn world map + pin | new |
| `LocationRow.kt` | `Location.kt` |
| `SectionHeader.kt` | `SectionHeader.kt` (retained, restyled) |
| `StatusPill.kt` | new (extracted) |
| `AddLocationSheet.kt` | `AddMockLocationBottomSheet.kt` |
| `SetupScreen.kt` | `SetupInstruction.kt` |

Under `presentation/theme/`:

- `MockColors.kt` — data class + `lightMockColors()` / `darkMockColors()` builders.
- `LocalMockColors.kt` — `CompositionLocal`.
- `Theme.kt` — extend the existing `MaterialTheme` wrapper to also provide `LocalMockColors` and the custom `Shapes`.

---

## Testing

Follow the existing test conventions (see `CLAUDE.md`):

- **Compose UI tests** with `createComposeRule()` — add tests for each new composable.
- **Preview-driven development**: every composable should have `@Preview` entries for light + dark, using a `MockColorsPreview` helper.
- **Unit tests** for the country-code derivation (`substringAfter(',')` + map lookup) in `UiStateMapperTest`.
- Keep `animationsDisabled = true` in `testOptions` — existing setting.

---

## Files in this bundle

```
design_handoff_mock_location_redesign/
├── README.md                       ← this file (Android/Compose/M3 spec)
├── Mock Location.html              ← visual reference; open in browser
├── components/                     ← HTML prototype source (reference only)
│   ├── theme.jsx
│   ├── icons.jsx
│   ├── map.jsx
│   ├── home.jsx
│   └── screens.jsx
└── frames/
    ├── design-canvas.jsx
    └── android-frame.jsx
```

## Open questions for the implementer

1. **MiniMap fidelity**: port the stylized world map to `Canvas`, or use a flat accent rectangle thumbnail? Recommendation: Canvas version — it's the most distinctive brand moment and not expensive with pre-defined paths.
2. **Timer persistence**: OK to add `mock_started_at` to DataStore? Alternative: derive from service start time in memory only, and show a dash when the service is recreated.
3. **Dynamic color**: confirm we are opting out of Material You — brand identity depends on fixed palette.
4. **Google Fonts downloadable vs bundled**: preference?
