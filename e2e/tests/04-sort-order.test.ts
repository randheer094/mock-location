import { test, expect } from '../fixtures/device';
import { Strings } from '../fixtures/selectors';
import { goToHome } from '../fixtures/home';

test.describe('sort order', () => {
  test.beforeEach(async ({ device }) => {
    await goToHome(device);
  });

  test('default order shows A–Z sort label', async ({ device }) => {
    const { screen } = device;
    await expect(screen.getByText(Strings.home.sortToggleAZ)).toBeVisible();
  });

  test('toggling sort flips the label to Z–A', async ({ device }) => {
    const { screen } = device;
    await expect(screen.getByText(Strings.home.sortToggleAZ)).toBeVisible();
    await screen.getByText(Strings.home.sortToggleAZ).tap();
    await expect(screen.getByText(Strings.home.sortToggleZA)).toBeVisible();
  });
});
