// Custom coordinates bottom sheet — replaces the boring 3-input form.
// - Live-validating lat/lng with a mini map preview that updates.
// - Name field with a quick "Use nearest city" hint.
// - Big CTA that's disabled until valid.
// - Drag handle and semi-transparent backdrop.

function CustomSheetScreen({ theme }) {
  const t = theme;
  const lat = 37.7749, long = -122.4194; // Sample: San Francisco-ish for preview
  return (
    <div style={{
      minHeight: '100%', background: t.bg, position: 'relative',
      fontFamily: 'Inter, system-ui, sans-serif', color: t.text,
    }}>
      {/* Dimmed underlying content */}
      <div style={{ opacity: 0.35, filter: 'blur(2px)', pointerEvents: 'none' }}>
        <HomeScreen theme={t} state="off" selectedIndex={0} />
      </div>
      {/* Scrim */}
      <div style={{ position: 'absolute', inset: 0, background: t.dark ? 'rgba(0,0,0,0.55)' : 'rgba(11,16,32,0.35)' }} />
      {/* Sheet */}
      <div style={{
        position: 'absolute', left: 0, right: 0, bottom: 0,
        background: t.bgElev, borderTopLeftRadius: 28, borderTopRightRadius: 28,
        boxShadow: '0 -20px 40px rgba(0,0,0,0.25)',
        padding: '10px 20px 28px',
        borderTop: `1px solid ${t.border}`,
      }}>
        <div style={{ width: 40, height: 4, borderRadius: 4, background: t.textMute, opacity: 0.5, margin: '0 auto 12px' }} />
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 4 }}>
          <div style={{ fontSize: 22, fontWeight: 700, letterSpacing: -0.5 }}>Custom location</div>
          <div style={{
            width: 32, height: 32, borderRadius: 10,
            background: t.chipBg, display: 'flex', alignItems: 'center', justifyContent: 'center',
          }}>
            <Icon name="x" size={16} color={t.textDim} />
          </div>
        </div>
        <div style={{ fontSize: 13, color: t.textDim, marginBottom: 16 }}>
          Enter a label and coordinates. Saved to your library.
        </div>

        {/* Name field */}
        <Field theme={t} label="Name" value="Dogpatch Dev HQ" hint="A friendly label for your library." />

        {/* Lat / Lng row */}
        <div style={{ display: 'flex', gap: 10, marginTop: 12 }}>
          <Field theme={t} label="Latitude" value="37.7749" mono hint="−90 to 90" flex />
          <Field theme={t} label="Longitude" value="−122.4194" mono hint="−180 to 180" flex />
        </div>

        {/* Validation strip */}
        <div style={{
          marginTop: 12, padding: '10px 12px', borderRadius: 12,
          border: `1px solid ${t.accentGhost}`, background: t.accentSoft,
          display: 'flex', alignItems: 'center', gap: 10,
          fontSize: 12, color: t.accent, fontWeight: 600,
        }}>
          <Icon name="check" size={16} color={t.accent} />
          Valid coordinates · Closest city: <span style={{ color: t.text, fontWeight: 600 }}>San Francisco, US</span>
        </div>

        {/* CTA */}
        <button style={{
          width: '100%', height: 56, marginTop: 14,
          borderRadius: 16, border: 'none', cursor: 'pointer',
          background: t.accent, color: t.accentInk,
          display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 10,
          fontSize: 16, fontWeight: 600, letterSpacing: -0.2,
          boxShadow: `0 12px 30px -10px ${t.liveGlow}`,
        }}>
          <Icon name="pin" size={18} color={t.accentInk} />
          Set mock location
        </button>
        <div style={{ textAlign: 'center', fontSize: 12, color: t.textMute, marginTop: 10 }}>
          Tip: paste <span style={{ fontFamily: 'JetBrains Mono, ui-monospace, monospace', color: t.textDim }}>37.7749, −122.4194</span> into any field
        </div>
      </div>
    </div>
  );
}

function Field({ theme, label, value, hint, mono, flex }) {
  const t = theme;
  return (
    <div style={{ flex: flex ? 1 : undefined, minWidth: 0, marginTop: 14 }}>
      <div style={{
        fontSize: 10, letterSpacing: 1.4, textTransform: 'uppercase',
        color: t.textDim, fontWeight: 700, marginBottom: 6,
      }}>{label}</div>
      <div style={{
        height: 54, padding: '0 14px',
        background: t.chipBg, border: `1px solid ${t.borderStrong}`,
        borderRadius: 14, display: 'flex', alignItems: 'center',
        fontSize: 16, color: t.text, fontWeight: 500, letterSpacing: -0.1,
        fontFamily: mono ? 'JetBrains Mono, ui-monospace, monospace' : 'Inter, system-ui, sans-serif',
      }}>
        {value}
      </div>
      {hint && (
        <div style={{ fontSize: 11, color: t.textMute, marginTop: 5, letterSpacing: 0 }}>{hint}</div>
      )}
    </div>
  );
}

