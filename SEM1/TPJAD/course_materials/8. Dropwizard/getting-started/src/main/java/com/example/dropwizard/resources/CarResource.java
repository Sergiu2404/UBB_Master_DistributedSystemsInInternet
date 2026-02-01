package com.example.dropwizard.resources;

import com.example.dropwizard.dao.CarDAO;
import com.example.dropwizard.entities.Car;
import io.dropwizard.hibernate.UnitOfWork;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarResource {
    private final CarDAO carDAO;

    public CarResource(CarDAO userDAO) {
        this.carDAO = userDAO;
    }

    @GET
    @UnitOfWork
    public List<Car> getCars(@QueryParam("id") Optional<Long> id) {
        List<Car> cars = new ArrayList<>();
        if (id.isPresent()) {
            cars.add(carDAO.findById(id.orElse(1L)));
        } else {
            cars.addAll(carDAO.findAll());
        }
        return cars;
    }

    @POST
    @UnitOfWork
    public Car createCar(Car user) {
        return carDAO.create(user);
    }
}