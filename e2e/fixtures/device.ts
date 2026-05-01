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
    await shell(`am force-stop ${PKG}`);
    const device = await android.launch({ bundleId: PKG });
    await use(device);
    await device.close();
  },
});

export { expect } from '@mobilewright/test';
