import { execSync } from 'node:child_process';
import { APK_PATH } from './fixtures/constants';

export default async function globalSetup(): Promise<void> {
  // Install (or reinstall) the debug APK with all runtime perms granted at install.
  // Per-test cleanup (pm clear + permission re-grant) lives in the device fixture
  // so every test starts from a known clean state.
  execSync(`adb install -r -t -g ${APK_PATH}`, { stdio: 'inherit' });
}
