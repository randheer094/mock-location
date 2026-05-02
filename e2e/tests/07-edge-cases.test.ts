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

    // Revoke the appops while the service is still running. The stop CTA
    // should still tear the service down — the app does not get stuck.
    await shell(`appops set ${PKG} android:mock_location default`);

    await screen.getByText(Strings.home.stopBroadcastingCta).tap();
    await playwrightExpect
      .poll(async () => isForegroundServiceRunning(device), { timeout: 5_000 })
      .toBe(false);
  });

  // Skipped: the ACTION_STOP intent path is exercised in 05-foreground-service
  // through the regular Stop CTA which dispatches the same intent. Driving
  // the service directly via `am start-service ... -a ACTION_STOP` from this
  // suite races the e2e runner's own connection — covered by an
  // instrumentation test in app/src/androidTest instead.
  test.skip('ACTION_STOP broadcast stops the service within 5 s', async () => {
    /* documentation only */
  });
});
