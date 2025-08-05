<%--
  Created by IntelliJ IDEA.
  User: SergiuGoian
  Date: 05.08.2025
  Time: 20:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<html>--%>
<%--<head>--%>
<%--    <title>My Portfolio</title>--%>
<%--</head>--%>
<%--<body>--%>
<%--  <h2>My Portfolio</h2>--%>

<%--  <table border="1">--%>
<%--    <thead>--%>
<%--      <tr>--%>
<%--        <th>Ticker</th>--%>
<%--        <th>Company</th>--%>
<%--        <th>Industry</th>--%>
<%--        <th>Quantity</th>--%>
<%--        <th>Description</th>--%>
<%--        <th>Website</th>--%>
<%--      </tr>--%>
<%--    </thead>--%>
<%--    <tbody>--%>
<%--      <c:forEach var="p" items = "${portfolio}">--%>
<%--        <tr>--%>
<%--          <td>${p.stock.ticker}</td>--%>
<%--          <td>${p.stock.companyName}</td>--%>
<%--          <td>${p.stock.industry}</td>--%>
<%--          <td>${p.quantity}</td>--%>
<%--          <td>${p.stock.description}</td>--%>
<%--          <td><a href="${p.stock.website}" target="_blank">${p.stock.website}</a></td>--%>
<%--        </tr>--%>
<%--      </c:forEach>--%>
<%--    </tbody>--%>
<%--  </table>--%>

<%--  <h3>Manage Portfolio</h3>--%>
<%--  <form method="post" action="${pageContext.request.contextPath}/portfolio">--%>
<%--    <input name="ticker" placeholder="Ticker (e.g. TLV)" required />--%>
<%--    <input name="quantity" type="number" placeholder="Quantity" required />--%>
<%--    <select name="action">--%>
<%--      <option value="add">Add</option>--%>
<%--      <option value="update">Update Quantity</option>--%>
<%--      <option value="delete">Remove</option>--%>
<%--    </select>--%>
<%--    <button type="submit">Submit</button>--%>
<%--  </form>--%>

<%--  <h3>Search Stock by Ticker</h3>--%>
<%--  <form method="get" action="${pageContext.request.contextPath}/portfolio">--%>
<%--    <input name="action" type="hidden" value="getStock"/>--%>
<%--    <input name="ticker" placeholder="e.g. SNP" required />--%>
<%--    <button type="submit">Search</button>--%>
<%--  </form>--%>

<%--  <c:if test="${not empty stock}">--%>
<%--    <h4>Stock Info</h4>--%>
<%--    <p><strong>${stock.ticker}</strong> - ${stock.companyName}</p>--%>
<%--    <p>Industry: ${stock.industry}</p>--%>
<%--    <p>${stock.description}</p>--%>
<%--    <p><a href="${stock.website}" target="_blank">Company Website</a></p>--%>
<%--  </c:if>--%>

<%--  <c:if test="${stockNotFound}">--%>
<%--    <p style="color:red;">Stock not found.</p>--%>
<%--  </c:if>--%>
<%--</body>--%>
<%--</html>--%>


<html>
<head>
  <title>My Portfolio</title>
</head>
<body>
<h2>My Portfolio</h2>

<table border="1">
  <thead>
  <tr>
    <th>Ticker</th>
    <th>Company</th>
    <th>Industry</th>
    <th>Quantity</th>
    <th>Description</th>
    <th>Website</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="p" items="${portfolio}">
    <tr>
      <td>${p.stock.ticker}</td>
      <td>${p.stock.companyName}</td>
      <td>${p.stock.industry}</td>
      <td>${p.quantity}</td>
      <td>${p.stock.description}</td>
      <td><a href="${p.stock.website}" target="_blank">${p.stock.website}</a></td>
    </tr>
  </c:forEach>
  </tbody>
</table>

<h3>Manage Portfolio</h3>
<form method="post" action="${pageContext.request.contextPath}/portfolio">
  <label>
    <select name="ticker" required>
      <c:forEach var="s" items="${availableStocks}">
        <option value="${s.ticker}">${s.ticker} - ${s.companyName}</option>
      </c:forEach>
    </select>
  </label>
  <input name="quantity" type="number" placeholder="Quantity" required />
  <label>
    <select name="action">
      <option value="add">Add</option>
      <option value="update">Update Quantity</option>
      <option value="delete">Remove</option>
    </select>
  </label>
  <button type="submit">Submit</button>
</form>

<h3>Search Stock by Ticker</h3>
<form method="get" action="${pageContext.request.contextPath}/portfolio">
  <input name="action" type="hidden" value="getStock" />
  <label>
    <input name="ticker" placeholder="e.g. SNP" required />
  </label>
  <button type="submit">Search</button>
</form>

<c:if test="${not empty stock}">
  <h4>Stock Info</h4>
  <p><strong>${stock.ticker}</strong> - ${stock.companyName}</p>
  <p>Industry: ${stock.industry}</p>
  <p>${stock.description}</p>
  <p><a href="${stock.website}" target="_blank">Company Website</a></p>
</c:if>

<c:if test="${stockNotFound}">
  <p style="color:red;">Stock not found.</p>
</c:if>

<form method="get" action="${pageContext.request.contextPath}/recommendation">
  <input type="hidden" name="mode" value="balancedPortfolio"/>
  <input type="hidden" name="maxIndustryPercentage" value="30"/>
  <input type="hidden" name="maxStockQuantity" value="100"/>
  <input type="hidden" name="suggestedStockQuantity" value="20"/>
  <button type="submit">Analyze Portfolio</button>
</form>
</body>
</html>