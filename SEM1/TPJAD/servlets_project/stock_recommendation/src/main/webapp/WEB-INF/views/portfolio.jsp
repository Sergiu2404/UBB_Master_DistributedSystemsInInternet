<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<c:choose>
  <c:when test="${not empty stock}">
    <h4>Stock Info</h4>
    <p><strong>${stock.ticker}</strong> - ${stock.companyName}</p>
    <p>Industry: ${stock.industry}</p>
    <p>${stock.description}</p>
    <p><a href="${stock.website}" target="_blank">${stock.website}</a></p>

    <form method="post" action="${pageContext.request.contextPath}/portfolio">
      <input type="hidden" name="action" value="add" />
      <input type="hidden" name="ticker" value="${stock.ticker}" />
      <input type="number" name="quantity" placeholder="Quantity" required />
      <button type="submit">Add to Portfolio</button>
    </form>
  </c:when>

  <c:when test="${searched and stockNotFound}">
    <p style="color:red;">Stock not found.</p>
  </c:when>
</c:choose>

<form method="get" action="${pageContext.request.contextPath}/recommendation">
  <input type="hidden" name="mode" value="balancedPortfolio"/>
  <input type="hidden" name="maxIndustryPercentage" value="30"/>
  <input type="hidden" name="maxStockQuantity" value="100"/>
  <input type="hidden" name="suggestedStockQuantity" value="20"/>
  <button type="submit">Analyze Portfolio</button>
</form>
</body>
</html>
