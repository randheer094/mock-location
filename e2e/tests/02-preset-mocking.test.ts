import type { Device } from 'mobilewright';
import { test, expect } from '../fixtures/device';
import { expect as playwrightExpect } from '@playwright/test';
import { Strings } from '../fixtures/selectors';
import { DEFAULT_CITY } from '../fixtures/constants';
import {
  isForegroundServiceRunning,
  lastMockedLocation,
} from '../fixtures/adb';

// The LocationRow composable renders only the city portion of the name
// (text = location.name.substringBefore(',')) so we match on just the city.
const DEFAULT_CITY_LABEL = DEFAULT_CITY.name.split(',')[0].trim();

/**
 * Select the default city and start mocking from any initial state.
 *
 * The hero card (with Start / Stop broadcasting) is at the top of the
 * LazyColumn. After scrolling to find Stockholm we scroll back up to
 * ensure the hero card is re-composed, then tap accordingly.
 */
async function startMockingDefaultCity(device: Device): Promise<void> {
  const { screen } = device;

  // Scroll to the top in case the hero card has been virtualised away.
  await screen.swipe('down');
  await screen.swipe('down');

  // If mocking is already active (leftover from a previous test), stop it first.
  const stopCta = screen.getByText(Strings.home.stopBroadcastingCta);
  if (await stopCta.isVisible({ timeout: 1_000 }).catch(() => false)) {
    await stopCta.tap();
    // Wait for the UI to return to idle state.
    await screen.getByText(Strings.home.startCta).isVisible({ timeout: 5_000 });
  }

  // Scroll to Stockholm (off-screen in a sorted LazyColumn) and select it.
  const cityLocator = screen.getByText(DEFAULT_CITY_LABEL);
  await cityLocator.scrollIntoViewIfNeeded();
  await cityLocator.tap();

  // Scroll back to top and tap Start.
  await screen.swipe('down');
  await screen.swipe('down');
  await screen.getByText(Strings.home.startCta).tap();
}

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
