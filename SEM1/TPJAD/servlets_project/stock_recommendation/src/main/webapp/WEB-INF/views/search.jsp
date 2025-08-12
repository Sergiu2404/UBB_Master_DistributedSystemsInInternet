<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<!DOCTYPE html>--%>
<%--<html>--%>
<%--<head>--%>
<%--  <title>Stock Search</title>--%>
<%--  <meta name="viewport" content="width=device-width, initial-scale=1" />--%>
<%--  <style>--%>
<%--    body { font-family: system-ui,-apple-system,Segoe UI,Roboto,Arial,sans-serif; margin: 2rem; }--%>
<%--    h1 { margin-bottom: 1rem; }--%>
<%--    form { display: grid; grid-template-columns: 2fr auto; gap: .75rem; max-width: 700px; align-items: end; }--%>
<%--    label { display: grid; gap: .25rem; font-size: .9rem; }--%>
<%--    input { padding: .55rem .6rem; font-size: 1rem; }--%>
<%--    button { padding: .6rem 1rem; font-size: 1rem; cursor: pointer; }--%>
<%--    #status { margin-top: 1rem; font-size: .95rem; color: #555; }--%>
<%--    table { width: 100%; border-collapse: collapse; margin-top: .75rem; }--%>
<%--    th, td { border: 1px solid #e2e2e2; padding: .45rem .55rem; text-align: left; }--%>
<%--    th { background: #f8f8f8; }--%>
<%--    .num { text-align: right; white-space: nowrap; }--%>
<%--    .pos { color: #0a7a00; }--%>
<%--    .neg { color: #b10000; }--%>
<%--    .hint { font-size: .85rem; color: #666; margin-top: .25rem; }--%>
<%--  </style>--%>
<%--</head>--%>
<%--<body>--%>
<%--<h1>Stock Search</h1>--%>

<%--<form id="frm" method="post" action="<%=request.getContextPath()%>/api/search">--%>
<%--  <label>Ticker--%>
<%--    <input type="text" name="q" id="q" required placeholder="e.g. AAPL" />--%>
<%--    <div class="hint">Search one ticker at a time.</div>--%>
<%--  </label>--%>
<%--  <button type="submit">Search</button>--%>
<%--</form>--%>

<%--<div id="status">Enter a ticker and press Search.</div>--%>

<%--<table id="results" hidden>--%>
<%--  <thead>--%>
<%--  <tr>--%>
<%--    <th>Symbol</th>--%>
<%--    <th>Name</th>--%>
<%--    <th class="num">Price</th>--%>
<%--    <th class="num">Prev Close</th>--%>
<%--    <th class="num">Change</th>--%>
<%--    <th class="num">Change %</th>--%>
<%--    <th class="num">Volume</th>--%>
<%--    <th>As Of</th>--%>
<%--  </tr>--%>
<%--  </thead>--%>
<%--  <tbody></tbody>--%>
<%--</table>--%>

<%--<noscript>--%>
<%--  <p><em>JavaScript is disabled. The form will submit to the server and render results via JSP.</em></p>--%>
<%--</noscript>--%>

<%--<script>--%>
<%--  (function () {--%>
<%--    const frm = document.getElementById('frm');--%>
<%--    const statusEl = document.getElementById('status');--%>
<%--    const table = document.getElementById('results');--%>
<%--    const tbody = table.querySelector('tbody');--%>

<%--    const fmt = (v) => (v === null || v === undefined) ? '' : v;--%>
<%--    const fmtNum = (v) => (v === null || v === undefined || isNaN(v)) ? '' : Number(v).toLocaleString();--%>
<%--    const fmtPct = (v) => (v === null || v === undefined || isNaN(v)) ? '' : (Number(v).toFixed(2) + '%');--%>
<%--    const n = (v) => (v === null || v === undefined || v === '') ? '' : Number(v);--%>
<%--    const signCls = (v) => (v == null || isNaN(v)) ? '' : (Number(v) >= 0 ? 'pos' : 'neg');--%>
<%--    const fmtTs = (ms) => (ms ? new Date(ms).toLocaleString() : '');--%>

<%--    frm.addEventListener('submit', async function (e) {--%>
<%--      if (!window.fetch) return; // fallback to server render--%>
<%--      e.preventDefault();--%>

<%--      const q = document.getElementById('q').value.trim();--%>

<%--      const payload = { q };--%>

<%--      statusEl.textContent = 'Searching…';--%>
<%--      table.hidden = true;--%>
<%--      tbody.innerHTML = '';--%>

<%--      try {--%>
<%--        const res = await fetch('<%=request.getContextPath()%>/api/filter', {--%>
<%--          method: 'POST',--%>
<%--          headers: { 'Content-Type': 'application/json' },--%>
<%--          body: JSON.stringify(payload)--%>
<%--        });--%>
<%--        if (!res.ok) throw new Error('HTTP ' + res.status);--%>
<%--        const data = await res.json();--%>

<%--        if (!Array.isArray(data) || data.length === 0) {--%>
<%--          statusEl.textContent = 'No results.';--%>
<%--          return;--%>
<%--        }--%>

<%--        for (const r of data) {--%>
<%--          const tr = document.createElement('tr');--%>
<%--          tr.innerHTML = `--%>
<%--            <td>${fmt(r.symbol)}</td>--%>
<%--            <td>${fmt(r.name)}</td>--%>
<%--            <td class="num">${fmtNum(n(r.price))}</td>--%>
<%--            <td class="num">${fmtNum(n(r.previousClose))}</td>--%>
<%--            <td class="num ${signCls(n(r.change))}">${fmtNum(n(r.change))}</td>--%>
<%--            <td class="num ${signCls(n(r.changePercent))}">${fmtPct(n(r.changePercent))}</td>--%>
<%--            <td class="num">${fmtNum(n(r.volume))}</td>--%>
<%--            <td>${fmtTs(r.asOf)}</td>--%>
<%--          `;--%>
<%--          tbody.appendChild(tr);--%>
<%--        }--%>

<%--        statusEl.textContent = `Done.`;--%>
<%--        table.hidden = false;--%>
<%--      } catch (err) {--%>
<%--        statusEl.textContent = 'Error: ' + err.message;--%>
<%--        table.hidden = true;--%>
<%--      }--%>
<%--    });--%>
<%--  })();--%>
<%--</script>--%>
<%--</body>--%>
<%--</html>--%>





<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<%--Uncomment when using tomcat or wildfly, comment when using jetty--%>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>


<!DOCTYPE html>
<html>
<head>
  <title>Stock Search</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <style>
    body { font-family: system-ui,-apple-system,Segoe UI,Roboto,Arial,sans-serif; margin: 2rem; }
    h1 { margin-bottom: 1rem; }
    form { display: grid; grid-template-columns: 2fr auto; gap: .75rem; max-width: 700px; align-items: end; }
    label { display: grid; gap: .25rem; font-size: .9rem; }
    input { padding: .55rem .6rem; font-size: 1rem; }
    button { padding: .6rem 1rem; font-size: 1rem; cursor: pointer; }
    #status { margin-top: 1rem; font-size: .95rem; color: #555; }
    table { width: 100%; border-collapse: collapse; margin-top: .75rem; }
    th, td { border: 1px solid #e2e2e2; padding: .45rem .55rem; text-align: left; }
    th { background: #f8f8f8; }
    .num { text-align: right; white-space: nowrap; }
    .pos { color: #0a7a00; }
    .neg { color: #b10000; }
    .hint { font-size: .85rem; color: #666; margin-top: .25rem; }
  </style>
</head>
<body>
<h1>Stock Search</h1>

<form id="frm" method="post" action="${pageContext.request.contextPath}/api/search">
  <label>Ticker
    <input type="text" name="q" id="q" required placeholder="e.g. AAPL" />
    <div class="hint">Search one ticker at a time.</div>
  </label>
  <button type="submit">Search</button>
</form>

<div id="status">Enter a ticker and press Search.</div>

<table id="results" hidden>
  <thead>
  <tr>
    <th>Symbol</th>
    <th>Name</th>
    <th class="num">Price</th>
    <th class="num">Prev Close</th>
    <th class="num">Change</th>
    <th class="num">Change %</th>
    <th class="num">Volume</th>
    <th>As Of</th>
  </tr>
  </thead>
  <tbody></tbody>
</table>

<noscript>
  <p><em>JavaScript is disabled. The form will submit to the server and render results via JSP.</em></p>
</noscript>

<script>
  (function () {
    const frm = document.getElementById('frm');
    const statusEl = document.getElementById('status');
    const table = document.getElementById('results');
    const tbody = table.querySelector('tbody');

    const fmt = (v) => (v === null || v === undefined) ? '' : String(v).replace(/[<>&"']/g, function(m) {
      return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;',"'":'&#39;'}[m];
    });
    const fmtNum = (v) => (v === null || v === undefined || isNaN(v)) ? '' : Number(v).toLocaleString();
    const fmtPct = (v) => (v === null || v === undefined || isNaN(v)) ? '' : (Number(v).toFixed(2) + '%');
    const n = (v) => (v === null || v === undefined || v === '') ? '' : Number(v);
    const signCls = (v) => (v == null || isNaN(v)) ? '' : (Number(v) >= 0 ? 'pos' : 'neg');
    const fmtTs = (ms) => (ms ? new Date(ms).toLocaleString() : '');

    frm.addEventListener('submit', async function (e) {
      if (!window.fetch) return;
      e.preventDefault();

      const q = document.getElementById('q').value.trim();
      const payload = { q };

      statusEl.textContent = 'Searching…';
      table.hidden = true;
      tbody.innerHTML = '';

      try {
        const res = await fetch('${pageContext.request.contextPath}/api/filter', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload)
        });
        if (!res.ok) throw new Error('HTTP ' + res.status);
        const data = await res.json();

        if (!Array.isArray(data) || data.length === 0) {
          statusEl.textContent = 'No results.';
          return;
        }

        data.forEach(function(r) {
          const tr = document.createElement('tr');

          const symbolTd = document.createElement('td');
          symbolTd.textContent = fmt(r.symbol);
          tr.appendChild(symbolTd);

          const nameTd = document.createElement('td');
          nameTd.textContent = fmt(r.name);
          tr.appendChild(nameTd);

          const priceTd = document.createElement('td');
          priceTd.className = 'num';
          priceTd.textContent = fmtNum(n(r.price));
          tr.appendChild(priceTd);

          const prevCloseTd = document.createElement('td');
          prevCloseTd.className = 'num';
          prevCloseTd.textContent = fmtNum(n(r.previousClose));
          tr.appendChild(prevCloseTd);

          const changeTd = document.createElement('td');
          changeTd.className = 'num ' + signCls(n(r.change));
          changeTd.textContent = fmtNum(n(r.change));
          tr.appendChild(changeTd);

          const changePctTd = document.createElement('td');
          changePctTd.className = 'num ' + signCls(n(r.changePercent));
          changePctTd.textContent = fmtPct(n(r.changePercent));
          tr.appendChild(changePctTd);

          const volumeTd = document.createElement('td');
          volumeTd.className = 'num';
          volumeTd.textContent = fmtNum(n(r.volume));
          tr.appendChild(volumeTd);

          const asOfTd = document.createElement('td');
          asOfTd.textContent = fmtTs(r.asOf);
          tr.appendChild(asOfTd);

          tbody.appendChild(tr);
        });

        statusEl.textContent = 'Done.';
        table.hidden = false;
      } catch (err) {
        statusEl.textContent = 'Error: ' + err.message;
        table.hidden = true;
      }
    });
  })();
</script>
</body>
</html>