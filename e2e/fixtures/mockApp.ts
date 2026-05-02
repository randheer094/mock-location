import type { Device } from 'mobilewright';
import { APP_LABEL } from './constants';
import {
  appopsAllowMockLocation,
  appopsResetMockLocation,
  shell,
} from './adb';

export async function selectMockApp(device: Device): Promise<void> {
  const granted = await appopsAllowMockLocation(device);
  if (granted) return;

  // UI fallback: drive Settings → Developer options → "Select mock location app".
  await shell('am start -a android.settings.APPLICATION_DEVELOPMENT_SETTINGS');
  const { screen } = device;
  await screen.getByText('Select mock location app').tap();
  await screen.getByText(APP_LABEL).tap();
  await shell('input keyevent KEYCODE_HOME');
}

export async function resetMockApp(device: Device): Promise<void> {
  await appopsResetMockLocation(device);
}
