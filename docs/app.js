/* Minimal DVS web client for GitHub Pages */
let dataSet = { name: 'Untitled', columns: [], rows: [] };
let chart;
const $ = (sel) => document.querySelector(sel);
const $$ = (sel) => Array.from(document.querySelectorAll(sel));

const PALETTES = {
  classic: ['#4f46e5','#16a34a','#f59e0b','#dc2626','#0891b2','#7c3aed','#f97316','#059669'],
  cbf: ['#0072B2','#E69F00','#009E73','#F0E442','#56B4E9','#D55E00','#CC79A7','#999999'],
  pastel: ['#a78bfa','#86efac','#fde68a','#fca5a5','#93c5fd','#fdba74','#c4b5fd','#99f6e4'],
  vivid: ['#3b82f6','#22c55e','#f97316','#ef4444','#06b6d4','#8b5cf6','#eab308','#10b981']
};

function setData(name, columns, rows) {
  dataSet = { name, columns, rows };
  renderPreview();
  suggestChartTypes();
  populateColumnPickers();
}

function renderPreview() {
  const el = $('#preview');
  const cols = dataSet.columns;
  const rows = dataSet.rows.slice(0, 10);
  if (!cols.length) { el.textContent = 'No data loaded'; return; }
  let html = '<table><thead><tr>' + cols.map(c=>`<th>${c}</th>`).join('') + '</tr></thead><tbody>';
  for (const r of rows) html += '<tr>' + r.map(v=>`<td>${v ?? ''}</td>`).join('') + '</tr>';
  html += '</tbody></table>';
  if (dataSet.rows.length > 10) html += `<div class="muted">... and ${dataSet.rows.length-10} more rows</div>`;
  el.innerHTML = html;
}

function parseCSV(text, name='CSV Data') {
  const res = Papa.parse(text.trim(), { skipEmptyLines: true });
  const [header, ...rows] = res.data;
  const parsed = rows.map(r => r.map(v => {
    const t = String(v).trim();
    if (t === '') return '';
    const n = Number(t);
    return Number.isFinite(n) ? n : t;
  }));
  setData(name, header, parsed);
}

function populateColumnPickers() {
  const xSel = $('#xColumn');
  const ySel = $('#yColumn');
  if (!xSel || !ySel) return;
  xSel.innerHTML = '';
  ySel.innerHTML = '';
  dataSet.columns.forEach((c, i) => {
    xSel.appendChild(new Option(c, String(i)));
    ySel.appendChild(new Option(c, String(i)));
  });
  if (dataSet.columns.length >= 2) {
    xSel.value = '0';
    ySel.value = '1';
  }
}

function inferNumericColumns() {
  const numericIdx = [];
  for (let c = 0; c < dataSet.columns.length; c++) {
    let ok = 0, seen = 0;
    for (let r = 0; r < Math.min(50, dataSet.rows.length); r++) {
      const v = dataSet.rows[r][c];
      if (v === '' || v == null) continue;
      seen++;
      if (typeof v === 'number') ok++;
    }
    if (seen && ok/seen > 0.7) numericIdx.push(c);
  }
  return numericIdx;
}

function suggestChartTypes() {
  const insights = $('#insights');
  insights.innerHTML = '';
  if (!dataSet.columns.length) return;
  const numeric = inferNumericColumns();
  const suggestions = [];
  if (dataSet.columns.length >= 2) {
    if (numeric.length >= 2) suggestions.push('Line Chart', 'Scatter Plot');
    if (numeric.length >= 1) suggestions.push('Bar Chart', 'Pie Chart');
  }
  const ul = suggestions.map(s => `<li>Suggested: ${s}</li>`).join('');
  insights.innerHTML = ul || '<li>No suggestions yet — add more numeric columns.</li>';
}

function toChartJsDataset(type) {
  const c0 = Number($('#xColumn')?.value ?? 0);
  const c1 = Number($('#yColumn')?.value ?? 1);
  const labels = dataSet.rows.map(r => String(r[c0]));
  const values = dataSet.rows.map(r => Number(r[c1]) || 0);
  if (type === 'Pie Chart') {
    return { labels, data: values };
  }
  return { labels, datasets: [{ label: dataSet.columns[c1] || 'Value', data: values }] };
}

