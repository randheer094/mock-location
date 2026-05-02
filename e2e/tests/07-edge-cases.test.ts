import { test, expect } from '../fixtures/device';
import { expect as playwrightExpect } from '@playwright/test';
import { Strings } from '../fixtures/selectors';
import { shell, isForegroundServiceRunning } from '../fixtures/adb';
import { PKG } from '../fixtures/constants';
import { goToHome, startMockingDefaultCity } from '../fixtures/home';

test.describe('edge cases', () => {
  test.beforeEach(async ({ device }) => {
    await goToHome(device);
  });

  test('user can stop mocking after mock-app appops is revoked mid-run', async ({ device }) => {
    const { screen } = device;
    await startMockingDefaultCity(device);
    await expect(screen.getByText(Strings.home.stopBroadcastingCta)).toBeVisible();

    await shell(`appops set ${PKG} android:mock_location default`);

    await screen.getByText(Strings.home.stopBroadcastingCta).tap();
    await playwrightExpect
      .poll(async () => isForegroundServiceRunning(device), { timeout: 5_000 })
      .toBe(false);
  });
});
