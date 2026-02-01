package com.example.dropwizard;

import com.example.dropwizard.dao.CarDAO;
import com.example.dropwizard.entities.Car;
import com.example.dropwizard.health.TemplateHealthCheck;
import com.example.dropwizard.resources.CarResource;
import com.example.dropwizard.resources.HelloWorldResource;
import com.example.dropwizard.resources.MyResource;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.hibernate.HibernateBundle;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {
    private final HibernateBundle<HelloWorldConfiguration> hibernateBundle =
            new HibernateBundle<>(Car.class) {
                @Override
                public io.dropwizard.db.DataSourceFactory getDataSourceFactory(HelloWorldConfiguration configuration) {
                    return configuration.getDatabase();
                }
            };


    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(HelloWorldConfiguration configuration, Environment environment) {
        // getting-started: HelloWorldApplication#run->HelloWorldResource
        HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(resource);
        environment.jersey().register(new MyResource());
        // getting-started: HelloWorldApplication#run->HelloWorldResource

        // getting-started: HelloWorldApplication#run->TemplateHealthCheck
        TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        // getting-started: HelloWorldApplication#run->TemplateHealthCheck


        final CarDAO userDAO = new CarDAO(hibernateBundle.getSessionFactory());

        // Register Resources
        environment.jersey().register(new CarResource(userDAO));
    }
}
