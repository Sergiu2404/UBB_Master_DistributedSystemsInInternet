<%--
  Created by IntelliJ IDEA.
  User: Sergiu
  Date: 03.11.2025
  Time: 18:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Countries</title>
    <script>
        function openModal() {
            document.getElementById("addModal").style.display = "block";
        }

        function closeModal() {
            document.getElementById("addModal").style.display = "none";
        }

        function submitForm(event) {
            event.preventDefault();

            const name = document.getElementById("name").value;
            const region = document.getElementById("region").value;

            fetch('countries', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: `name=` + encodeURIComponent(name) + `&region=` + encodeURIComponent(region)
            }).then(() => location.reload());
        }
    </script>
</head>
<body>
<h1>Countries</h1>
<ul id="countriesList">
    <c:forEach var="country" items="${countries}">
        <li>${country.name} (${country.region})</li>
    </c:forEach>
</ul>
<button onclick="openModal()">Add Country</button>
<div id="addModal" style="display:none;">
    <h3>Add Country</h3>
    <form onsubmit="submitForm(event)">
        Name: <input id="name" type="text" required><br>
        Region: <input id="region" type="text" required><br>
        <button type="submit">Save</button>
    </form>
    <button onclick="closeModal()">Close</button>
</div>
</body>
</html>
