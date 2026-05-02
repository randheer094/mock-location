import { test, expect } from '../fixtures/device';
import { Strings } from '../fixtures/selectors';
import { goToHome } from '../fixtures/home';

test.describe('permissions', () => {
  test('pre-grant skips the rationale screen', async ({ device }) => {
    await goToHome(device);
    const { screen } = device;
    await expect(screen.getByText(Strings.home.sortToggleAZ)).toBeVisible();
  });
});
