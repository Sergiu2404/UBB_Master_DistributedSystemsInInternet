<%@ page import="com.example.weather_app.models.Forecast" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Check weather at desired location for the next 7 days</title>
</head>
<body>

<form action="forecasts" method="get">
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
    List<Forecast> forecasts =
            (List<Forecast>) request.getAttribute("filteredForecasts");

    if (forecasts != null && !forecasts.isEmpty()) {
%>
<h3>Weather forecast results:</h3>
<table border="1" cellpadding="5">
    <tr>
        <th>Date</th>
        <th>Min Temp</th>
        <th>Max Temp</th>
    </tr>

    <% for (Forecast f : forecasts) { %>
    <tr>
        <td><%= f.getDate() %></td>
        <td><%= f.getMinTemp() %></td>
        <td><%= f.getMaxTemp() %></td>
    </tr>
    <% } %>
</table>
<%
    }
%>



</body>
</html>