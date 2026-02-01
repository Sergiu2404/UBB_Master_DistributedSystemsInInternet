<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://example.com/custom-tags" prefix="custom" %>
<html>
<head>
    <title>JSP Examples</title>
    <script>
        function test() {
            alert("Hello new world!");
        }
    </script>
</head>
<body>
<h1>Simple JSP Examples</h1>
<p>Invoke HTML rendered by Servlet: <a href="ExampleOne" target="_blank">here</a></p>
<p>Java in static page: <a href="ExampleTwo.jsp" target="_blank">here</a></p>
<p>Java injected by Servlet: <a href="ExampleThree?message=hello!" target="_blank">here</a></p>

Custom tag:
<custom:customDiv title="Welcome to Custom Tags!"/>
<hr>
<custom:greet name="John"/>
<hr>

<%-- Example list of items --%>
<%
    List<String> itemList = Arrays.asList("Item 1", "Item 2", "Item 3", "Item 4");
    pageContext.setAttribute("items", itemList);
%>
<custom:customTag items="${items}" />

<hr>
<button onclick="test()">Click me!</button>
</body>
</html>