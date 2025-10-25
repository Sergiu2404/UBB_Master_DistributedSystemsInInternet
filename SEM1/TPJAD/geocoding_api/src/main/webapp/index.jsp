<%@ page import="com.weather_app.models.Forecast" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Check weather at your desired location</title>
</head>
<style>

</style>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>

<form action="geocoding" method="get">
    <label>
        Give a place for which you want to see the weather:
        <input type="text" name="location"/> <br>
        Give a max temperature:
        <input type="number" name="max_temp" step="0.01" min="-90" max="90"> <br>
        Give a min temperature:
        <input type="number" name="min_temp" step="0.01" min="-90" max="90"> <br>
        <input type="submit" value="Send"/> <br>
    </label>
</form>

<% String errorMsg = (String) request.getAttribute("error"); %>
<% if (errorMsg != null) { %>
<p style="color:red;"><%= errorMsg %></p>
<% } %>


<%
    List<Forecast> filteredForecasts =
            (List<Forecast>) request.getAttribute("filteredForecasts");
%>

<% if (filteredForecasts != null) { %>

<% if (filteredForecasts.isEmpty()) { %>
<p>No forecasts found within the given temperature range.</p>
<% } else { %>
<p>Matching forecasts for <strong><%= request.getAttribute("location") %></strong>:</p>
<ul>
    <% for (Forecast f : filteredForecasts) { %>
    <li>
        Date: <%= f.getDate() %> —
        Min: <%= f.getMinTemp() %> °C —
        Max: <%= f.getMaxTemp() %> °C
    </li>
    <% } %>
</ul>
<% } %>

<% } %>

</body>
</html>