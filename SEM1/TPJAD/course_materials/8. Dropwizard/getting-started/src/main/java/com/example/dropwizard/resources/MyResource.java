package com.example.dropwizard.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/")
public class MyResource {
    @GET
    public String hello() {
        return "Hello, world!";
    }
}