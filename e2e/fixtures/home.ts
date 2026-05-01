import type { Device } from 'mobilewright';
import { Strings } from './selectors';
import { DEFAULT_CITY } from './constants';

const DEFAULT_CITY_LABEL = DEFAULT_CITY.name.split(',')[0].trim();

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
