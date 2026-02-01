package com.example.jsp;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class CustomDivTag extends SimpleTagSupport {
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        out.write("<div>");
        out.write("<h1>" + title + "</h1>");
        out.write("</div>");
    }
}