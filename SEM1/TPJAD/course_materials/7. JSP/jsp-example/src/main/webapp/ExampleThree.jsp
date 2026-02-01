<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
  <head>
    <title>Java Binding Example</title>
  </head>
  <body>
    <h1>Bound Value</h1>
    <%
      if (true) {
    %>
	<p>You said: ${unsafeText}</p>
   <%
     }
   %>
  </body>
</html>