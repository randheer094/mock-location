// Theme tokens for Mock Location redesign.
// Two modes: light ('warm off-white') and dark ('deep navy').
// Single accent: a configurable hue driving primary + live ring.

const buildTheme = ({ dark, accentHue = 188 }) => {
  if (dark) {
    return {
      dark: true,
      bg: '#0A0E1A',
      bgElev: '#121829',
      bgElev2: '#1A2236',
      card: '#141B2C',
      border: 'rgba(255,255,255,0.06)',
      borderStrong: 'rgba(255,255,255,0.12)',
      text: '#EDF1F7',
      textDim: '#9AA5BD',
      textMute: '#5E6A83',
      accent: `oklch(0.78 0.14 ${accentHue})`,
      accentInk: '#04131A',
      accentSoft: `oklch(0.78 0.14 ${accentHue} / 0.14)`,
      accentGhost: `oklch(0.78 0.14 ${accentHue} / 0.24)`,
      live: `oklch(0.78 0.16 ${accentHue})`,
      liveGlow: `oklch(0.78 0.16 ${accentHue} / 0.35)`,
      mapBg: '#0B1322',
      mapLine: 'rgba(140,180,220,0.08)',
      mapLineStrong: 'rgba(140,180,220,0.14)',
      mapWater: '#0F1A2D',
      danger: '#FF7A85',
      chipBg: 'rgba(255,255,255,0.04)',
    };
  }
  return {
    dark: false,
    bg: '#F3F1EC',
    bgElev: '#FFFFFF',
    bgElev2: '#F8F6F1',
    card: '#FFFFFF',
    border: 'rgba(11,16,32,0.07)',
    borderStrong: 'rgba(11,16,32,0.13)',
    text: '#0B1020',
    textDim: '#55607A',
    textMute: '#8A93A8',
    accent: `oklch(0.55 0.14 ${accentHue})`,
    accentInk: '#FFFFFF',
    accentSoft: `oklch(0.55 0.14 ${accentHue} / 0.10)`,
    accentGhost: `oklch(0.55 0.14 ${accentHue} / 0.18)`,
    live: `oklch(0.55 0.16 ${accentHue})`,
    liveGlow: `oklch(0.55 0.16 ${accentHue} / 0.28)`,
    mapBg: '#EAE7DE',
    mapLine: 'rgba(11,16,32,0.05)',
    mapLineStrong: 'rgba(11,16,32,0.10)',
    mapWater: '#DEDBD0',
    danger: '#C64454',
    chipBg: 'rgba(11,16,32,0.04)',
  };
};

// Utility: compact coordinate formatting — 4 decimals with degree suffix.
const fmtCoord = (v, axis /* 'lat' | 'lng' */) => {
  const abs = Math.abs(v).toFixed(4);
  const suffix = axis === 'lat' ? (v >= 0 ? 'N' : 'S') : (v >= 0 ? 'E' : 'W');
  return `${abs}° ${suffix}`;
};

// Two-letter country code from a "City, Country" string — hand-mapped.
const COUNTRY_CODE = {
  Singapore: 'SG', Sweden: 'SE', 'Türkiye': 'TR', 'Hong Kong': 'HK',
  Malaysia: 'MY', Norway: 'NO', Bangladesh: 'BD', Pakistan: 'PK',
  Philippines: 'PH', Taiwan: 'TW', Cambodia: 'KH', Laos: 'LA',
  Myanmar: 'MM', Czechia: 'CZ', Hungary: 'HU', Austria: 'AT',
};

const LOCATIONS = [
  { name: 'Lau Pa Sat, Singapore', lat: 1.2806319, long: 103.8501362 },
  { name: 'Stockholm, Sweden', lat: 59.3383223, long: 18.0549621 },
  { name: 'İstanbul, Türkiye', lat: 41.0763745, long: 29.0114464 },
  { name: 'Sai Wan, Hong Kong', lat: 22.2848692, long: 114.1410492 },
  { name: 'Kuala Lumpur, Malaysia', lat: 3.1115726, long: 101.6633107 },
  { name: 'Oslo, Norway', lat: 59.9271583, long: 10.7246093 },
  { name: 'Dhaka, Bangladesh', lat: 23.8516649, long: 90.2535154 },
  { name: 'Karachi, Pakistan', lat: 24.8653886, long: 67.0535651 },
  { name: 'Manila, Philippines', lat: 14.5574546, long: 120.9863673 },
  { name: 'Taipei City, Taiwan', lat: 25.0415595, long: 121.5630013 },
  { name: 'GIA Tower, Cambodia', lat: 11.5526867, long: 104.9374995 },
  { name: 'Vientiane, Laos', lat: 17.9772614, long: 102.6132033 },
  { name: 'Yangon, Myanmar', lat: 16.8422397, long: 96.1543628 },
  { name: 'Prague, Czechia', lat: 50.1039621, long: 14.528223 },
  { name: 'Budapest, Hungary', lat: 47.4752135, long: 19.0691096 },
  { name: 'Wien, Austria', lat: 48.1972505, long: 16.3857097 },
];

const withCountry = (loc) => {
  const country = loc.name.split(',').pop().trim();
  return { ...loc, country, code: COUNTRY_CODE[country] || '—' };
};

Object.assign(window, { buildTheme, fmtCoord, LOCATIONS: LOCATIONS.map(withCountry) });
