package com.example.jsp;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.Tag;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.io.IOException;
import java.util.List;

public class CustomTagHandler extends TagSupport {
    private List<String> items; // List of items to display in the table

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();

        try {
            // Generate HTML table
            out.println("<table border='1'>");
            out.println("<tr><th>Index</th><th>Item</th></tr>");

            if (items != null && !items.isEmpty()) {
                for (int i = 0; i < items.size(); i++) {
                    out.println("<tr><td>" + i + "</td><td>" + items.get(i) + "</td></tr>");
                }
            } else {
                out.println("<tr><td colspan='2'>No items to display</td></tr>");
            }

            out.println("</table>");
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }

        return Tag.SKIP_BODY;
    }
}