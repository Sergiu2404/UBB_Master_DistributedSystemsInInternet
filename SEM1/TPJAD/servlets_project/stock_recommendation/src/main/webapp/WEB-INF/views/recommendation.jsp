<%--
  Created by IntelliJ IDEA.
  User: SergiuGoian
  Date: 05.08.2025
  Time: 20:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Portfolio Recommendations</title>
</head>
<body>

<h2>Portfolio Recommendations</h2>

<c:if test="${not empty balancedPortfolio}">
  <h3>Balanced Portfolio Suggestion</h3>
  <table border="1">
    <thead>
    <tr>
      <th>Ticker</th>
      <th>Company</th>
      <th>Industry</th>
      <th>Quantity</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="ps" items="${balancedPortfolio}">
      <tr>
        <td>${ps.stock.ticker}</td>
        <td>${ps.stock.companyName}</td>
        <td>${ps.stock.industry}</td>
        <td>${ps.quantity}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</c:if>

<c:if test="${not empty recommendations}">
  <h3>Industry-Based Stock Recommendations</h3>
  <c:forEach var="entry" items="${recommendations}">
    <h4>Industry: ${entry.key}</h4>
    <ul>
      <c:forEach var="stock" items="${entry.value}">
        <li>
            ${stock.ticker} - ${stock.companyName} (${stock.industry})
          <a href="${stock.website}" target="_blank">Website</a>
        </li>
      </c:forEach>
    </ul>
  </c:forEach>
</c:if>

<c:if test="${not empty overconcentratedStocks}">
  <h3>Overconcentrated Stocks</h3>
  <ul>
    <c:forEach var="ps" items="${overconcentratedStocks}">
      <li>${ps.stock.ticker} - ${ps.stock.companyName} (Quantity: ${ps.quantity})</li>
    </c:forEach>
  </ul>
</c:if>

<c:if test="${not empty autoBalanced}">
  <h3>Auto-Balanced Portfolio</h3>
  <table border="1">
    <thead>
    <tr>
      <th>Ticker</th>
      <th>Company</th>
      <th>Industry</th>
      <th>Quantity</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="ps" items="${autoBalanced}">
      <tr>
        <td>${ps.stock.ticker}</td>
        <td>${ps.stock.companyName}</td>
        <td>${ps.stock.industry}</td>
        <td>${ps.quantity}</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</c:if>

<c:if test="${not empty error}">
  <p style="color:red;">${error}</p>
</c:if>

<br>
<a href="${pageContext.request.contextPath}/portfolio">← Back to Portfolio</a>

</body>
</html>
