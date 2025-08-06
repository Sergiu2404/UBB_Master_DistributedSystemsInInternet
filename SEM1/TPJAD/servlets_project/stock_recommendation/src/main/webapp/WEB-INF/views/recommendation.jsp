<%--
  Created by IntelliJ IDEA.
  User: SergiuGoian
  Date: 05.08.2025
  Time: 20:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Portfolio Recommendations</title>
</head>
<body>

<h2>Portfolio Recommendations</h2>

<!-- Debug info -->
<c:if test="${not empty analysisType}">
  <h3>${analysisType}</h3>
</c:if>

<!-- Show current portfolio for debugging -->
<c:if test="${not empty currentPortfolio}">
  <h4>Current Portfolio (${currentPortfolio.size()} stocks):</h4>
  <ul>
    <c:forEach var="ps" items="${currentPortfolio}">
      <li>${ps.stock.ticker} - ${ps.stock.companyName} (${ps.quantity} shares, ${ps.stock.industry})</li>
    </c:forEach>
  </ul>
</c:if>

<!-- Main feature: Balanced Portfolio -->
<c:if test="${not empty balancedPortfolio}">
  <h3>🎯 Recommended Balanced Portfolio</h3>
  <p>This recommendation considers both industry diversification and quantity balance:</p>
  <table border="1">
    <thead>
    <tr>
      <th>Ticker</th>
      <th>Company</th>
      <th>Industry</th>
      <th>Recommended Quantity</th>
      <th>Description</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="ps" items="${balancedPortfolio}">
      <tr>
        <td><strong>${ps.stock.ticker}</strong></td>
        <td>${ps.stock.companyName}</td>
        <td>${ps.stock.industry}</td>
        <td>${ps.quantity}</td>
        <td>${ps.stock.description}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>

  <p><strong>Total recommended stocks:</strong> ${balancedPortfolio.size()}</p>
</c:if>

<!-- Missing Industries Recommendations -->
<c:if test="${not empty recommendations}">
  <h3>🏭 Industry Diversification Recommendations</h3>
  <c:forEach var="entry" items="${recommendations}">
    <h4>Industry: ${entry.key}</h4>
    <ul>
      <c:forEach var="stock" items="${entry.value}">
        <li>
          <strong>${stock.ticker}</strong> - ${stock.companyName}
          <br><small>${stock.description}</small>
        </li>
      </c:forEach>
    </ul>
  </c:forEach>
</c:if>

<!-- Overconcentrated Stocks -->
<c:if test="${not empty overconcentratedStocks}">
  <h3>⚠️ Overconcentrated Holdings</h3>
  <p>These stocks may represent too large a portion of your portfolio:</p>
  <table border="1">
    <thead>
    <tr>
      <th>Ticker</th>
      <th>Company</th>
      <th>Industry</th>
      <th>Current Quantity</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="ps" items="${overconcentratedStocks}">
      <tr>
        <td><strong>${ps.stock.ticker}</strong></td>
        <td>${ps.stock.companyName}</td>
        <td>${ps.stock.industry}</td>
        <td style="color: red;">${ps.quantity}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</c:if>

<!-- Error Display -->
<c:if test="${not empty error}">
  <div style="color: red; font-weight: bold; padding: 10px; border: 1px solid red; background-color: #ffe6e6;">
    ❌ ${error}
  </div>
</c:if>

<!-- No results message -->
<c:if test="${empty balancedPortfolio and empty recommendations and empty overconcentratedStocks and empty error}">
  <p><em>No recommendations generated. This might indicate your portfolio is already well-balanced!</em></p>
</c:if>

<br><br>
<a href="${pageContext.request.contextPath}/portfolio">← Back to Portfolio</a>

<!-- Additional Analysis Options -->
<hr>
<h3>Other Analysis Options:</h3>
<form method="get" action="${pageContext.request.contextPath}/recommendation" style="display: inline;">
  <input type="hidden" name="mode" value="balancedPortfolio"/>
  <input type="hidden" name="maxIndustryPercentage" value="25"/>
  <input type="hidden" name="maxStockQuantity" value="100"/>
  <input type="hidden" name="suggestedStockQuantity" value="20"/>
  <button type="submit">🎯 Balanced Portfolio Analysis</button>
</form>

<form method="get" action="${pageContext.request.contextPath}/recommendation" style="display: inline;">
  <input type="hidden" name="mode" value="missingIndustries"/>
  <button type="submit">🏭 Find Missing Industries</button>
</form>

<form method="get" action="${pageContext.request.contextPath}/recommendation" style="display: inline;">
  <input type="hidden" name="mode" value="overconcentratedStocks"/>
  <input type="hidden" name="maxQuantity" value="50"/>
  <button type="submit">⚠️ Check Overconcentration</button>
</form>

</body>
</html>