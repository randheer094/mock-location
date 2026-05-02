import { test, expect } from '../fixtures/device';
import { expect as playwrightExpect } from '@playwright/test';
import { Strings } from '../fixtures/selectors';
import { lastMockedLocation } from '../fixtures/adb';
import { goToHome, stopIfMocking, scrollToTop } from '../fixtures/home';

const SF = { lat: 37.7749, lng: -122.4194 };

async function dismissSheetIfOpen(device: import('mobilewright').Device): Promise<void> {
  const { screen } = device;
  const closeBtn = screen.getByLabel(Strings.customSheet.closeCd);
  if (await closeBtn.isVisible({ timeout: 1_000 }).catch(() => false)) {
    await closeBtn.tap();
    await expect(screen.getByText(Strings.customSheet.title)).not.toBeVisible({ timeout: 3_000 });
  }
}

async function openCustomSheet(device: import('mobilewright').Device): Promise<void> {
  const { screen } = device;
  await dismissSheetIfOpen(device);
  await scrollToTop(device);
  await stopIfMocking(device);
  const fab = screen.getByLabel(Strings.home.addLocationFab);
  await expect(fab).toBeVisible({ timeout: 5_000 });
  await fab.tap();
  await expect(screen.getByText(Strings.customSheet.title)).toBeVisible();
}

test.describe('custom coordinates', () => {
  test.beforeEach(async ({ device }) => {
    await goToHome(device);
  });

  test('opens the bottom sheet', async ({ device }) => {
    await openCustomSheet(device);
  });

  test('valid input starts mocking at the entered coordinates', async ({ device }) => {
    const { screen } = device;
    await openCustomSheet(device);
    await screen.getByLabel(Strings.customSheet.latitudeInputCd).fill(String(SF.lat));
    await screen.getByLabel(Strings.customSheet.longitudeInputCd).fill(String(SF.lng));
    await screen.getByText(Strings.customSheet.saveCta).tap();

    // Wait for the sheet to dismiss, then tap Start to begin mocking
    await expect(screen.getByText(Strings.customSheet.title)).not.toBeVisible({ timeout: 5_000 });
    await screen.getByText(Strings.home.startCta).tap();
    await expect(screen.getByText(Strings.home.stopBroadcastingCta)).toBeVisible({ timeout: 10_000 });
    await playwrightExpect
      .poll(async () => lastMockedLocation(device), { timeout: 5_000 })
      .toMatchObject({
        lat: playwrightExpect.closeTo(SF.lat, 2),
        lng: playwrightExpect.closeTo(SF.lng, 2),
      });
  });

  test('out-of-range latitude does not start mocking', async ({ device }) => {
    const { screen } = device;
    await openCustomSheet(device);
    await screen.getByLabel(Strings.customSheet.latitudeInputCd).fill('200');
    await screen.getByLabel(Strings.customSheet.longitudeInputCd).fill('0');
    await screen.getByText(Strings.customSheet.saveCta).tap();
    await expect(screen.getByText(Strings.home.stopBroadcastingCta)).not.toBeVisible({ timeout: 2_000 });
  });

  test('close button dismisses without starting mocking (ARC-155)', async ({ device }) => {
    const { screen } = device;
    await openCustomSheet(device);
    await screen.getByLabel(Strings.customSheet.closeCd).tap();
    await expect(screen.getByText(Strings.customSheet.title)).not.toBeVisible({ timeout: 2_000 });
    await expect(screen.getByText(Strings.home.stopBroadcastingCta)).not.toBeVisible({ timeout: 1_000 });
  });
});
