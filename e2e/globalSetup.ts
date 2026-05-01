import { execSync } from 'node:child_process';
import { android } from 'mobilewright';
import { PKG, APK_PATH } from './fixtures/constants';
import { selectMockApp } from './fixtures/mockApp';
import { grantPostNotifications } from './fixtures/adb';

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
  } finally {
    await device.close();
  }
}
