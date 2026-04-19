// MiniMap — stylized world/region map drawn with SVG.
// Not geographically accurate — it's a typographic-grade decoration
// that gives the app a "map" vibe. A dot marks the current coord.
//
// Projection: plate carrée (lon/lat linearly mapped) onto a 360x180 box.
// We draw:
//   - a grid of meridians + parallels
//   - a set of hand-authored continent blobs (simple polygons)
//   - a soft crosshair + ring at the pinned coordinate
//   - an optional pulsing live ring

const CONTINENT_BLOBS = [
  // Each blob is a closed path on 360x180 (lon [-180..180] + 180, lat [90..-90] inverted to 0..180)
  // Rough, stylized silhouettes — evocative, not accurate.
  // Eurasia
  'M 170 40 L 210 30 L 260 32 L 300 40 L 325 55 L 340 70 L 330 90 L 295 95 L 270 110 L 240 105 L 215 95 L 200 80 L 180 70 Z',
  // Africa
  'M 185 95 L 215 95 L 230 120 L 220 150 L 200 160 L 190 145 L 180 120 Z',
  // North America
  'M 55 45 L 95 40 L 130 50 L 140 70 L 115 90 L 95 100 L 80 90 L 65 75 Z',
  // South America
  'M 110 110 L 130 110 L 140 130 L 130 160 L 115 170 L 108 150 L 105 125 Z',
  // Australia
  'M 295 135 L 325 130 L 340 145 L 325 160 L 300 155 Z',
  // UK / British Isles
  'M 170 55 L 178 53 L 180 63 L 172 65 Z',
  // Japan
  'M 325 65 L 332 62 L 333 72 L 328 75 Z',
  // SE Asia islands
  'M 295 105 L 310 100 L 315 115 L 300 118 Z',
];

function MiniMap({
  lat, long, theme,
  width = 340, height = 170,
  live = false,
  compact = false,
}) {
  // Project lon/lat into our 360x180 space.
  const px = ((long + 180) / 360) * 360;
  const py = ((90 - lat) / 180) * 180;

  // For compact mode we zoom into a 120x60 region around the point.
  let viewBox, pinR;
  if (compact) {
    const w = 90, h = 45;
    viewBox = `${Math.max(0, Math.min(360 - w, px - w/2))} ${Math.max(0, Math.min(180 - h, py - h/2))} ${w} ${h}`;
    pinR = 2.4;
  } else {
    viewBox = '0 0 360 180';
    pinR = 3.2;
  }

  const grid = [];
  for (let i = 0; i <= 360; i += 20) grid.push(<line key={`v${i}`} x1={i} y1={0} x2={i} y2={180} stroke={theme.mapLine} strokeWidth={0.4} />);
  for (let j = 0; j <= 180; j += 20) grid.push(<line key={`h${j}`} x1={0} y1={j} x2={360} y2={j} stroke={theme.mapLine} strokeWidth={0.4} />);
  // Equator + prime meridian slightly stronger
  grid.push(<line key="eq" x1={0} y1={90} x2={360} y2={90} stroke={theme.mapLineStrong} strokeWidth={0.5} />);
  grid.push(<line key="pm" x1={180} y1={0} x2={180} y2={180} stroke={theme.mapLineStrong} strokeWidth={0.5} />);

  return (
    <div style={{
      position: 'relative',
      width, height,
      borderRadius: 14,
      overflow: 'hidden',
      background: theme.mapBg,
      border: `1px solid ${theme.border}`,
    }}>
      <svg width="100%" height="100%" viewBox={viewBox} preserveAspectRatio="xMidYMid slice">
        {grid}
        {CONTINENT_BLOBS.map((d, i) => (
          <path key={i} d={d} fill={theme.dark ? 'rgba(140,180,220,0.11)' : 'rgba(11,16,32,0.09)'} stroke={theme.dark ? 'rgba(140,180,220,0.22)' : 'rgba(11,16,32,0.18)'} strokeWidth={0.4} />
        ))}
        {/* pin crosshair */}
        <line x1={px - 10} y1={py} x2={px + 10} y2={py} stroke={theme.accent} strokeWidth={0.5} opacity={0.6} />
        <line x1={px} y1={py - 10} x2={px} y2={py + 10} stroke={theme.accent} strokeWidth={0.5} opacity={0.6} />
        {live && (
          <>
            <circle cx={px} cy={py} r={pinR * 3} fill="none" stroke={theme.live} strokeWidth={0.4} opacity={0.45}>
              <animate attributeName="r" from={pinR} to={pinR * 5} dur="2s" repeatCount="indefinite" />
              <animate attributeName="opacity" from="0.6" to="0" dur="2s" repeatCount="indefinite" />
            </circle>
            <circle cx={px} cy={py} r={pinR * 2} fill="none" stroke={theme.live} strokeWidth={0.3} opacity={0.25} />
          </>
        )}
        <circle cx={px} cy={py} r={pinR} fill={theme.accent} stroke={theme.dark ? '#04131A' : '#fff'} strokeWidth={0.6} />
      </svg>
      {/* coord corner readout */}
      {!compact && (
        <div style={{
          position: 'absolute', left: 10, bottom: 8,
          fontFamily: 'JetBrains Mono, ui-monospace, monospace',
          fontSize: 10, letterSpacing: 0.2,
          color: theme.textDim, textTransform: 'uppercase',
          display: 'flex', gap: 10,
        }}>
          <span>LAT {lat.toFixed(4)}</span>
          <span>LNG {long.toFixed(4)}</span>
        </div>
      )}
    </div>
  );
}

