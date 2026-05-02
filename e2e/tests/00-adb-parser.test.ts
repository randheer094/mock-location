import { test, expect } from '@playwright/test';
import { parseLastLocation } from '../fixtures/adb';

test('parseLastLocation extracts lat/lng from a typical dumpsys block', () => {
  const sample = `
    gps provider:
      last location=Location[gps 59.338300,18.054970 hAcc=5.0 et=...]
  `;
  expect(parseLastLocation(sample)).toEqual({ lat: 59.3383, lng: 18.05497 });
});

test('parseLastLocation returns null when no last location', () => {
  expect(parseLastLocation('gps provider: enabled')).toBeNull();
});
