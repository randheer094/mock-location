import { execSync } from 'node:child_process';
import { android } from 'mobilewright';
import { PKG, APK_PATH } from './fixtures/constants';
import { selectMockApp } from './fixtures/mockApp';
import { grantPostNotifications } from './fixtures/adb';
import { Strings } from './fixtures/selectors';

export default async function globalSetup(): Promise<void> {
  // 1. Install (or reinstall) the debug APK with all runtime perms granted at install.
  execSync(`adb install -r -t -g ${APK_PATH}`, { stdio: 'inherit' });

  // 2. Grant POST_NOTIFICATIONS so the rationale screen does not appear in
  //    golden-path tests. The dedicated permissions test revokes/re-grants
  //    in beforeEach/afterEach.
  const device = await android.launch({ bundleId: PKG });
  try {
    await grantPostNotifications(device);
    await selectMockApp(device);

    // 3. Dismiss the setup wizard so the home screen is visible on first launch.
    //    The DataStore key defaults to true (show wizard); tapping "I've done this"
    //    persists false so subsequent test launches skip the wizard.
    const { screen } = device;
    const checkAgain = screen.getByText(Strings.setupScreen.checkAgainCta);
    const isVisible = await checkAgain.isVisible({ timeout: 5_000 }).catch(() => false);
    if (isVisible) {
      await checkAgain.tap();
    }
  } finally {
    await device.close();
  }
}