// Radar — concentric rings + sweeping needle, rendered in a fixed box.
function Radar({ theme, size = 132, live = true }) {
  const c = size / 2;
  return (
    <div style={{ width: size, height: size, position: 'relative' }}>
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
        <defs>
          <radialGradient id="radarFill" cx="50%" cy="50%" r="50%">
            <stop offset="0%" stopColor={theme.live} stopOpacity="0.28" />
            <stop offset="70%" stopColor={theme.live} stopOpacity="0.04" />
            <stop offset="100%" stopColor={theme.live} stopOpacity="0" />
          </radialGradient>
          <linearGradient id="sweepGrad" x1="0" y1="0" x2="1" y2="0">
            <stop offset="0%" stopColor={theme.live} stopOpacity="0" />
            <stop offset="100%" stopColor={theme.live} stopOpacity="0.8" />
          </linearGradient>
        </defs>
        <circle cx={c} cy={c} r={c - 4} fill="url(#radarFill)" />
        {[0.25, 0.5, 0.75, 1].map((t, i) => (
          <circle key={i} cx={c} cy={c} r={(c - 4) * t} fill="none" stroke={theme.live} strokeOpacity={0.35 - i * 0.06} strokeWidth={0.8} />
        ))}
        <line x1={c} y1={4} x2={c} y2={size - 4} stroke={theme.live} strokeOpacity={0.2} strokeWidth={0.6} />
        <line x1={4} y1={c} x2={size - 4} y2={c} stroke={theme.live} strokeOpacity={0.2} strokeWidth={0.6} />
        {live && (
          <g style={{ transformOrigin: `${c}px ${c}px`, animation: 'radarSweep 3.4s linear infinite' }}>
            <path d={`M ${c} ${c} L ${c + (c - 4)} ${c} A ${c - 4} ${c - 4} 0 0 0 ${c + (c - 4) * Math.cos(-Math.PI / 4)} ${c + (c - 4) * Math.sin(-Math.PI / 4)} Z`} fill="url(#sweepGrad)" />
          </g>
        )}
        {/* center dot with pulse */}
        {live && (
          <circle cx={c} cy={c} r={10} fill="none" stroke={theme.live} strokeWidth={1}>
            <animate attributeName="r" from="3" to="24" dur="2.2s" repeatCount="indefinite" />
            <animate attributeName="opacity" from="0.8" to="0" dur="2.2s" repeatCount="indefinite" />
          </circle>
        )}
        <circle cx={c} cy={c} r={3.5} fill={theme.live} />
      </svg>
      <style>{`@keyframes radarSweep { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }`}</style>
    </div>
  );
}

Object.assign(window, { MiniMap, Radar });
