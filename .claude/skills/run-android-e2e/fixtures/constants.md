# constants

Single source of truth for package, app label, notification channel, default
city. Mirrors `e2e/fixtures/constants.ts`.

| Name | Value |
| --- | --- |
| `PKG` | `dev.randheer094.dev.location.debug` |
| `APP_LABEL` | `Mock Location for Developers (Debug)` |
| `CHANNEL_ID` | `mock_location_channel` |
| `MAIN_ACTIVITY` | `dev.randheer094.dev.location.presentation.main.MainActivity` |
| `MAIN_ACTIVITY_FQN` | `${PKG}/${MAIN_ACTIVITY}` (i.e. `dev.randheer094.dev.location.debug/dev.randheer094.dev.location.presentation.main.MainActivity`) |
| `MOCK_LOCATION_SERVICE_FQN` | `${PKG}/dev.randheer094.dev.location.presentation.service.MockLocationService` |
| `STOP_ACTION` | `dev.randheer094.dev.location.action.STOP` |
| `APK_PATH` | `app/build/outputs/apk/debug/app-debug.apk` (relative to repo root) |

## Default city (Stockholm)

```
name = "Stockholm, Sweden"
lat  = 59.3383223
lng  = 18.0549621
label = "Stockholm"   // text shown in the preset list (split on comma, trimmed)
```

## San Francisco (used by 03-custom-coordinates)

```
lat = 37.7749
lng = -122.4194
```
