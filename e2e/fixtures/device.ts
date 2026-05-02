import { test as base } from '@mobilewright/test';
import { android } from 'mobilewright';
import type { Device } from 'mobilewright';
import { PKG } from './constants';
import { shell } from './adb';

type Fixtures = {
  device: Device;
};

export const test = base.extend<Fixtures>({
  device: async ({}, use) => {
    // Reset to a known clean state before every test:
    // pm clear wipes DataStore + runtime perms; granting BEFORE launch ensures
    // the app's startup checks see mock-app authorised (no transient wizard).
    await shell(`cmd statusbar collapse`);
    await shell(`pm clear ${PKG}`);
    await shell(`pm grant ${PKG} android.permission.POST_NOTIFICATIONS`);
    await shell(`appops set ${PKG} android:mock_location allow`);
    await shell(`am force-stop ${PKG}`);

    const device = await android.launch({ bundleId: PKG });

    // mobilewright's android.launch() resolves the launcher activity by
    // category, and LeakCanary registers a CATEGORY_LAUNCHER activity in
    // our package; after a leaky earlier test it wins the resolution.
    // Force MainActivity to the front after the driver session is up, so
    // the test always lands on our actual UI.
    await shell(
      `am start -n ${PKG}/dev.randheer094.dev.location.presentation.main.MainActivity`,
    );
    // Brief settle for the activity transition.
    await new Promise(r => setTimeout(r, 500));
    await use(device);
    await device.close();
  },
});

export { expect } from '@mobilewright/test';
