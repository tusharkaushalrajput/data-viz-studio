/* Minimal DVS web client for GitHub Pages */
let dataSet = { name: 'Untitled', columns: [], rows: [] };
let chart;
const $ = (sel) => document.querySelector(sel);

function setData(name, columns, rows) {
  dataSet = { name, columns, rows };
  renderPreview();
  suggestChartTypes();
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
  const [c0, c1] = [0, 1];
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
  const cfg = { type: 'bar', data: {}, options: { responsive: true, maintainAspectRatio: false } };
  const d = toChartJsDataset(type);
  if (type === 'Bar Chart') {
    cfg.type = 'bar'; cfg.data = d; }
  else if (type === 'Pie Chart') {
    cfg.type = 'pie'; cfg.data = { labels: d.labels, datasets: [{ data: d.data }] }; }
  else if (type === 'Line Chart') {
    cfg.type = 'line'; cfg.data = d; }
  else if (type === 'Scatter Plot') {
    cfg.type = 'scatter'; cfg.data = { datasets: [{ label: d.datasets?.[0]?.label || 'Series', data: dataSet.rows.map(r => ({ x: Number(r[0]) || 0, y: Number(r[1]) || 0 })) }] };
    cfg.options.scales = { x: { type: 'linear' } };
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
