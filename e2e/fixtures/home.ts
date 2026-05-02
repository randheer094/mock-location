import type { Device } from 'mobilewright';
import { expect } from '@mobilewright/test';
import { Strings } from './selectors';
import { DEFAULT_CITY } from './constants';

const DEFAULT_CITY_LABEL = DEFAULT_CITY.name.split(',')[0].trim();

/**
 * Dismiss the one-time setup wizard if it is on screen, then wait for a
 * stable home-screen marker before returning. No-op when the wizard is
 * already dismissed.
 */
export async function goToHome(device: Device): Promise<void> {
  const { screen } = device;
  const cta = screen.getByText(Strings.setupScreen.checkAgainCta);
  if (await cta.isVisible({ timeout: 5_000 }).catch(() => false)) {
    await cta.tap();
  }
  await expect(screen.getByText(Strings.home.presetSection)).toBeVisible({ timeout: 10_000 });
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
  await scrollToTop(device);
  await stopIfMocking(device);

  const cityLocator = screen.getByText(DEFAULT_CITY_LABEL);
  await cityLocator.scrollIntoViewIfNeeded();
  await cityLocator.tap();

  await scrollToTop(device);
  await screen.getByText(Strings.home.startCta).tap();
}
