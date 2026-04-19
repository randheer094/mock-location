// Home screen — the hero of the app.
// Two states:
//   - ON  → "Broadcasting" hero with live radar + big stop button
//   - OFF → "Idle" hero prompting to start or pick a city
// Always below: Selected location card, Quick actions row, Presets list.

function HomeScreen({ theme, state = 'on', selectedIndex = 0 }) {
  const selected = window.LOCATIONS[selectedIndex];
  const isOn = state === 'on';
  const t = theme;

  const TopBar = (
    <div style={{
      display: 'flex', alignItems: 'center', justifyContent: 'space-between',
      padding: '18px 20px 8px',
    }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
        <Wordmark theme={t} size={22} />
      </div>
      <button title="Add custom location" style={{
        width: 40, height: 40, borderRadius: 12,
        background: t.accent, color: t.accentInk,
        border: 'none', cursor: 'pointer',
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        boxShadow: `0 8px 20px -8px ${t.liveGlow}`,
      }}>
        <Icon name="plus" size={18} color={t.accentInk} />
      </button>
    </div>
  );

  const Hero = isOn ? (
    <div style={{
      margin: '6px 16px 0',
      borderRadius: 28,
      overflow: 'hidden',
      position: 'relative',
      background: t.dark
        ? `linear-gradient(165deg, ${t.bgElev2} 0%, ${t.bgElev} 100%)`
        : `linear-gradient(165deg, #FFFFFF 0%, #F8F6F1 100%)`,
      border: `1px solid ${t.border}`,
      padding: '18px 18px 16px',
    }}>
      {/* top row: status badge + kebab */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <StatusPill theme={t} on />
        <div style={{
          fontFamily: 'JetBrains Mono, ui-monospace, monospace',
          fontSize: 11, letterSpacing: 1.2, color: t.textDim, textTransform: 'uppercase',
        }}>
          T+ 00:04:12
        </div>
      </div>

      {/* radar + label */}
      <div style={{ display: 'flex', alignItems: 'center', gap: 18, marginTop: 14 }}>
        <Radar theme={t} size={120} live />
        <div style={{ flex: 1, minWidth: 0 }}>
          <div style={{
            fontSize: 11, letterSpacing: 1.6, textTransform: 'uppercase',
            color: t.textDim, fontWeight: 600, marginBottom: 4,
          }}>Currently spoofing</div>
          <div style={{
            fontFamily: 'Inter, system-ui, sans-serif',
            fontSize: 26, lineHeight: 1.08, fontWeight: 600, color: t.text,
            letterSpacing: -0.5, textWrap: 'balance',
          }}>{selected.name.split(',')[0]}</div>
          <div style={{
            fontFamily: 'Inter, system-ui, sans-serif',
            fontSize: 14, color: t.textDim, fontWeight: 500, marginTop: 2,
          }}>{selected.country}</div>
          <div style={{
            fontFamily: 'JetBrains Mono, ui-monospace, monospace',
            fontSize: 11, color: t.textMute, marginTop: 8,
            display: 'flex', gap: 10, flexWrap: 'wrap',
          }}>
            <span>{fmtCoord(selected.lat, 'lat')}</span>
            <span>{fmtCoord(selected.long, 'lng')}</span>
          </div>
        </div>
      </div>

      {/* stop button */}
      <button style={{
        marginTop: 16, width: '100%', height: 56,
        borderRadius: 16, border: 'none', cursor: 'pointer',
        background: t.text, color: t.bg,
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        gap: 10, fontFamily: 'Inter, system-ui, sans-serif',
        fontSize: 16, fontWeight: 600, letterSpacing: -0.2,
      }}>
        <Icon name="stop" size={18} color={t.bg} />
        Stop broadcasting
      </button>
    </div>
  ) : (
    <div style={{
      margin: '6px 16px 0',
      borderRadius: 28, overflow: 'hidden',
      background: t.card, border: `1px solid ${t.border}`,
      padding: '18px 18px 16px',
    }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <StatusPill theme={t} on={false} />
        <div style={{
          fontFamily: 'JetBrains Mono, ui-monospace, monospace',
          fontSize: 11, letterSpacing: 1.2, color: t.textMute, textTransform: 'uppercase',
        }}>
          Ready
        </div>
      </div>
      <div style={{ marginTop: 14 }}>
        <MiniMap lat={selected.lat} long={selected.long} theme={t} width="100%" height={150} />
      </div>
      <div style={{ marginTop: 14, display: 'flex', alignItems: 'flex-end', justifyContent: 'space-between', gap: 12 }}>
        <div style={{ minWidth: 0, flex: 1 }}>
          <div style={{
            fontSize: 11, letterSpacing: 1.6, textTransform: 'uppercase',
            color: t.textDim, fontWeight: 600, marginBottom: 2,
          }}>Last used</div>
          <div style={{
            fontSize: 22, fontWeight: 600, color: t.text,
            letterSpacing: -0.4, lineHeight: 1.1,
          }}>{selected.name.split(',')[0]}, <span style={{ color: t.textDim, fontWeight: 500 }}>{selected.country}</span></div>
        </div>
        <button style={{
          height: 52, minWidth: 52, padding: '0 22px',
          borderRadius: 14, border: 'none', cursor: 'pointer',
          background: t.accent, color: t.accentInk,
          display: 'flex', alignItems: 'center', gap: 8,
          fontFamily: 'Inter, system-ui, sans-serif',
          fontSize: 15, fontWeight: 600, letterSpacing: -0.2,
          boxShadow: `0 10px 24px -8px ${t.liveGlow}`,
        }}>
          <Icon name="play" size={16} color={t.accentInk} />
          Start
        </button>
      </div>
    </div>
  );

  const ListHeader = (
    <div style={{
      display: 'flex', alignItems: 'baseline', justifyContent: 'space-between',
      padding: '22px 20px 10px',
    }}>
      <div style={{
        fontSize: 11, letterSpacing: 1.6, textTransform: 'uppercase',
        color: t.textDim, fontWeight: 600,
      }}>Preset locations</div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 6, color: t.textDim, fontSize: 13, fontWeight: 500 }}>
        <span>Sort · A–Z</span>
        <Icon name="chevron" size={14} color={t.textDim} />
      </div>
    </div>
  );

  return (
    <div style={{
      minHeight: '100%',
      background: t.bg,
      fontFamily: 'Inter, system-ui, sans-serif',
      color: t.text,
      paddingBottom: 12,
    }}>
      {TopBar}
      {Hero}
      {ListHeader}
      <div style={{ padding: '0 16px' }}>
        {window.LOCATIONS.slice(0, 7).map((loc, i) => (
          <LocationRow
            key={loc.name}
            theme={t}
            loc={loc}
            active={isOn && i === selectedIndex}
            selected={i === selectedIndex}
          />
        ))}
      </div>
    </div>
  );
}

// ────── Sub-components ──────

function Wordmark({ theme, size = 22 }) {
  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
      <div style={{
        width: size + 8, height: size + 8, borderRadius: 10,
        background: theme.accent, color: theme.accentInk,
        display: 'flex', alignItems: 'center', justifyContent: 'center',
      }}>
        <svg width={size - 4} height={size - 4} viewBox="0 0 24 24" fill="none">
          <path d="M12 22s-7-7-7-13a7 7 0 1 1 14 0c0 6-7 13-7 13Z" fill={theme.accentInk} />
          <circle cx="12" cy="9" r="2.3" fill={theme.accent} />
        </svg>
      </div>
      <div style={{
        fontFamily: 'Inter, system-ui, sans-serif',
        fontSize: size * 0.82, fontWeight: 700, letterSpacing: -0.6, color: theme.text,
      }}>
        mock<span style={{ color: theme.textDim, fontWeight: 600 }}>location</span>
      </div>
    </div>
  );
}

