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
        <input type="text" name="max_temp"/> <br>
        Give a min temperature:
        <input type="text" name="min_temp"/> <br>
        <input type="submit" value="Send"/> <br>
    </label>
</form>
</body>
</html>