// Setup instructions screen — redesigned as a checklist with numbered steps.
function SetupScreen({ theme }) {
  const t = theme;
  const steps = [
    {
      title: 'Open Developer Options',
      body: 'On your device, go to Settings → System → Developer options.',
      mono: 'adb shell am start -a android.settings.APPLICATION_DEVELOPMENT_SETTINGS',
    },
    {
      title: 'Find "Select mock location app"',
      body: 'Scroll to the Debugging section. Tap the row.',
      mono: null,
    },
    {
      title: 'Pick Mock Location',
      body: 'Select this app from the list. You\'ll see it appear in your debug surface.',
      mono: null,
    },
  ];
  return (
    <div style={{ minHeight: '100%', background: t.bg, fontFamily: 'Inter, system-ui, sans-serif', color: t.text }}>
      {/* top bar */}
      <div style={{ padding: '18px 20px 4px', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div style={{
          width: 40, height: 40, borderRadius: 12,
          background: t.chipBg, border: `1px solid ${t.border}`,
          display: 'flex', alignItems: 'center', justifyContent: 'center',
        }}>
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke={t.textDim} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="m15 6-6 6 6 6" /></svg>
        </div>
        <div style={{ fontSize: 13, fontWeight: 600, color: t.textDim }}>Setup · step 1 of 3</div>
        <div style={{ width: 40, height: 40 }} />
      </div>

      {/* hero */}
      <div style={{ padding: '24px 22px 10px' }}>
        <div style={{
          display: 'inline-flex', alignItems: 'center', gap: 8,
          padding: '6px 11px 6px 9px', borderRadius: 100,
          background: t.accentSoft, border: `1px solid ${t.accentGhost}`,
          fontSize: 11, fontWeight: 700, letterSpacing: 0.8, color: t.accent, textTransform: 'uppercase',
          marginBottom: 14,
        }}>
          <Icon name="info" size={13} color={t.accent} />
          One-time setup
        </div>
        <div style={{ fontSize: 34, fontWeight: 700, letterSpacing: -1.2, lineHeight: 1.04, textWrap: 'balance' }}>
          Let Android know<br /><span style={{ color: t.textDim }}>we're the boss of GPS.</span>
        </div>
        <div style={{ fontSize: 14, color: t.textDim, marginTop: 12, lineHeight: 1.5 }}>
          Android requires you to manually authorize a mock-location provider. Takes about 20 seconds.
        </div>
      </div>

      {/* steps */}
      <div style={{ padding: '18px 16px 0', display: 'flex', flexDirection: 'column', gap: 10 }}>
        {steps.map((s, i) => (
          <div key={i} style={{
            padding: '14px 14px 14px 14px',
            borderRadius: 18,
            background: t.card,
            border: `1px solid ${t.border}`,
            display: 'flex', gap: 14,
          }}>
            <div style={{
              width: 32, height: 32, borderRadius: 10,
              background: i === 0 ? t.accent : t.chipBg,
              color: i === 0 ? t.accentInk : t.textDim,
              display: 'flex', alignItems: 'center', justifyContent: 'center',
              fontWeight: 700, fontSize: 14, flexShrink: 0,
              fontFamily: 'JetBrains Mono, ui-monospace, monospace',
            }}>{String(i + 1).padStart(2, '0')}</div>
            <div style={{ flex: 1, minWidth: 0 }}>
              <div style={{ fontSize: 15, fontWeight: 600, letterSpacing: -0.2 }}>{s.title}</div>
              <div style={{ fontSize: 13, color: t.textDim, marginTop: 3, lineHeight: 1.5 }}>{s.body}</div>
              {s.mono && (
                <div style={{
                  marginTop: 10, padding: '8px 10px',
                  fontFamily: 'JetBrains Mono, ui-monospace, monospace',
                  fontSize: 11, color: t.textDim, letterSpacing: -0.1,
                  background: t.dark ? 'rgba(255,255,255,0.03)' : 'rgba(11,16,32,0.04)',
                  border: `1px dashed ${t.border}`, borderRadius: 8,
                  overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap',
                }}>
                  {s.mono}
                </div>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* CTA cluster */}
      <div style={{ padding: '18px 16px 0' }}>
        <button style={{
          width: '100%', height: 54,
          borderRadius: 16, border: 'none', cursor: 'pointer',
          background: t.accent, color: t.accentInk,
          display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 10,
          fontSize: 15, fontWeight: 600, letterSpacing: -0.2,
          boxShadow: `0 12px 30px -10px ${t.liveGlow}`,
        }}>
          <Icon name="arrow-up-right" size={16} color={t.accentInk} />
          Open developer options
        </button>
        <button style={{
          width: '100%', height: 48, marginTop: 8,
          borderRadius: 14, cursor: 'pointer',
          background: 'transparent', color: t.textDim,
          border: `1px solid ${t.border}`,
          fontSize: 14, fontWeight: 600,
        }}>
          I've done this — check again
        </button>
      </div>
    </div>
  );
}

Object.assign(window, { CustomSheetScreen, SetupScreen });
