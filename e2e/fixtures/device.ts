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
    await shell(`pm clear ${PKG}`);
    await shell(`pm grant ${PKG} android.permission.POST_NOTIFICATIONS`);
    await shell(`appops set ${PKG} android:mock_location allow`);
    await shell(`am force-stop ${PKG}`);

    const device = await android.launch({ bundleId: PKG });
    await use(device);
    await device.close();
  },
});

export { expect } from '@mobilewright/test';
