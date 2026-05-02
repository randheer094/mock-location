import { test, expect } from '../fixtures/device';
import { expect as playwrightExpect } from '@playwright/test';
import { Strings } from '../fixtures/selectors';
import { PKG } from '../fixtures/constants';
import {
  isForegroundServiceRunning,
  notificationVisible,
  shell,
} from '../fixtures/adb';
import { goToHome, startMockingDefaultCity } from '../fixtures/home';

test.describe('foreground service', () => {
  test.beforeEach(async ({ device }) => {
    await goToHome(device);
  });

  test('ongoing notification is posted while mocking', async ({ device }) => {
    const { screen } = device;
    await startMockingDefaultCity(device);
    await expect(screen.getByText(Strings.home.stopBroadcastingCta)).toBeVisible();

    await playwrightExpect
      .poll(async () => notificationVisible(device), { timeout: 5_000 })
      .toBe(true);
  });

  test('tapping notification returns to MainActivity', async ({ device }) => {
    const { screen } = device;
    await startMockingDefaultCity(device);
    await expect(screen.getByText(Strings.home.stopBroadcastingCta)).toBeVisible();

    await shell('cmd statusbar expand-notifications');
    await screen.getByText(Strings.notification.title).tap();

    const top = await shell('dumpsys activity activities | grep topResumedActivity');
    expect(top.stdout).toContain(PKG);
    expect(top.stdout).toContain('MainActivity');
  });

  test('service survives task removal (START_STICKY + stopWithTask=false)', async ({ device }) => {
    const { screen } = device;
    await startMockingDefaultCity(device);
    await expect(screen.getByText(Strings.home.stopBroadcastingCta)).toBeVisible();

    await shell(`am kill ${PKG}`);
    await device.launchApp(PKG);

    await playwrightExpect
      .poll(async () => isForegroundServiceRunning(device), { timeout: 5_000 })
      .toBe(true);
  });
});
