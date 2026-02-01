
## **Jakarta JSP and Jakarta JSF**

---

## **Introduction**
### **Jakarta JSP (Jakarta Server Pages)**  
Jakarta Server Pages (JSP) is part of the Jakarta EE platform and provides a way to create dynamic web content. It enables embedding Java code into HTML or XML, allowing for the seamless creation of server-side logic and presentation layers.

### **Jakarta JSF (Jakarta Server Faces)**  
Jakarta Server Faces (JSF) is a framework for building component-based user interfaces in Jakarta EE. It simplifies UI creation by providing reusable components, managed beans, and integration with backend services.

---

## **Getting Started with Jakarta JSP**

### **Structure of a JSP File**
A Jakarta JSP file works similarly to JavaEE JSP but follows Jakarta EE standards.  
Example:  
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Jakarta JSP Example</title>
</head>
<body>
    <h1>Hello, <%= request.getParameter("user") %>!</h1>
</body>
</html>
```

### **Most Used Tags in Jakarta JSP**
| Tag                           | Description                                                                 |
|-------------------------------|-----------------------------------------------------------------------------|
| `<%@ page %>`                 | Defines page directives like language, content type, and encoding.         |
| `<%@ include %>`              | Includes a static file during translation time.                            |
| `<jsp:include>`               | Dynamically includes another resource during runtime.                      |
| `<jsp:useBean>`               | Declares a Jakarta Bean and makes it accessible in the page.               |
| `<jsp:setProperty>`           | Sets a property of a Jakarta Bean.                                         |
| `<jsp:getProperty>`           | Retrieves a property from a Jakarta Bean.                                  |
| `<%! ... %>`                  | Declaration tag for defining variables and methods accessible globally.     |
| `<%= ... %>`                  | Expression tag for outputting the result of a Java expression.             |

### **Custom Tags in Jakarta JSP**
Custom tags enable the extension of Jakarta JSP functionality.

#### **Steps to Create a Custom Tag**
1. **Create a Tag Handler Class**:  
   ```java
   package com.example.tags;

   import jakarta.servlet.jsp.tagext.TagSupport;

   public class CustomTag extends TagSupport {
       @Override
       public int doStartTag() {
           try {
               pageContext.getOut().print("This is a Jakarta Custom Tag!");
           } catch (Exception e) {
               e.printStackTrace();
           }
           return SKIP_BODY;
       }
   }
   ```

2. **Define Tag in TLD File**:  
   ```xml
   <tag>
       <name>customTag</name>
       <tag-class>com.example.tags.CustomTag</tag-class>
       <body-content>empty</body-content>
   </tag>
   ```

3. **Use the Tag in JSP**:  
   ```jsp
   <%@ taglib prefix="ex" uri="custom-taglib" %>
   <ex:customTag />
   ```

---

## **Getting Started with Jakarta JSF**

### **Structure of a Jakarta JSF File**
Jakarta JSF uses Facelets, an XML-based templating technology.  
Example:  
```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="jakarta.faces.html">
<head>
    <title>Jakarta JSF Example</title>
</head>
<body>
    <h:form>
        <h:outputLabel for="name" value="Name: " />
        <h:inputText id="name" value="#{userBean.name}" />
        <h:commandButton value="Submit" action="#{userBean.submit}" />
    </h:form>
</body>
</html>
```

### **Most Known Jakarta JSF Tags**
| Tag                             | Description                                                                 |
|---------------------------------|-----------------------------------------------------------------------------|
| `<h:form>`                      | Defines a form for sending data to the server.                             |
| `<h:inputText>`                 | Creates a text input component.                                            |
| `<h:commandButton>`             | Submits a form or triggers an action.                                      |
| `<h:outputLabel>`               | Displays a label for an input field.                                       |
| `<h:outputText>`                | Outputs plain text.                                                        |
| `<f:convertNumber>`             | Formats numbers in a localized format.                                     |
| `<f:convertDateTime>`           | Formats dates into a readable format.                                      |
| `<f:ajax>`                      | Adds Ajax behavior to a component.                                         |

### **Creating Custom Components in Jakarta JSF**
1. **Define the Component Class**:  
   ```java
   @FacesComponent("com.example.CustomButton")
   public class CustomButton extends UIComponentBase {
       @Override
       public String getFamily() {
           return "jakarta.faces.Output";
       }
   }
   ```

2. **Add to the Tag Library Descriptor**:  
   ```xml
   <tag>
       <tag-name>customButton</tag-name>
       <component>
           <component-type>com.example.CustomButton</component-type>
       </component>
   </tag>
   ```

3. **Use the Component in JSF File**:  
   ```xml
   <custom:customButton />
   ```

---

## **Exercises**

1. **Jakarta JSP Exercises**:
   - Build a Jakarta JSP page that calculates the factorial of a number input by the user.
   - Use `jsp:include` to modularize a JSP application into header, body, and footer components.

2. **Jakarta JSF Exercises**:
   - Create a login form using Jakarta JSF with input validation.
   - Develop a master-detail interface using JSF managed beans and components.

3. **Advanced Tasks**:
   - Implement a Jakarta Custom JSP tag to format a timestamp.
   - Build a Jakarta JSF composite component for displaying paginated data.

---

## **Bibliography**
- [Jakarta JSP Specification](https://jakarta.ee/specifications/jsp/)
- [Jakarta JSF Specification](https://jakarta.ee/specifications/faces/)
- Core JavaServer Faces (Book)
- Head First Servlets and JSP (Book)
- [Jakarta EE Platform](https://jakarta.ee/)

---