function IconButton({ theme, icon }) {
  return (
    <div style={{
      width: 40, height: 40, borderRadius: 12,
      background: theme.chipBg, border: `1px solid ${theme.border}`,
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      color: theme.textDim, cursor: 'pointer',
    }}>
      <Icon name={icon} size={18} color={theme.textDim} />
    </div>
  );
}

function StatusPill({ theme, on }) {
  return (
    <div style={{
      display: 'inline-flex', alignItems: 'center', gap: 8,
      padding: '6px 11px 6px 9px',
      borderRadius: 100,
      background: on ? theme.accentSoft : theme.chipBg,
      border: `1px solid ${on ? theme.accentGhost : theme.border}`,
      fontSize: 12, fontWeight: 600, letterSpacing: 0.2,
      color: on ? theme.accent : theme.textDim,
      fontFamily: 'Inter, system-ui, sans-serif',
    }}>
      <span style={{
        width: 7, height: 7, borderRadius: '50%',
        background: on ? theme.live : theme.textMute,
        boxShadow: on ? `0 0 0 3px ${theme.liveGlow}` : 'none',
      }} />
      {on ? 'LIVE · GPS + NET' : 'Mock location off'}
    </div>
  );
}

function QuickChip({ theme, icon, label, count }) {
  return (
    <div style={{
      flex: 1, height: 62,
      borderRadius: 14,
      background: theme.card,
      border: `1px solid ${theme.border}`,
      padding: '0 12px',
      display: 'flex', alignItems: 'center', gap: 10,
      cursor: 'pointer',
    }}>
      <div style={{
        width: 32, height: 32, borderRadius: 9,
        background: theme.chipBg, display: 'flex', alignItems: 'center', justifyContent: 'center',
        color: theme.text,
      }}>
        <Icon name={icon} size={16} color={theme.text} />
      </div>
      <div style={{ flex: 1, minWidth: 0 }}>
        <div style={{ fontSize: 13, fontWeight: 600, color: theme.text, letterSpacing: -0.1, lineHeight: 1.2 }}>{label}</div>
        {count !== undefined && (
          <div style={{ fontSize: 11, color: theme.textDim, marginTop: 2 }}>{count} saved</div>
        )}
      </div>
    </div>
  );
}

