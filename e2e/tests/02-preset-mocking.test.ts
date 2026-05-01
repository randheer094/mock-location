import { test, expect } from '../fixtures/device';
import { expect as playwrightExpect } from '@playwright/test';
import { Strings } from '../fixtures/selectors';
import { DEFAULT_CITY } from '../fixtures/constants';
import {
  isForegroundServiceRunning,
  lastMockedLocation,
} from '../fixtures/adb';
import { startMockingDefaultCity } from '../fixtures/home';

test.describe('preset mocking', () => {
  test('tap preset transitions home to mocking-active state', async ({ device }) => {
    const { screen } = device;
    await startMockingDefaultCity(device);
    await expect(screen.getByText(Strings.home.stopBroadcastingCta)).toBeVisible();
  });

  test('mocked location is delivered to LocationManager', async ({ device }) => {
    const { screen } = device;
    await startMockingDefaultCity(device);
    await expect(screen.getByText(Strings.home.stopBroadcastingCta)).toBeVisible();

    await playwrightExpect
      .poll(async () => isForegroundServiceRunning(device), { timeout: 5_000 })
      .toBe(true);

    await playwrightExpect
      .poll(async () => lastMockedLocation(device), { timeout: 5_000 })
      .toMatchObject({
        lat: playwrightExpect.closeTo(DEFAULT_CITY.lat, 2),
        lng: playwrightExpect.closeTo(DEFAULT_CITY.lng, 2),
      });
  });

  test('stop returns to idle and tears down the service', async ({ device }) => {
    const { screen } = device;
    await startMockingDefaultCity(device);
    await screen.getByText(Strings.home.stopBroadcastingCta).tap();

    await playwrightExpect
      .poll(async () => isForegroundServiceRunning(device), { timeout: 5_000 })
      .toBe(false);
  });
});
