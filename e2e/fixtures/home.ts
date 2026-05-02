import type { Device } from 'mobilewright';
import { expect } from '@mobilewright/test';
import { Strings } from './selectors';
import { DEFAULT_CITY, PKG } from './constants';
import { shell } from './adb';

const DEFAULT_CITY_LABEL = DEFAULT_CITY.name.split(',')[0].trim();
const MAIN_ACTIVITY = `${PKG}/dev.randheer094.dev.location.presentation.main.MainActivity`;

/**
 * Verify our MainActivity is the foreground activity; if not (e.g. LeakCanary's
 * LeakLauncherActivity won the launcher resolution after a leaky test, or a
 * heap-dump-induced ANR dropped us back to the home launcher) force-start it
 * and wait for the activity transition to settle.
 */
export async function ensureForegroundIsOurApp(device: Device): Promise<void> {
  for (let i = 0; i < 3; i++) {
    const top = await shell(
      `dumpsys activity activities | grep topResumedActivity`,
    );
    if (top.stdout.includes(`${PKG}/dev.randheer094.dev.location.presentation.main.MainActivity`)) {
      return;
    }
    await shell(`am start -n ${MAIN_ACTIVITY}`);
    await new Promise(resolve => setTimeout(resolve, 1000));
  }
}

/**
 * Dismiss the one-time setup wizard if it is on screen, then wait for a
 * stable home-screen marker before returning. No-op when the wizard is
 * already dismissed.
 */
export async function goToHome(device: Device): Promise<void> {
  const { screen } = device;
  // Up to 3 attempts to dismiss the wizard. The CTA tap occasionally lands
  // mid-recomposition on a fresh launch and the screen does not transition;
  // a re-tap after a short delay reliably moves things along. Each attempt
  // also re-verifies our app is foreground (LeakCanary's analyzer can run a
  // heap dump that suspends us long enough for the system to swap activities).
  for (let i = 0; i < 3; i++) {
    await ensureForegroundIsOurApp(device);
    await new Promise(resolve => setTimeout(resolve, 1500));
    const cta = screen.getByText(Strings.setupScreen.checkAgainCta);
    if (await cta.isVisible({ timeout: 5_000 }).catch(() => false)) {
      await cta.tap();
    }
    const home = screen.getByText(Strings.home.statusMockOff);
    if (await home.isVisible({ timeout: 5_000 }).catch(() => false)) {
      return;
    }
  }
  await expect(screen.getByText(Strings.home.statusMockOff)).toBeVisible({ timeout: 5_000 });
}

export async function scrollToTop(device: Device): Promise<void> {
  const { screen } = device;
  await screen.swipe('down');
  await screen.swipe('down');
}

export async function stopIfMocking(device: Device): Promise<void> {
  const { screen } = device;
  const stopCta = screen.getByText(Strings.home.stopBroadcastingCta);
  if (await stopCta.isVisible({ timeout: 1_000 }).catch(() => false)) {
    await stopCta.tap();
    await screen.getByText(Strings.home.startCta).isVisible({ timeout: 5_000 });
  }
}

export async function startMockingDefaultCity(device: Device): Promise<void> {
  const { screen } = device;
  await ensureForegroundIsOurApp(device);
  await scrollToTop(device);
  await stopIfMocking(device);

  const cityLocator = screen.getByText(DEFAULT_CITY_LABEL);
  await cityLocator.scrollIntoViewIfNeeded();
  await cityLocator.tap();

  await scrollToTop(device);
  await screen.getByText(Strings.home.startCta).tap();
}
