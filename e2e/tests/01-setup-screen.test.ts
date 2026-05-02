import { test, expect } from '../fixtures/device';
import { Strings } from '../fixtures/selectors';
import { resetMockApp } from '../fixtures/mockApp';
import { shell } from '../fixtures/adb';
import { PKG } from '../fixtures/constants';

test.describe('setup screen', () => {
  test.beforeEach(async ({ device }) => {
    // The fixture has already pm-cleared the app, granted POST_NOTIFICATIONS,
    // and granted mock-app via appops. Revert the appops grant so the wizard
    // is visible, then bounce the activity to pick up the change.
    await resetMockApp(device);
    await device.terminateApp(PKG);
    // Explicit am start MainActivity (not device.launchApp) so LeakCanary's
    // CATEGORY_LAUNCHER activity doesn't win launcher resolution.
    await shell(
      `am start -n ${PKG}/dev.randheer094.dev.location.presentation.main.MainActivity`,
    );
    await new Promise(r => setTimeout(r, 500));
  });

  test('shows setup wizard when mock-app is not selected', async ({ device }) => {
    const { screen } = device;
    await expect(screen.getByText(Strings.setupScreen.headlineLine1)).toBeVisible();
    await expect(screen.getByText(Strings.setupScreen.step1Title)).toBeVisible();
    await expect(screen.getByText(Strings.setupScreen.step2Title)).toBeVisible();
    await expect(screen.getByText(Strings.setupScreen.step3Title)).toBeVisible();
  });

  test('Open Settings CTA dispatches developer options intent', async ({ device }) => {
    const { screen } = device;
    // The card title for step 1 ("Open Developer Options") matches by case-
    // insensitive prefix against the CTA label "Open developer options",
    // which makes getByText flaky. Target the button by its content
    // description (added via Modifier.semantics) instead.
    await expect(screen.getByLabel(Strings.setupScreen.openDevOptionsCta)).toBeVisible({
      timeout: 15_000,
    });
    await screen.getByLabel(Strings.setupScreen.openDevOptionsCta).tap();

    const top = await shell('dumpsys activity activities | grep topResumedActivity');
    expect(top.stdout).toContain('com.android.settings');
  });
});
