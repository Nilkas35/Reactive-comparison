package org.acme;

import io.smallrye.mutiny.Uni;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Duration;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from RESTEasy Reactive";
    }

/*
    @GET
    @Path("/example")
    @Produces(MediaType.APPLICATION_JSON)

    public String example() {


        Uni.createFrom().item(1)
                .onItem().transform(i -> "hello-" + i)
                .onItem().delayIt().by(Duration.ofMillis(100))
                .subscribe().with(System.out::println);


        return "hello";
    }*/

}