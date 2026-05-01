import { defineConfig } from '@playwright/test';

export default defineConfig({
  testDir: './tests',
  workers: 1,
  retries: 1,
  timeout: 60_000,
  fullyParallel: false,
  reporter: [['list'], ['html', { open: 'never' }]],
  use: {
    trace: 'on-first-retry',
  },
  globalSetup: './globalSetup.ts',
  globalTeardown: './globalTeardown.ts',
});
