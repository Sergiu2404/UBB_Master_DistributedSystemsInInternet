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
<a href="hello-servlet">Hello Servlet</a>

<form action="geocoding" method="get">
<label>
    Give a place for which you want to see the weather:
    <input type="text" name="location"/>
    <input type="submit" value="Send"/>
</label>
</form>
</body>
</html>