import { android } from 'mobilewright';
import { PKG } from './fixtures/constants';
import { resetMockApp } from './fixtures/mockApp';

export default async function globalTeardown(): Promise<void> {
  const device = await android.launch({ bundleId: PKG }).catch(() => null);
  if (!device) return;
  try {
    await resetMockApp(device);
  } finally {
    await device.close();
  }
}