function LocationRow({ theme, loc, active, selected }) {
  return (
    <div style={{
      display: 'flex', alignItems: 'center', gap: 12,
      padding: '12px 12px 12px 10px',
      borderRadius: 14,
      background: selected ? theme.card : 'transparent',
      border: `1px solid ${selected ? theme.border : 'transparent'}`,
      marginBottom: 4,
      position: 'relative',
    }}>
      {/* country code tile */}
      <div style={{
        width: 44, height: 44, borderRadius: 11,
        background: active ? theme.accentSoft : theme.chipBg,
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        fontFamily: 'JetBrains Mono, ui-monospace, monospace',
        fontSize: 13, fontWeight: 700, letterSpacing: 0.5,
        color: active ? theme.accent : theme.textDim,
        border: `1px solid ${active ? theme.accentGhost : theme.border}`,
      }}>
        {loc.code}
      </div>
      <div style={{ flex: 1, minWidth: 0 }}>
        <div style={{
          fontSize: 15, fontWeight: 600, color: theme.text, letterSpacing: -0.2,
          display: 'flex', alignItems: 'center', gap: 8,
        }}>
          {loc.name.split(',')[0]}
          {active && (
            <span style={{
              display: 'inline-flex', alignItems: 'center', gap: 4,
              fontSize: 10, fontWeight: 700, letterSpacing: 0.8,
              color: theme.accent, textTransform: 'uppercase',
            }}>
              <span style={{ width: 6, height: 6, borderRadius: '50%', background: theme.live, boxShadow: `0 0 0 3px ${theme.liveGlow}` }} />
              LIVE
            </span>
          )}
        </div>
        <div style={{
          fontFamily: 'JetBrains Mono, ui-monospace, monospace',
          fontSize: 11, color: theme.textDim, marginTop: 3,
          display: 'flex', gap: 10,
        }}>
          <span>{loc.lat.toFixed(4)}</span>
          <span>{loc.long.toFixed(4)}</span>
        </div>
      </div>
      <MiniMap lat={loc.lat} long={loc.long} theme={theme} width={64} height={44} compact />
    </div>
  );
}

Object.assign(window, { HomeScreen, Wordmark, IconButton, StatusPill, QuickChip, LocationRow });
