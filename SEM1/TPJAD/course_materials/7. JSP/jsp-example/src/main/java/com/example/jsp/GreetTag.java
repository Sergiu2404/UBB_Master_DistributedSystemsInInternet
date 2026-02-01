package com.example.jsp;

import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class GreetTag extends SimpleTagSupport {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void doTag() throws IOException {
        getJspContext().getOut().write("Hello, " + name + "!");
    }
}