function renderChart() {
  const type = $('#chartType').value;
  const ctx = $('#chartCanvas').getContext('2d');
  chart && chart.destroy();
  if (!dataSet.columns.length || dataSet.columns.length < 2) {
    alert('Need at least two columns (Category, Value)');
    return;
  }
  const cfg = { type: 'bar', data: {}, options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { display: type !== 'Pie Chart' }, title: { display: !!$('#chartTitle').value, text: $('#chartTitle').value } } } };
  const d = toChartJsDataset(type);
  const palette = PALETTES[$('#palette')?.value || 'classic'];
  if (type === 'Bar Chart') {
    cfg.type = 'bar'; cfg.data = d; cfg.data.datasets[0].backgroundColor = palette; cfg.data.datasets[0].borderWidth = 0; }
  else if (type === 'Pie Chart') {
    cfg.type = 'pie'; cfg.data = { labels: d.labels, datasets: [{ data: d.data, backgroundColor: palette }] }; }
  else if (type === 'Line Chart') {
    cfg.type = 'line'; cfg.data = d; cfg.data.datasets[0].borderColor = palette[0]; cfg.data.datasets[0].backgroundColor = 'transparent'; cfg.data.datasets[0].tension = 0.25; }
  else if (type === 'Scatter Plot') {
    cfg.type = 'scatter'; cfg.data = { datasets: [{ label: d.datasets?.[0]?.label || 'Series', data: dataSet.rows.map(r => ({ x: Number(r[0]) || 0, y: Number(r[1]) || 0 })) }] };
    cfg.options.scales = { x: { type: 'linear' } };
    cfg.data.datasets[0].pointBackgroundColor = palette[0];
  }
  chart = new Chart(ctx, cfg);
}

function exportPng() {
  if (!chart) { alert('Render a chart first'); return; }
  const link = document.createElement('a');
  link.download = `${dataSet.name.replace(/\W+/g,'_')}_chart.png`;
  link.href = $('#chartCanvas').toDataURL('image/png');
  link.click();
}

// Wire up UI
$('#fileInput').addEventListener('change', (e) => {
  const file = e.target.files?.[0];
  if (!file) return;
  const reader = new FileReader();
  reader.onload = () => parseCSV(String(reader.result), file.name);
  reader.readAsText(file);
});

$('#loadSample').addEventListener('click', async () => {
  const res = await fetch('sample.csv');
  const text = await res.text();
  parseCSV(text, 'Sample Sales Data');
});

$('#renderChart').addEventListener('click', renderChart);
$('#exportPng').addEventListener('click', exportPng);

// Cleaning options
function applyCleaning() {
  if (!dataSet.columns.length) return;
  const trim = $('#optTrim')?.checked;
  const dropEmpty = $('#optDropEmpty')?.checked;
  const infer = $('#optInfer')?.checked;
  let cols = dataSet.columns.slice();
  let rows = dataSet.rows.map(r => r.slice());
  if (trim) {
    cols = cols.map(c => String(c).trim());
    rows = rows.map(r => r.map(v => typeof v === 'string' ? v.trim() : v));
  }
  if (dropEmpty) {
    rows = rows.filter(r => r.some(v => v !== '' && v != null));
  }
  if (infer) {
    rows = rows.map(r => r.map(v => {
      if (typeof v !== 'string') return v;
      if (v === '') return '';
      const n = Number(v);
      return Number.isFinite(n) ? n : v;
    }));
  }
  setData(dataSet.name, cols, rows);
}
$('#applyClean')?.addEventListener('click', applyCleaning);

// Basic profiling & insights
function profileData() {
  const el = $('#profile');
  if (!el || !dataSet.columns.length) { if (el) el.innerHTML = ''; return; }
  const rows = dataSet.rows.length;
  const cols = dataSet.columns.length;
  const numeric = inferNumericColumns().length;
  el.innerHTML = `Rows: <b>${rows}</b> • Columns: <b>${cols}</b> • Numeric columns: <b>${numeric}</b>`;
}

function richerInsights() {
  const list = $('#insights');
  if (!list) return;
  list.innerHTML = '';
  if (dataSet.columns.length < 2 || !dataSet.rows.length) return;
  const c0 = Number($('#xColumn')?.value ?? 0);
  const c1 = Number($('#yColumn')?.value ?? 1);
  const pairs = dataSet.rows
    .map(r => ({ x: r[c0], y: Number(r[c1]) }))
    .filter(p => p.x != null && p.x !== '' && Number.isFinite(p.y));
  if (!pairs.length) return;
  const ys = pairs.map(p => p.y);
  const sum = ys.reduce((a,b)=>a+b,0);
  const mean = sum / ys.length;
  const min = Math.min(...ys), max = Math.max(...ys);
  const top = pairs.reduce((m,p)=> p.y>m.y? p:m, pairs[0]);
  const html = [
    `<li>Mean of <b>${dataSet.columns[c1]||'Y'}</b>: ${mean.toFixed(2)} (min ${min}, max ${max})</li>`,
    `<li>Top category: <b>${String(top.x)}</b> with value <b>${top.y}</b></li>`
  ].join('');
  list.innerHTML = html;
}

function refreshSidebars() { profileData(); richerInsights(); }

// Refresh sidebars after data changes
const _setData = setData;
setData = function(name, columns, rows) {
  dataSet = { name, columns, rows };
  renderPreview();
  suggestChartTypes();
  populateColumnPickers();
  refreshSidebars();
}

// Drag & drop support
window.addEventListener('dragover', e => { e.preventDefault(); });
window.addEventListener('drop', e => {
  e.preventDefault();
  const file = e.dataTransfer.files?.[0];
  if (!file) return;
  const reader = new FileReader();
  reader.onload = () => parseCSV(String(reader.result), file.name);
  reader.readAsText(file);
});
