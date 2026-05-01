import { execFile } from 'node:child_process';
import { promisify } from 'node:util';
import type { Device } from 'mobilewright';
import { PKG, CHANNEL_ID } from './constants';

const execFileAsync = promisify(execFile);

export interface MockedLocation {
  lat: number;
  lng: number;
}

interface ShellResult {
  stdout: string;
  exitCode: number;
}

const LAST_LOCATION_RE =
  /last location=Location\[\w+ (-?\d+\.\d+),(-?\d+\.\d+)/;

export function parseLastLocation(dumpsysOutput: string): MockedLocation | null {
  const match = LAST_LOCATION_RE.exec(dumpsysOutput);
  if (!match) return null;
  return { lat: parseFloat(match[1]), lng: parseFloat(match[2]) };
}

export async function shell(cmd: string): Promise<ShellResult> {
  try {
    const { stdout } = await execFileAsync('adb', ['shell', cmd]);
    return { stdout, exitCode: 0 };
  } catch (err: unknown) {
    const e = err as { stdout?: string; code?: number };
    return { stdout: e.stdout ?? '', exitCode: e.code ?? 1 };
  }
}

// The Device parameter is kept for interface compatibility and future use
// (e.g. multi-device support via device.driver session id).
// All helpers currently target the default ADB device.

export async function isForegroundServiceRunning(
  _device: Device,
  pkg: string = PKG,
): Promise<boolean> {
  const out = await shell(`dumpsys activity services ${pkg}`);
  return out.stdout.includes('isForeground=true');
}

export async function lastMockedLocation(
  _device: Device,
): Promise<MockedLocation | null> {
  const out = await shell('dumpsys location');
  return parseLastLocation(out.stdout);
}

export async function notificationVisible(
  _device: Device,
  channelId: string = CHANNEL_ID,
): Promise<boolean> {
  const out = await shell('dumpsys notification --noredact');
  return out.stdout.includes(channelId);
}

export async function killApp(_device: Device, pkg: string = PKG): Promise<void> {
  await shell(`am force-stop ${pkg}`);
}

export async function swipeFromRecents(
  _device: Device,
  pkg: string = PKG,
): Promise<void> {
  await shell(`am kill ${pkg}`);
}

export async function broadcastStopAction(_device: Device): Promise<void> {
  // ACTION_STOP from MockLocationService.kt:40
  await shell(
    `am start-service -a dev.randheer094.dev.location.action.STOP -n ${PKG}/dev.randheer094.dev.location.presentation.service.MockLocationService`,
  );
}

export async function appopsAllowMockLocation(_device: Device): Promise<boolean> {
  const out = await shell(`appops set ${PKG} android:mock_location allow`);
  return out.exitCode === 0;
}

export async function appopsResetMockLocation(_device: Device): Promise<void> {
  await shell(`appops set ${PKG} android:mock_location default`).catch(() => {});
}

export async function grantPostNotifications(_device: Device): Promise<void> {
  await shell(`pm grant ${PKG} android.permission.POST_NOTIFICATIONS`).catch(() => {});
}

export async function revokePostNotifications(_device: Device): Promise<void> {
  await shell(`pm revoke ${PKG} android.permission.POST_NOTIFICATIONS`).catch(() => {});
}
